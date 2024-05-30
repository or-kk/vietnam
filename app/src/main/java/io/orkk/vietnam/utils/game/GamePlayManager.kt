package io.orkk.vietnam.utils.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class GamePlayManager {

    var progressCourseCount = 0
    var progressHoleCount = 0

    private val _gamePlayState = MutableLiveData<Int>()
    val gamePlayState: LiveData<Int>
        get() = _gamePlayState

    private val _isCourseStart = MutableLiveData<Boolean>(false)
    val isCourseStart: LiveData<Boolean>
        get() = _isCourseStart

    private val _isGameFinished = MutableLiveData<Boolean>(false)
    val isGameFinished: LiveData<Boolean>
        get() = _isGameFinished

    private val _isCourseCountActive = MutableLiveData<Boolean>(false)
    val isCourseCountActive: LiveData<Boolean>
        get() = _isCourseCountActive

    private val _isFirstCourseFinished = MutableLiveData<Boolean>(false)
    val isFirstCourseFinished: LiveData<Boolean>
        get() = _isFirstCourseFinished

    private val _isSecondCourseFinished = MutableLiveData<Boolean>(false)
    val isSecondCourseFinished: LiveData<Boolean>
        get() = _isSecondCourseFinished

    private val _isAddGameCourseFinished = MutableLiveData<Boolean>(false)
    val isAddGameCourseFinished: LiveData<Boolean>
        get() = _isAddGameCourseFinished

    fun setGamePlayState(state: Int) {
        _gamePlayState.value = state
    }

    fun setCourseStart() {
        _isCourseStart.value = true
    }

    fun setGameFinished() {
        _isGameFinished.value = true
    }

    fun setCourseCountActive() {
        _isCourseCountActive.value = true
    }

    fun setFirstCourseFinished() {
        _isFirstCourseFinished.value = true
    }

    fun setSecondCourseFinished() {
        _isSecondCourseFinished.value = true
    }

    fun setAddGameCourseFinished() {
        _isAddGameCourseFinished.value = true
    }
}