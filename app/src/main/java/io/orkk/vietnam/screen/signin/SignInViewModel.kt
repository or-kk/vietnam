package io.orkk.vietnam.screen.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.orkk.vietnam.screen.BaseViewModel
import io.orkk.vietnam.utils.event.Event
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor() : BaseViewModel() {

    private val _navigateToMain = MutableLiveData<Event<Unit>>()
    val navigateToMain: LiveData<Event<Unit>>
        get() = _navigateToMain

    fun navigateToMain() {
        _navigateToMain.value = Event(Unit)
    }
}