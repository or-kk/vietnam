package io.orkk.vietnam.utils.packet

import io.orkk.vietnam.model.tcpip.RXPackets
import io.orkk.vietnam.model.tcpip.ReceivePacket
import io.orkk.vietnam.service.SendPacketQueue
import io.orkk.vietnam.utils.converter.DataUtils
import timber.log.Timber

class ConvertReceivePacketToData(private var sendPacketQueue: SendPacketQueue) {

    var receiveTime: Long = System.currentTimeMillis()

    fun convertPacket(receivePacket: ReceivePacket) {
        receiveTime = System.currentTimeMillis()

        when(receivePacket.command) {
            RXPackets.COMMAND_REQ_PACKET -> {
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

                Timber.d("KoProcessReceivePacket >> processPacket() >>  CMD_REQ_PACKET >> nSPacket : $nSPacket nEPacket : $nEPacket")

                val cntSPacket: Int = PacketManager.sendPackets.size

                if (nEPacket >= nEPacket) {
                    for (i in cntSPacket - 1 downTo 0) {
                        if (PacketManager.sendPackets[i].sendIndex in nSPacket..nEPacket) {
                            sendPacketQueue.enQueue(PacketManager.sendPackets[i].sendCommand, PacketManager.sendPackets[i])
                            PacketManager.sendPackets.removeAt(i)
                        }
                    }
                } else {
                    for (i in cntSPacket - 1 downTo 0) {
                        if (PacketManager.sendPackets[i].sendIndex <= nSPacket) {
                            PacketManager.sendPackets.removeAt(i)
                        }
                    }
                }
            }
        }
    }
}