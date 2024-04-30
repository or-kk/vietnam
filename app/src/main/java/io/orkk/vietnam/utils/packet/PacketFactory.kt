package io.orkk.vietnam.utils.packet

import io.orkk.vietnam.model.Constants
import io.orkk.vietnam.model.signin.SignInItem
import io.orkk.vietnam.model.tcpip.RequestPacket
import io.orkk.vietnam.model.tcpip.TXPackets
import io.orkk.vietnam.service.SendPacket
import io.orkk.vietnam.utils.converter.DataUtils
import timber.log.Timber

object PacketFactory {
    var indexPacketSend = 0
    var indexPacketReceive = 0
    var indexPacketPrevReceive = 0

    var lossPackets = ArrayList<Int>()
    var sendPackets: ArrayList<SendPacket> = ArrayList<SendPacket>()

    var packetStartIndex: Int = 0
    var packetEndIndex: Int = 0

    fun makePacket(command: Int, obj: Any): SendPacket {
        Timber.i("PacketFactory -> makePacket -> command -> $command")
        when (command) {
            TXPackets.COMMAND_SIGN_IN -> {
                val signInItem = obj as SignInItem
                val menuVersion = DataUtils.convertDoubleToByteArray(signInItem.menuVer)
                val id = DataUtils.convertIntToByteArray(signInItem.id)
                val cartNumber = byteArrayOf(signInItem.cartNumber)
                val powers = byteArrayOf(signInItem.powerOn)
                val macAddress = DataUtils.convertStringToByteArray(signInItem.macAddress)
                val packetData = menuVersion + id + cartNumber + powers + macAddress!!

                return SendPacket(
                    sendIndex = -1,
                    sendCommand = command,
                    sendPacket = createPacketHeader(TXPackets.COMMAND_SIGN_IN, packetData.size) + packetData
                )
            }

//            TXPackets.COMMAND_REQ_PACKET -> {
//                val bBase1 = DataUtils.convertIntToByte(Constants.COMMAND_BASE_VERSION)
//                val bBase2 = DataUtils.convertIntToByte(TXPackets.COMMAND_REQ_PACKET)
//
//                val bData1 = DataUtils.convertIntToByteArray(RequestPacket.indexOfPacketStart)
//                val bData2 = DataUtils.convertIntToByteArray(RequestPacket.indexOfPacketEnd)
//
//                val bData = ByteArray(bData1.size + bData2.size)
//
//                System.arraycopy(bData1, 0, bData, 0, bData1.size)
//                System.arraycopy(bData2, 0, bData, bData1.size, bData2.size)
//
//                val bBase3 = DataUtils.convertShortToByteArray(bData.size.toShort())
//
//                val bPacket = ByteArray(bBase1.size + bBase2.size + bBase3.size + bData.size)
//                System.arraycopy(bBase1, 0, bPacket, 0, bBase1.size)
//                System.arraycopy(bBase2, 0, bPacket, bBase1.size, bBase2.size)
//                System.arraycopy(bBase3, 0, bPacket, bBase1.size + bBase2.size, bBase3.size)
//                System.arraycopy(bData, 0, bPacket, bBase1.size + bBase2.size + bBase3.size, bData.size)
//
//                return SendPacket(sendIndex = -1, sendCommand = command, sendPacket = bPacket)
//            }

            TXPackets.COMMAND_REQUEST_PACKET -> {
                val requestPacket = obj as RequestPacket
                val packetStartIndexBytes = DataUtils.convertIntToByteArray(requestPacket.indexOfPacketStart)
                val packetEndIndexBytes = DataUtils.convertIntToByteArray(requestPacket.indexOfPacketEnd)
                val packetData = packetStartIndexBytes + packetEndIndexBytes

                return SendPacket(
                    sendIndex = -1,
                    sendCommand = command,
                    sendPacket = createPacketHeader(TXPackets.COMMAND_REQUEST_PACKET, packetData.size) + packetData
                )
            }

            else -> return SendPacket(sendIndex = -1, sendCommand = command, sendPacket = ByteArray(0))
        }
    }

    private fun createPacketHeader(command: Int, packetSize: Int): ByteArray = DataUtils.convertIntToByte(Constants.COMMAND_BASE_VERSION) + DataUtils.convertIntToByte(command) + DataUtils.convertShortToByteArray(packetSize.toShort())
}