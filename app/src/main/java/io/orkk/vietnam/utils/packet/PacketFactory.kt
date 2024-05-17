package io.orkk.vietnam.utils.packet

import io.orkk.vietnam.model.Constants
import io.orkk.vietnam.model.signin.SignIn
import io.orkk.vietnam.model.tcpip.RequestPacket
import io.orkk.vietnam.model.tcpip.TXPackets
import io.orkk.vietnam.model.tcpip.TXPackets.Companion.getTransmitCommandNameByHexadecimal
import io.orkk.vietnam.service.tcp.SendPacket
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
        Timber.i("PacketFactory -> makePacket -> command -> ${getTransmitCommandNameByHexadecimal(command)}")
        when (command) {
            TXPackets.TRANSMIT_COMMAND_SIGN_IN -> {
                val signIn = obj as SignIn
                val menuVersion = DataUtils.convertDoubleToByteArray(signIn.menuVer)
                val id = DataUtils.convertIntToByteArray(signIn.id)
                val cartNumber = byteArrayOf(signIn.id.toByte())
                val powers = byteArrayOf(signIn.powerOn)
                val loginType = DataUtils.convertIntToByteArray(signIn.loginType)
                val macAddress = DataUtils.convertStringToByteArray(signIn.macAddress)
                val packetData = menuVersion + id + cartNumber + powers + loginType + macAddress!!

                return SendPacket(
                    sendIndex = -1,
                    sendCommand = command,
                    sendPacket = createPacketHeader(TXPackets.TRANSMIT_COMMAND_SIGN_IN, packetData.size) + packetData
                )
            }

            TXPackets.TRANSMIT_COMMAND_REQUEST_PACKET -> {
                val requestPacket = obj as RequestPacket
                val packetStartIndexBytes = DataUtils.convertIntToByteArray(requestPacket.indexOfPacketStart)
                val packetEndIndexBytes = DataUtils.convertIntToByteArray(requestPacket.indexOfPacketEnd)
                val packetData = packetStartIndexBytes + packetEndIndexBytes

                return SendPacket(
                    sendIndex = -1,
                    sendCommand = command,
                    sendPacket = createPacketHeader(TXPackets.TRANSMIT_COMMAND_REQUEST_PACKET, packetData.size) + packetData
                )
            }

            else -> return SendPacket(sendIndex = -1, sendCommand = command, sendPacket = ByteArray(0))
        }
    }

    private fun createPacketHeader(command: Int, packetSize: Int): ByteArray = DataUtils.convertIntToByte(Constants.COMMAND_BASE_VERSION) + DataUtils.convertIntToByte(command) + DataUtils.convertShortToByteArray(packetSize.toShort())
}