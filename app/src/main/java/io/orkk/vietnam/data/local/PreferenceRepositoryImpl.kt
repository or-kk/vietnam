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

    override val isInitialSetting: Flow<Boolean>
        get() = prefDatastore.data.map { it[PREFERENCES_KEY_OF_IS_INITIAL_SETTING] ?: false }

    override suspend fun setIsInitialSetting(isInitialSetting: Boolean) {
        prefDatastore.edit { it[PREFERENCES_KEY_OF_IS_INITIAL_SETTING] = isInitialSetting }
    }

    override val appDataUpdateList: Flow<String>
        get() = prefDatastore.data.map { it[PREFERENCES_KEY_OF_APP_DATA_VERSION] ?: "" }

    override suspend fun setAppDataUpdateList(version: String?) {
        if (version != null) {
            prefDatastore.edit { it[PREFERENCES_KEY_OF_APP_DATA_VERSION] = version }
        }
    }

    override val latestAppDataVersion: Flow<String>
        get() = prefDatastore.data.map { it[PREFERENCES_KEY_OF_LATEST_APP_DATA_VERSION] ?: "" }

    override suspend fun setLatestAppDataVersion(version: String) {
        prefDatastore.edit { it[PREFERENCES_KEY_OF_LATEST_APP_DATA_VERSION] = version}
    }

    override val clubIndex: Flow<String?>
        get() = prefDatastore.data.map { it[PREFERENCES_KEY_OF_CLUB_INDEX] ?: "" }

    override suspend fun setClubIndex(clubIndex: String?) {
        if (clubIndex != null) {
            prefDatastore.edit { it[PREFERENCES_KEY_OF_CLUB_INDEX] = clubIndex }
        }
    }

    override val clubName: Flow<String?>
        get() = prefDatastore.data.map { it[PREFERENCES_KEY_OF_CLUB_NAME] ?: "" }

    override suspend fun setClubName(clubName: String?) {
        if (clubName != null) {
            prefDatastore.edit { it[PREFERENCES_KEY_OF_CLUB_NAME] = clubName }
        }
    }

    override val downloadMainUrl: Flow<String?>
        get() = prefDatastore.data.map { it[PREFERENCES_KEY_OF_DOWNLOAD_MAIN_URL] ?: "" }

    override suspend fun setDownloadMainUrl(downloadMainUrl: String?) {
        if (downloadMainUrl != null) {
            prefDatastore.edit { it[PREFERENCES_KEY_OF_DOWNLOAD_MAIN_URL] = downloadMainUrl }
        }
    }

    override val savedId: Flow<String?>
        get() = prefDatastore.data.map { it[PREFERENCES_KEY_OF_ID] ?: "" }

    override suspend fun setSaveId(id: String?) {
        if (id != null) {
            prefDatastore.edit { it[PREFERENCES_KEY_OF_ID] = id }
        }
    }

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

        val PREFERENCES_KEY_OF_IS_INITIAL_SETTING = booleanPreferencesKey("preference_is_initial_setting")
        val PREFERENCES_KEY_OF_LATEST_APP_DATA_VERSION = stringPreferencesKey("preference_latest_app_data_version")
        val PREFERENCES_KEY_OF_APP_DATA_VERSION = stringPreferencesKey("preference_app_data_version")
        val PREFERENCES_KEY_OF_CLUB_INDEX = stringPreferencesKey("preference_club_index")
        val PREFERENCES_KEY_OF_CLUB_NAME = stringPreferencesKey("preference_club_name")
        val PREFERENCES_KEY_OF_DOWNLOAD_MAIN_URL = stringPreferencesKey("preference_download_main_url")
        val PREFERENCES_KEY_OF_ID = stringPreferencesKey("preference_id")
        val PREFERENCES_KEY_OF_IS_SAVE_PASSWORD = booleanPreferencesKey("preference_is_save_password")
        val PREFERENCES_KEY_OF_PASSWORD = stringPreferencesKey("preference_password")
    }
}