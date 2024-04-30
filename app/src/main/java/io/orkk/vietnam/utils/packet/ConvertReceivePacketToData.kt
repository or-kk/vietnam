package io.orkk.vietnam.utils.packet

import io.orkk.vietnam.model.tcpip.RXPackets
import io.orkk.vietnam.model.tcpip.ReceivePacket
import io.orkk.vietnam.service.SendPacketQueue
import io.orkk.vietnam.utils.converter.DataUtils
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class ConvertReceivePacketToData(private var sendPacketQueue: SendPacketQueue) {

    var receiveTime: Long = System.currentTimeMillis()

    private fun decimalToHex(decimal: Int): String {
        return Integer.toHexString(decimal)
    }

    fun convertPacket(receivePacket: ReceivePacket) {
        receiveTime = System.currentTimeMillis()

        Timber.e("ConvertReceivePacketToData -> ${decimalToHex(receivePacket.command)}")
        when(receivePacket.command) {
            RXPackets.COMMAND_SIGN_IN_OK -> {
                Timber.e("COMMAND_SIGN_IN_OK")
                runBlocking {
                    PacketChannel.sendChannel(RXPackets.COMMAND_SIGN_IN_OK)
                }
            }
            RXPackets.COMMAND_SIGN_IN_FAIL_01 -> {
                Timber.e("COMMAND_SIGN_IN_FAIL_01")
                runBlocking {
                    PacketChannel.sendChannel(RXPackets.COMMAND_SIGN_IN_FAIL_01)
                }
            }

            RXPackets.COMMAND_SIGN_IN_FAIL_02 -> {
                Timber.e("COMMAND_SIGN_IN_FAIL_02")
                runBlocking {
                    PacketChannel.sendChannel(RXPackets.COMMAND_SIGN_IN_FAIL_02)
                }
            }

            RXPackets.COMMAND_SIGN_IN_FAIL_03 -> {
                Timber.e("COMMAND_SIGN_IN_FAIL_03")
                runBlocking {
                    PacketChannel.sendChannel(RXPackets.COMMAND_SIGN_IN_FAIL_03)
                }
            }

            RXPackets.COMMAND_SIGN_IN_FAIL_04 -> {
                Timber.e("COMMAND_SIGN_IN_FAIL_04")
                runBlocking {
                    PacketChannel.sendChannel(RXPackets.COMMAND_SIGN_IN_FAIL_04)
                }
            }

            RXPackets.COMMAND_SIGN_IN_FAIL_05 -> {
                Timber.e("COMMAND_SIGN_IN_FAIL_05")
                runBlocking {
                    PacketChannel.sendChannel(RXPackets.COMMAND_SIGN_IN_FAIL_05)
                }
            }

            RXPackets.COMMAND_REQUEST_PACKET -> {
                val arSPacket = ByteArray(4)
                val arEPacket = ByteArray(4)

                for (i in 0..7) {
                    if (i < 4) {
                        arSPacket[i] = receivePacket.getByteFromData(i)
                    } else {
                        arEPacket[i - 4] = receivePacket.getByteFromData(i)
                    }
                }

                val nSPacket: Int = DataUtils.convertByteToInt(arSPacket, 0)
                val nEPacket: Int = DataUtils.convertByteToInt(arEPacket, 0)

                Timber.d("ProcessReceivePacket >> processPacket() >>  CMD_REQ_PACKET >> nSPacket : $nSPacket nEPacket : $nEPacket")

                val cntSPacket: Int = PacketFactory.sendPackets.size

                if (nEPacket >= nEPacket) {
                    for (i in cntSPacket - 1 downTo 0) {
                        if (PacketFactory.sendPackets[i].sendIndex in nSPacket..nEPacket) {
                            SendPacketQueue.enQueue(PacketFactory.sendPackets[i].sendCommand, PacketFactory.sendPackets[i])
                            PacketFactory.sendPackets.removeAt(i)
                        }
                    }
                } else {
                    for (i in cntSPacket - 1 downTo 0) {
                        if (PacketFactory.sendPackets[i].sendIndex <= nSPacket) {
                            PacketFactory.sendPackets.removeAt(i)
                        }
                    }
                }
            }
        }
    }

    object PacketChannel {
        private val channel = Channel<Any>(BUFFERED)

        suspend fun sendChannel(data: Any) {
            channel.send(data)
        }

        fun receive(): Flow<Any> = channel.receiveAsFlow()
    }
}