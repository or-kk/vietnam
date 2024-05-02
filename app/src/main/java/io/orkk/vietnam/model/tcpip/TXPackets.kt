package io.orkk.vietnam.model.tcpip

class TXPackets {

    companion object {
        const val TRANSMIT_COMMAND_SIGN_IN: Int = 0x1000
        const val TRANSMIT_COMMAND_REQUEST_PACKET: Int = 0x2707

        enum class Packets(val commandName: String, val decimal: Int, val hexadecimal: Int) {
            SIGN_IN("TRANSMIT_COMMAND_SIGN_IN", 4096, TRANSMIT_COMMAND_SIGN_IN),
            REQUEST("TRANSMIT_COMMAND_REQUEST_PACKET", 9991, TRANSMIT_COMMAND_REQUEST_PACKET),
        }

        fun getTransmitCommandNameByHexadecimal(hexadecimal: Int): String {
            for (packet in Packets.entries) {
                if (packet.hexadecimal == hexadecimal) {
                    return packet.commandName
                }
            }
            return "UnKnown"
        }
    }
}