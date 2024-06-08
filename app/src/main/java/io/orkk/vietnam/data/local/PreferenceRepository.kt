package io.orkk.vietnam.data.local

import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {

    val clubIndex: Flow<String?>
    suspend fun setClubIndex(clubIndex: String?)

//    val isSaveClubIndex: Flow<Boolean>
//    suspend fun setIsSaveClubIndex(isSaved: Boolean)

    val clubName: Flow<String?>

    suspend fun setClubName(clubName: String?)
//    val isSaveClubName: Flow<Boolean>
//    suspend fun setIsSaveClubName(isSaved: Boolean)

    val savedId: Flow<String?>
    suspend fun setSaveId(id: String?)

    val isSavePassword: Flow<Boolean>
    suspend fun setIsSavePassword(isSaved: Boolean)

    val savedPassword: Flow<String?>
    suspend fun setSavePassword(password: String?)
}