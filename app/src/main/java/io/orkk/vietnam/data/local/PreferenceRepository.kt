package io.orkk.vietnam.data.local

import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {

    val savedId: Flow<String?>
    suspend fun setSaveId(id: String?)

    val isSavePassword: Flow<Boolean>
    suspend fun setIsSavePassword(isSaved: Boolean)

    val savedPassword: Flow<String?>
    suspend fun setSavePassword(password: String?)
}