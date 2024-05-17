package io.orkk.vietnam.model.game

enum class GamePlayStateType(val code: Int, val description: String) {
    VIEW_STATE_NONE_GAME(0, "None game"),
    VIEW_STATE_WAIT(1, "Wait"),
    VIEW_STATE_HOLE(2, "Hole")
}