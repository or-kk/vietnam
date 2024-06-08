package io.orkk.vietnam.screen.intro.initial

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.orkk.vietnam.data.local.PreferenceRepository
import io.orkk.vietnam.data.remote.firebase.FirebaseRepository
import io.orkk.vietnam.model.config.ClubInfo
import io.orkk.vietnam.model.config.UrlInfo
import io.orkk.vietnam.screen.BaseViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InitialSettingViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val preferenceRepository: PreferenceRepository,
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel() {

    private val clubIndex: StateFlow<String?> = preferenceRepository.clubIndex.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), ""
    )

    private val clubName: StateFlow<String?> = preferenceRepository.clubName.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), ""
    )

    val isExistClubInfo: StateFlow<Boolean> = combine(clubIndex, clubName) { index, name ->
        index != null && name != null
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)


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

    private val selectClubIndex: StateFlow<String?>
        get() = savedStateHandle.getStateFlow<String?>(KEY_OF_CLUB_INDEX, null)

    private fun setClubIndex() = viewModelScope.launch {
        preferenceRepository.setClubIndex(selectClubIndex.value)
    }

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

    companion object {
        private const val KEY_OF_CLUB_INDEX = "CLUB_INDEX"
//        private const val KEY_OF_ID_EDIT_TEXT_STATE = "SIGN_IN_ID_EDIT_TEXT_STATE"
    }
}