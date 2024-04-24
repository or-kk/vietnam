package io.orkk.vietnam.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.orkk.vietnam.data.local.PreferenceRepository
import io.orkk.vietnam.data.local.PreferenceRepositoryImpl
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class PreferenceModule {

    private val Context.prefDataStore by preferencesDataStore(
        name = PreferenceRepositoryImpl.DATA_STORE_NAME
    )

    @Singleton
    @Provides
    internal fun providePreferenceModule(
        @ApplicationContext context: Context
    ): PreferenceRepository = PreferenceRepositoryImpl(context.prefDataStore)
}