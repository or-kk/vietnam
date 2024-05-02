package io.orkk.vietnam.utils.packet

import android.content.Context
import io.orkk.vietnam.R
import io.orkk.vietnam.model.tcpip.RXPackets

class PacketUtils {

    companion object {
        @JvmStatic
        fun getSignInFailMessage(context: Context, command: Any): String {
            when (command) {
                RXPackets.RECEIVE_COMMAND_SIGN_IN_FAIL_01 -> {
                    return context.resources.getString(R.string.sign_in_fail_message1)
                }
                RXPackets.RECEIVE_COMMAND_SIGN_IN_FAIL_02 -> {
                    return context.resources.getString(R.string.sign_in_fail_message2)
                }
                RXPackets.RECEIVE_COMMAND_SIGN_IN_FAIL_03 -> {
                    return context.resources.getString(R.string.sign_in_fail_message3)
                }
                RXPackets.RECEIVE_COMMAND_SIGN_IN_FAIL_04 -> {
                    return context.resources.getString(R.string.sign_in_fail_message4)
                }
                RXPackets.RECEIVE_COMMAND_SIGN_IN_FAIL_05 -> {
                    return context.resources.getString(R.string.sign_in_fail_message5)
                }
                else -> return context.resources.getString(R.string.sign_in_fail_message)
            }
        }
    }
}