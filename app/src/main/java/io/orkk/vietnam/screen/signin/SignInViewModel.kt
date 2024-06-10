package io.orkk.vietnam.screen.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.orkk.vietnam.R
import io.orkk.vietnam.data.local.PreferenceRepository
import io.orkk.vietnam.data.remote.user.UserRepository
import io.orkk.vietnam.screen.BaseViewModel
import io.orkk.vietnam.utils.EditTextState
import io.orkk.vietnam.utils.event.Event
import io.orkk.vietnam.utils.packet.ConvertReceivePacketToData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val preferenceRepository: PreferenceRepository,
) : BaseViewModel() {

    private val _navigateToMain = MutableLiveData<Event<Unit>>()
    val navigateToMain: LiveData<Event<Unit>>
        get() = _navigateToMain

    private val _navigateToLocation = MutableLiveData<Event<Unit>>()
    val navigateToLocation: LiveData<Event<Unit>>
        get() = _navigateToLocation

    private val inputId: StateFlow<String?>
        get() = savedStateHandle.getStateFlow<String?>(KEY_OF_ID, null)

    private val savedId: StateFlow<String?> = preferenceRepository.savedId.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), ""
    )

    val id = inputId.combine(savedId) { input, saved ->
        saved ?: input
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val idEditTextState: StateFlow<EditTextState> = savedStateHandle.getStateFlow<EditTextState>(KEY_OF_ID_EDIT_TEXT_STATE, EditTextState.Loading)

    private val inputPassword: StateFlow<String?>
        get() = savedStateHandle.getStateFlow<String?>(KEY_OF_PASSWORD, null)

    private val savedPassword: StateFlow<String?> = preferenceRepository.savedPassword.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), ""
    )

    val password = inputPassword.combine(savedPassword) { input, saved ->
        saved ?: input
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val passwordEditTextState: StateFlow<EditTextState> = savedStateHandle.getStateFlow<EditTextState>(KEY_OF_PASSWORD_EDIT_TEXT_STATE, EditTextState.Loading)

    val isEnableSignIn: StateFlow<Boolean> = combine(idEditTextState, passwordEditTextState) { idState, passwordState ->
        idState == EditTextState.Success && passwordState == EditTextState.Success
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val isSavePassword: StateFlow<Boolean> = preferenceRepository.isSavePassword.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), false
    )

    val loginTypes = listOf<String>("0", "1")

    private val _loginType = MutableLiveData<Int>()
    val loginType: LiveData<Int> = _loginType

    fun setSelectedLoginType(type: String) {
        _loginType.value = type.toInt()
    }

    val dataFlow: Flow<Any> = ConvertReceivePacketToData.PacketChannel.receive()

    fun setSavePassword(isChecked: Boolean) = viewModelScope.launch {
        preferenceRepository.setIsSavePassword(isChecked)
        preferenceRepository.setSavePassword(inputPassword.value)
    }

    private fun setSaveId() = viewModelScope.launch {
        preferenceRepository.setSaveId(inputId.value)
    }

    fun navigateToMain() {
        _navigateToMain.value = Event(Unit)
    }

    fun navigateToLocation() {
        _navigateToLocation.value = Event(Unit)
    }

    fun signInWithMiddleware() {
        viewModelScope.launch {
            setSaveId()
            userRepository.signInWithMiddleware(inputId.value, inputPassword.value, loginType.value!!).onEach {
                setLoading(true)
            }.collect {
                if (it) {
                    Timber.i("true")
                } else {
                    Timber.i("false")
                }
            }
        }
    }

    fun checkIdValidation(id: String) {
        savedStateHandle[KEY_OF_ID] = id

        if (id.isEmpty()) {
            setIdEditTextState(EditTextState.Error(R.string.sign_in_id_empty_error))
        } else if (id.toInt() in 1 until 256) {
            setIdEditTextState(EditTextState.Success)
        } else {
            setIdEditTextState(EditTextState.Error(R.string.sign_in_id_common_error))
        }
    }

    fun checkPasswordValidation(password: String) {
        savedStateHandle[KEY_OF_PASSWORD] = password

        if (password.isEmpty()) {
            setPasswordEditTextState(EditTextState.Error(R.string.sign_in_password_empty_error))
        } else {
            setPasswordEditTextState(EditTextState.Success)
        }
    }

    fun setIdEditTextState(editTextState: EditTextState) {
        if (this.idEditTextState.value == editTextState) return
        savedStateHandle[KEY_OF_ID_EDIT_TEXT_STATE] = editTextState
    }

    fun setPasswordEditTextState(editTextState: EditTextState) {
        if (this.passwordEditTextState == editTextState) return
        savedStateHandle[KEY_OF_PASSWORD_EDIT_TEXT_STATE] = editTextState
    }

    companion object {
        private const val KEY_OF_ID = "SIGN_IN_ID"
        private const val KEY_OF_ID_EDIT_TEXT_STATE = "SIGN_IN_ID_EDIT_TEXT_STATE"

        private const val KEY_OF_PASSWORD = "SIGN_IN_PASSWORD"
        private const val KEY_OF_PASSWORD_EDIT_TEXT_STATE = "SIGN_IN_PASSWORD_EDIT_TEXT_STATE"
    }
}