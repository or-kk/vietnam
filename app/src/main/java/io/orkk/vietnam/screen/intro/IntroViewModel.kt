package io.orkk.vietnam.screen.intro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.orkk.vietnam.data.remote.file.FileRepository
import io.orkk.vietnam.screen.BaseViewModel
import io.orkk.vietnam.utils.event.Event
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
}