package io.orkk.vietnam.data.remote.firebase

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.orkk.vietnam.model.config.ClubInfo
import io.orkk.vietnam.model.config.UrlInfo

class FirebaseRepositoryImpl(
    private val firebaseRemoteConfig: FirebaseRemoteConfig
) : FirebaseRepository {

    override suspend fun fetchClubInfoConfig(
        onSuccess: (List<ClubInfo>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firebaseRemoteConfig
            .fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    try {
                        val jsonString = firebaseRemoteConfig.getString(KEY_OF_CLUB_INFO)
                        val gson = Gson()
                        val listType = object : TypeToken<List<UrlInfo>>() {}.type
                        val clubInfoList: List<ClubInfo> = gson.fromJson(jsonString, listType)
                        onSuccess(clubInfoList)
                    } catch (e: Exception) {
                        onFailure(e)
                    }
                }
            }
    }

    override suspend fun fetchUrlInfoConfig(
        onSuccess: (List<UrlInfo>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firebaseRemoteConfig
            .fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    try {
                        val jsonString = firebaseRemoteConfig.getString(KEY_OF_URL_INFO)
                        val gson = Gson()
                        val listType = object : TypeToken<List<UrlInfo>>() {}.type
                        val urlInfoList: List<UrlInfo> = gson.fromJson(jsonString, listType)
                        onSuccess(urlInfoList)
                    } catch (e: Exception) {
                        onFailure(e)
                    }
                }
            }
    }

    companion object {
        const val KEY_OF_CLUB_INFO = "club_info"
        const val KEY_OF_URL_INFO = "url_info"
    }
}