package io.orkk.vietnam.data.remote

import io.orkk.vietnam.model.signin.SignInItem
import io.orkk.vietnam.model.tcpip.TXPackets
import io.orkk.vietnam.service.SendPacketQueue
import io.orkk.vietnam.utils.packet.PacketFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val packetFactory: PacketFactory,
) : UserRepository {

    override suspend fun signInWithMiddleware(id: String?, password: String?): Flow<Boolean> = flow {
        Timber.i("sign in id -> $id password -> $password")
        packetFactory.makePacket(command = TXPackets.COMMAND_SIGN_IN, obj = SignInItem(id = id!!.toInt())).apply {
            SendPacketQueue.enQueue(TXPackets.COMMAND_SIGN_IN, this)
            SendPacketQueue.isExistCommand(TXPackets.COMMAND_SIGN_IN).let {
                if (it) emit(true) else emit(false)
            }
        }
    }
}