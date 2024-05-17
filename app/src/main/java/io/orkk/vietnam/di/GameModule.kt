package io.orkk.vietnam.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.orkk.vietnam.utils.game.GamePlayManager
import io.orkk.vietnam.utils.game.GameTimeManager
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class GameModule {

    @Provides
    @Singleton
    fun provideGameTimeManager(): GameTimeManager {
        return GameTimeManager()
    }

    @Provides
    @Singleton
    fun provideGamePlayManager(): GamePlayManager {
        return GamePlayManager()
    }
}