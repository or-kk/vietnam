package io.orkk.vietnam.screen.intro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.orkk.vietnam.data.remote.FileRepository
import io.orkk.vietnam.screen.BaseViewModel
import io.orkk.vietnam.utils.event.Event
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val fileRepository: FileRepository
) : BaseViewModel() {

    private val _navigateToSignIn = MutableLiveData<Event<Unit>>()
    val navigateToSignIn: LiveData<Event<Unit>>
        get() = _navigateToSignIn

    fun navigateToMain() {
        _navigateToSignIn.value = Event(Unit)
    }

    fun downloadAllAppData(remoteFilePath: String, localFilePath: String) {
        viewModelScope.launch {
            fileRepository.downloadFile(remoteFilePath = remoteFilePath, localFilePath = localFilePath).apply {
                onSuccess {

                }
                onFailure {

                }
            }
        }
    }
}