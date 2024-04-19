package io.orkk.vietnam.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.orkk.vietnam.data.remote.UserRepository
import io.orkk.vietnam.data.remote.UserRepositoryImpl
import io.orkk.vietnam.service.SendPacketQueue
import io.orkk.vietnam.utils.packet.PacketManager
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun providePacketManager(): PacketManager {
        return PacketManager
    }

    @Provides
    fun provideSendPacketQueue() : SendPacketQueue {
        return SendPacketQueue()
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        packetManager: PacketManager,
        sendPacketQueue: SendPacketQueue
    ): UserRepository {
        return UserRepositoryImpl(packetManager, sendPacketQueue)
    }
}