package io.orkk.vietnam.screen.intro.initial

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.orkk.vietnam.data.remote.firebase.FirebaseRepository
import io.orkk.vietnam.model.config.ClubInfo
import io.orkk.vietnam.model.config.UrlInfo
import io.orkk.vietnam.screen.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InitialSettingViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel() {

    private val _clubInfoList = MutableLiveData<List<ClubInfo>>()
    val clubInfoList: LiveData<List<ClubInfo>>
        get() = _clubInfoList

    private val _clubInfoFetchError = MutableLiveData<String>()
    val clubInfoFetchError: LiveData<String>
        get() = _clubInfoFetchError

    private val _urlInfoList = MutableLiveData<List<UrlInfo>>()
    val urlInfoList: LiveData<List<UrlInfo>>
        get() = _urlInfoList

    private val _urlInfoFetchError = MutableLiveData<String>()
    val urlInfoFetchError: LiveData<String>
        get() = _urlInfoFetchError

    fun fetchClubInfoConfig() {
        viewModelScope.launch {
            firebaseRepository.fetchClubInfoConfig(
                onSuccess = { configList ->
                    _clubInfoList.value = configList
                },
                onFailure = { exception ->
                    _clubInfoFetchError.value = exception.message
                }
            )
        }
    }

    fun fetchUrlInfoConfig() {
        viewModelScope.launch {
            firebaseRepository.fetchUrlInfoConfig(
                onSuccess = { configList ->
                    _urlInfoList.value = configList
                },
                onFailure = { exception ->
                    _urlInfoFetchError.value = exception.message
                }
            )
        }
    }
}