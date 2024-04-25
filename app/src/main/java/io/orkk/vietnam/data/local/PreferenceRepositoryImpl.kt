package io.orkk.vietnam.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class PreferenceRepositoryImpl @Inject constructor(
    private val prefDatastore: DataStore<Preferences>
) : PreferenceRepository {
    override suspend fun setIsSavePassword(isSaved: Boolean) {
        prefDatastore.edit { it[PREFERENCES_KEY_OF_IS_SAVE_PASSWORD] = isSaved }
    }

    override val isSavePassword: Flow<Boolean>
        get() = prefDatastore.data.map { it[PREFERENCES_KEY_OF_IS_SAVE_PASSWORD] ?: false }

    override val savedPassword: Flow<String?>
        get() = prefDatastore.data.map { it[PREFERENCES_KEY_OF_PASSWORD] }

    override suspend fun setSavePassword(password: String?) {
        if (password != null) {
            prefDatastore.edit { it[PREFERENCES_KEY_OF_PASSWORD] = password }
        }
    }

    companion object {
        const val DATA_STORE_NAME = "PREFERENCE_DATA"

        val PREFERENCES_KEY_OF_IS_SAVE_PASSWORD = booleanPreferencesKey("preference_is_save_password")
        val PREFERENCES_KEY_OF_PASSWORD = stringPreferencesKey("preference_password")
    }
}