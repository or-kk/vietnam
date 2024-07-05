package io.orkk.vietnam.data.local

import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {

    val isInitialSetting: Flow<Boolean>
    suspend fun setIsInitialSetting(isInitialSetting: Boolean)

    val appDataDownloadList: Flow<String>
    suspend fun setAppDataDownloadList(list: String)

    val latestAppDataVersion: Flow<String>
    suspend fun setLatestAppDataVersion(version: String)

    val clubIndex: Flow<String?>
    suspend fun setClubIndex(clubIndex: String?)

    val clubName: Flow<String?>
    suspend fun setClubName(clubName: String?)

    val downloadMainUrl: Flow<String?>
    suspend fun setDownloadMainUrl(downloadMainUrl: String?)

    val savedId: Flow<String?>
    suspend fun setSaveId(id: String?)

    val isSavePassword: Flow<Boolean>
    suspend fun setIsSavePassword(isSaved: Boolean)

    val savedPassword: Flow<String?>
    suspend fun setSavePassword(password: String?)
}