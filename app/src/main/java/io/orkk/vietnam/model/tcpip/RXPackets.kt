package io.orkk.vietnam.model.tcpip

class RXPackets {
    companion object {

        const val RECEIVE_COMMAND_SIGN_IN_OK: Int = 0x1001
        const val RECEIVE_COMMAND_RESERVATION_INFO: Int = 0x2000
        const val RECEIVE_COMMAND_PLAYER_INFO: Int = 0x2001
        const val RECEIVE_COMMAND_GAME_TIME: Int = 0x2004
        const val RECEIVE_COMMAND_PLAYER_INFO_EX: Int = 0x2705
        const val RECEIVE_COMMAND_REQUEST_PACKET: Int = 0x2707
        const val RECEIVE_COMMAND_APP_BLOCK_MODE_SEND: Int = 0x4203
        const val RECEIVE_COMMAND_SIGN_IN_FAIL_01: Int = 0xF001
        const val RECEIVE_COMMAND_SIGN_IN_FAIL_02: Int = 0xF002
        const val RECEIVE_COMMAND_SIGN_IN_FAIL_03: Int = 0xF003
        const val RECEIVE_COMMAND_SIGN_IN_FAIL_04: Int = 0xF004
        const val RECEIVE_COMMAND_SIGN_IN_FAIL_05: Int = 0xF005

        enum class Packets(val commandName: String, val decimal: Int, val hexadecimal: Int) {
            SIGN_IN_OK("RECEIVE_COMMAND_SIGN_IN_OK", 4097, RECEIVE_COMMAND_SIGN_IN_OK),
            RESERVATION_INFO("RECEIVE_COMMAND_RESERVATION_INFO", 8192, RECEIVE_COMMAND_RESERVATION_INFO),
            PLAYER_INFO("RECEIVE_COMMAND_PLAYER_INFO", 8193, RECEIVE_COMMAND_PLAYER_INFO),
            GAME_TIME("RECEIVE_COMMAND_GAME_TIME", 8196, RECEIVE_COMMAND_GAME_TIME),
            PLAYER_INFO_EX("RECEIVE_COMMAND_PLAYER_INFO_EX", 9989, RECEIVE_COMMAND_PLAYER_INFO_EX),
            REQUEST("RECEIVE_COMMAND_REQUEST_PACKET", 9991, RECEIVE_COMMAND_REQUEST_PACKET),
            BLOCK_MODE("RECEIVE_COMMAND_APP_BLOCK_MODE_SEND", 16899, RECEIVE_COMMAND_APP_BLOCK_MODE_SEND),
            SIGN_IN_FAIL_01("RECEIVE_COMMAND_SIGN_IN_FAIL_01", 61441, RECEIVE_COMMAND_SIGN_IN_FAIL_01),
            SIGN_IN_FAIL_02("RECEIVE_COMMAND_SIGN_IN_FAIL_02", 61442, RECEIVE_COMMAND_SIGN_IN_FAIL_02),
            SIGN_IN_FAIL_03("RECEIVE_COMMAND_SIGN_IN_FAIL_03", 61443, RECEIVE_COMMAND_SIGN_IN_FAIL_03),
            SIGN_IN_FAIL_04("RECEIVE_COMMAND_SIGN_IN_FAIL_04", 61444, RECEIVE_COMMAND_SIGN_IN_FAIL_04),
            SIGN_IN_FAIL_05("RECEIVE_COMMAND_SIGN_IN_FAIL_05", 61445, RECEIVE_COMMAND_SIGN_IN_FAIL_05),
        }

        fun getReceiveCommandNameByHexadecimal(hexadecimal: Int): String {
            for (packet in Packets.entries) {
                if (packet.hexadecimal == hexadecimal) {
                    return packet.commandName
                }
            }
            return "UnKnown"
        }
    }
}