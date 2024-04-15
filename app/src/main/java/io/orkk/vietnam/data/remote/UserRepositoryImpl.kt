package io.orkk.vietnam.data.remote

import io.orkk.vietnam.model.signin.SignInItem
import io.orkk.vietnam.model.tcpip.TXPackets
import io.orkk.vietnam.service.SendPacketQueue
import io.orkk.vietnam.utils.packet.PacketManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val packetManager: PacketManager,
    private val sendPacketQueue: SendPacketQueue
) : UserRepository {

    override suspend fun signInWithMiddleware(id: String?, password: String?): Flow<Boolean> = flow {
        packetManager.makePacket(command = TXPackets.COMMAND_SIGN_IN, obj = SignInItem(id = id!!.toInt())).apply {
            sendPacketQueue.enQueue(TXPackets.COMMAND_SIGN_IN, this)
        }
    }
}