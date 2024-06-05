package io.orkk.vietnam.data.remote

import io.orkk.vietnam.model.signin.SignIn
import io.orkk.vietnam.model.tcpip.TXPackets
import io.orkk.vietnam.service.tcp.SendPacketQueue
import io.orkk.vietnam.utils.packet.PacketFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val packetFactory: PacketFactory,
) : UserRepository {

    override suspend fun signInWithMiddleware(id: String?, password: String?, loginType: Int): Flow<Boolean> = flow {
        packetFactory.makePacket(command = TXPackets.TRANSMIT_COMMAND_SIGN_IN, obj = SignIn(id = id!!.toInt(), loginType = loginType)).apply {
            SendPacketQueue.enQueue(TXPackets.TRANSMIT_COMMAND_SIGN_IN, this)
            SendPacketQueue.isExistCommand(TXPackets.TRANSMIT_COMMAND_SIGN_IN).let {
                if (it) emit(true) else emit(false)
            }
        }
    }
}