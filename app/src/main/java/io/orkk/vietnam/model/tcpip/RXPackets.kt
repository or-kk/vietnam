package io.orkk.vietnam.model.tcpip

class RXPackets {
    companion object {
        const val COMMAND_REQUEST_PACKET: Int = 0x2707
        const val COMMAND_SIGN_IN_OK: Int = 0x1001
        const val COMMAND_SIGN_IN_FAIL_01: Int = 0xF001
        const val COMMAND_SIGN_IN_FAIL_02: Int = 0xF002
        const val COMMAND_SIGN_IN_FAIL_03: Int = 0xF003
        const val COMMAND_SIGN_IN_FAIL_04: Int = 0xF004
        const val COMMAND_SIGN_IN_FAIL_05: Int = 0xF005
    }
}