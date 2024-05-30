package io.orkk.vietnam.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.orkk.vietnam.utils.golfclub.GolfClubManager
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class GolfClubModule {

    @Provides
    @Singleton
    fun provideGolfClubModule(): GolfClubManager {
        return GolfClubManager()
    }
}