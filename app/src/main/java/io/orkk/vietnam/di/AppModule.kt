package io.orkk.vietnam.di

import android.content.Context
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.orkk.vietnam.data.local.LocationRepository
import io.orkk.vietnam.data.remote.file.FileRepository
import io.orkk.vietnam.data.remote.file.FileRepositoryImpl
import io.orkk.vietnam.data.remote.firebase.FirebaseRepository
import io.orkk.vietnam.data.remote.firebase.FirebaseRepositoryImpl
import io.orkk.vietnam.data.remote.user.UserRepository
import io.orkk.vietnam.data.remote.user.UserRepositoryImpl
import io.orkk.vietnam.service.tcp.SendPacketQueue
import io.orkk.vietnam.utils.packet.PacketFactory
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun providePacketFactory(): PacketFactory {
        return PacketFactory
    }

    @Provides
    fun provideSendPacketQueue(): SendPacketQueue {
        return SendPacketQueue()
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        packetFactory: PacketFactory
    ): UserRepository {
        return UserRepositoryImpl(packetFactory)
    }

    @Provides
    @Singleton
    fun provideLocationRepository(
        @ApplicationContext context: Context
    ): LocationRepository {
        return LocationRepository(context)
    }

    @Provides
    @Singleton
    fun provideFileRepository(
        okHttpClient: OkHttpClient
    ): FileRepository {
        return FileRepositoryImpl(okHttpClient)
    }

    @Provides
    @Singleton
    fun provideFirebaseRepository(
        firebaseRemoteConfig: FirebaseRemoteConfig
    ): FirebaseRepository {
        return FirebaseRepositoryImpl(firebaseRemoteConfig)
    }
}