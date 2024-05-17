package io.orkk.vietnam.utils.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class GamePlayManager {

    private val _gamePlayState = MutableLiveData<Int>()
    val gamePlayState: LiveData<Int>
        get() = _gamePlayState

    fun setGamePlayState(state: Int) {
        _gamePlayState.value = state
    }

    private val _isEndGame = MutableLiveData<Boolean>(false)
    val isEndGame: LiveData<Boolean>
        get() = _isEndGame

    fun setEndGame() {
        _isEndGame.value = true
    }
}