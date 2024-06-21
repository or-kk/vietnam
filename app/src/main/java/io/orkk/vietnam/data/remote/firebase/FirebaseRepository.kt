package io.orkk.vietnam.data.remote.firebase

import io.orkk.vietnam.model.config.AppdataUpdateInfo
import io.orkk.vietnam.model.config.ClubInfo
import io.orkk.vietnam.model.config.UrlInfo

interface FirebaseRepository {

    suspend fun fetchClubInfoConfig(
        onSuccess: (List<ClubInfo>) -> Unit,
        onFailure: (Exception) -> Unit
    )

    suspend fun fetchUrlInfoConfig(
        onSuccess: (List<UrlInfo>) -> Unit,
        onFailure: (Exception) -> Unit
    )

    suspend fun fetchAppdataUpdateInfoConfig(
        onSuccess: (List<AppdataUpdateInfo>) -> Unit,
        onFailure: (Exception) -> Unit
    )
}