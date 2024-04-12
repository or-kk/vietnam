package io.orkk.vietnam.utils.packet

import io.orkk.vietnam.model.Constants
import io.orkk.vietnam.model.signin.SignInItem
import io.orkk.vietnam.model.tcpip.TXPackets
import io.orkk.vietnam.service.SendPacket
import io.orkk.vietnam.utils.converter.DataUtils

object PacketManager {
    var indexPacketSend = 0
    var indexPacketReceive = 0
    var indexPacketPrevReceive = 0

    var lossPackets = ArrayList<Int>()
    var sendPackets: ArrayList<SendPacket> = ArrayList<SendPacket>()

    fun makePacket(command: Int, obj: Any): SendPacket {
        when (command) {
            TXPackets.COMMAND_SIGN_IN -> {
                val signInItem = obj as SignInItem

                val bBase1 = DataUtils.convertIntToByte(Constants.COMMAND_BASE_VERSION)
                val bBase2 = DataUtils.convertIntToByte(TXPackets.COMMAND_SIGN_IN)

                val bData1 = DataUtils.convertDoubleToByteArray(signInItem.menuVer)
                val bData2 = DataUtils.convertIntToByteArray(signInItem.caddyNumber)
                val bData3 = ByteArray(1)
                bData3[0] = signInItem.cartNumber
                val bData4 = ByteArray(1)
                bData4[0] = signInItem.powerOn
                val bData5 = DataUtils.convertStringToByteArray(signInItem.macAddress)

                val bData = ByteArray(bData1.size + bData2.size + bData3.size + bData4.size + bData5!!.size)
                System.arraycopy(bData1, 0, bData, 0, bData1.size)
                System.arraycopy(bData2, 0, bData, bData1.size, bData2.size)
                System.arraycopy(bData3, 0, bData, bData1.size + bData2.size, bData3.size)
                System.arraycopy(bData4, 0, bData, bData1.size + bData2.size + bData3.size, bData4.size)
                System.arraycopy(bData5, 0, bData, bData1.size + bData2.size + bData3.size + bData4.size, bData5.size)

                val bBase3 = DataUtils.convertShortToByteArray(bData.size.toShort())

                val bPacket = ByteArray(bBase1.size + bBase2.size + bBase3.size + bData.size)
                System.arraycopy(bBase1, 0, bPacket, 0, bBase1.size)
                System.arraycopy(bBase2, 0, bPacket, bBase1.size, bBase2.size)
                System.arraycopy(bBase3, 0, bPacket, bBase1.size + bBase2.size, bBase3.size)
                System.arraycopy(bData, 0, bPacket, bBase1.size + bBase2.size + bBase3.size, bData.size)
                return SendPacket(sendIndex = -1, sendCommand = command, sendPacket = bPacket)
            }

            else -> return SendPacket(sendIndex = -1, sendCommand = command, sendPacket = ByteArray(0))
        }
    }
}