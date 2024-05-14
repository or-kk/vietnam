package io.orkk.vietnam.screen.location.blockmode

import android.content.pm.ActivityInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.orkk.vietnam.screen.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class BlockModeViewModel @Inject constructor() : BaseViewModel() {
    private val _currentScreenOrientation = MutableLiveData<Int>()
    val currentScreenOrientation: LiveData<Int>
        get() = _currentScreenOrientation

    val currentParInfo = listOf(4, 3, 5, 4, 3, 4, 5, 4, 4)

    init {
        _currentScreenOrientation.value = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    fun setScreenOrientation() {
        val currentOrientation = _currentScreenOrientation.value ?: return
        _currentScreenOrientation.value = if (currentOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}