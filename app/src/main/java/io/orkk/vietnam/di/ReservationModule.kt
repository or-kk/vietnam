package io.orkk.vietnam.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.orkk.vietnam.utils.reservation.ReservationManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ReservationModule {

    @Provides
    @Singleton
    fun provideLocationManager(): ReservationManager {
        return ReservationManager()
    }
}