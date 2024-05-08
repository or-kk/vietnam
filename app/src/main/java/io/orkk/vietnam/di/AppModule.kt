package io.orkk.vietnam.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.orkk.vietnam.data.remote.UserRepository
import io.orkk.vietnam.data.remote.UserRepositoryImpl
import io.orkk.vietnam.service.tcp.SendPacketQueue
import io.orkk.vietnam.utils.packet.PacketFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun providePacketFactory(): PacketFactory {
        return PacketFactory
    }

    @Provides
    fun provideSendPacketQueue() : SendPacketQueue {
        return SendPacketQueue()
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        packetFactory: PacketFactory
    ): UserRepository {
        return UserRepositoryImpl(packetFactory)
    }
}