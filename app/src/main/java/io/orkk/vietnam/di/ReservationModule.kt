package io.orkk.vietnam.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.orkk.vietnam.utils.reservation.ReservationManager
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ReservationModule {

    @Provides
    @Singleton
    fun provideLocationManager(): ReservationManager {
        return ReservationManager()
    }
}