package io.orkk.vietnam.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.orkk.vietnam.utils.location.LocationManager
import io.orkk.vietnam.utils.location.GpsManager
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class LocationModule {

    @Provides
    @Singleton
    fun provideGpsManager(): GpsManager {
        return GpsManager()
    }

    @Provides
    @Singleton
    fun provideLocationManager(): LocationManager {
        return LocationManager()
    }
}