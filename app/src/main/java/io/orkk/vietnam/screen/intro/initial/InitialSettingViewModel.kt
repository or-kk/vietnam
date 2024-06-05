package io.orkk.vietnam.screen.intro.initial

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.orkk.vietnam.data.remote.firebase.FirebaseRepository
import io.orkk.vietnam.model.config.ClubInfo
import io.orkk.vietnam.screen.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InitialSettingViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): BaseViewModel() {

    private val _clubInfoList = MutableLiveData<List<ClubInfo>>()
    val configList: LiveData<List<ClubInfo>>
        get() = _clubInfoList

    private val _clubInfoFetchError = MutableLiveData<String>()
    val clubInfoFetchError: LiveData<String>
        get() = _clubInfoFetchError

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
}