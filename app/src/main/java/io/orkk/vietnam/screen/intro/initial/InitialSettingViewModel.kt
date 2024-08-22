package io.orkk.vietnam.screen.intro.initial

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.orkk.vietnam.data.local.PreferenceRepository
import io.orkk.vietnam.data.remote.firebase.FirebaseRepository
import io.orkk.vietnam.model.config.AppdataUpdateInfo
import io.orkk.vietnam.model.config.ClubInfo
import io.orkk.vietnam.model.config.UrlInfo
import io.orkk.vietnam.screen.BaseViewModel
import io.orkk.vietnam.utils.event.Event
import io.orkk.vietnam.utils.extension.asStateFlow
import io.orkk.vietnam.utils.extension.whileSubscribed
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InitialSettingViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel() {

    val isInitialSetting: StateFlow<Boolean?> = preferenceRepository.isInitialSetting.stateIn(
        viewModelScope, whileSubscribed(), null
    )

    private val clubIndex: StateFlow<String?> = preferenceRepository.clubIndex.stateIn(
        viewModelScope, whileSubscribed(), null
    )

    private val clubName: StateFlow<String?> = preferenceRepository.clubName.stateIn(
        viewModelScope, whileSubscribed(), null
    )

    private val _clubInfoFetchedList = MutableLiveData<List<ClubInfo>>()
    val clubInfoFetchedList: LiveData<List<ClubInfo>>
        get() = _clubInfoFetchedList

    private val _clubInfoFetchError = MutableLiveData<String>()
    val clubInfoFetchError: LiveData<String>
        get() = _clubInfoFetchError

    private val _urlInfoFetchedList = MutableLiveData<List<UrlInfo>>()
    val urlInfoFetchedList: LiveData<List<UrlInfo>>
        get() = _urlInfoFetchedList

    private val _urlInfoFetchError = MutableLiveData<String>()
    val urlInfoFetchError: LiveData<String>
        get() = _urlInfoFetchError

    private val _appdataUpdateInfoList = MutableLiveData<List<AppdataUpdateInfo>>()
    val appdataUpdateInfoList: LiveData<List<AppdataUpdateInfo>>
        get() = _appdataUpdateInfoList

    private val _appdataUpdateInfoFetchError = MutableLiveData<String>()
    val appdataUpdateInfoFetchError: LiveData<String>
        get() = _appdataUpdateInfoFetchError

    private val _selectedClubInfo = MutableLiveData<ClubInfo>()
    val selectedClubInfo: LiveData<ClubInfo>
        get() = _selectedClubInfo

    val isEnableSetClubInfo: StateFlow<Boolean> = _selectedClubInfo.asStateFlow(viewModelScope).map {
        it?.clubIndex != "0" && it?.clubIndex != null
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    private val _navigateToDownload = MutableLiveData<Event<Unit>>()
    val navigateToDownload: LiveData<Event<Unit>>
        get() = _navigateToDownload

    fun setSelectedClub(position: Int) {
        val list = _clubInfoFetchedList.value
        list?.let {
            if (position in it.indices) {
                _selectedClubInfo.value = it[position]
            }
        }
    }

    fun fetchClubInfoConfig() {
        viewModelScope.launch {
            firebaseRepository.fetchClubInfoConfig(
                onSuccess = { configList ->
                    _clubInfoFetchedList.value = configList
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
                    _urlInfoFetchedList.value = configList
                },
                onFailure = { exception ->
                    _urlInfoFetchError.value = exception.message
                }
            )
        }
    }

    fun fetchAppdataUpdateInfoConfig() {
        viewModelScope.launch {
            firebaseRepository.fetchAppdataUpdateInfoConfig(
                onSuccess = { configList ->
                    _appdataUpdateInfoList.value = configList
                },
                onFailure = { exception ->
                    _appdataUpdateInfoFetchError.value = exception.message
                }
            )
        }
    }

    fun setGolfClubInfo() = viewModelScope.launch {
        with(preferenceRepository) {
            setClubIndex(clubIndex = selectedClubInfo.value?.clubIndex)
            setClubName(clubName = selectedClubInfo.value?.clubName)
            setIsInitialSetting(isInitialSetting = true)
        }
        setDownloadUrlInfo()
        setAppDataUpdateInfo()
        navigateToDownload()
    }

    private fun setDownloadUrlInfo() = viewModelScope.launch {
        val mainUrl = urlInfoFetchedList.value?.find { it.clubIndex == selectedClubInfo.value?.clubIndex }
        preferenceRepository.setDownloadMainUrl(downloadMainUrl = mainUrl!!.downloadUrl)
    }

    private fun setAppDataUpdateInfo() = viewModelScope.launch {
        val updateInfo = appdataUpdateInfoList.value?.find { it.clubIndex == selectedClubInfo.value?.clubIndex }
        preferenceRepository.setAppDataUpdateList(updateInfo?.downloadFileList)
    }

    private fun navigateToDownload() {
        _navigateToDownload.value = Event(Unit)
    }
}