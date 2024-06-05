package io.orkk.vietnam.data.remote

import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun signInWithMiddleware(id: String?, password: String?, loginType: Int): Flow<Boolean>
}