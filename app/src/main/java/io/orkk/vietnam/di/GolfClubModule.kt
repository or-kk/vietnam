package io.orkk.vietnam.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.orkk.vietnam.utils.golfclub.GolfClubManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GolfClubModule {
    @Provides
    @Singleton
    fun provideGolfClubModule(): GolfClubManager {
        return GolfClubManager()
    }
}