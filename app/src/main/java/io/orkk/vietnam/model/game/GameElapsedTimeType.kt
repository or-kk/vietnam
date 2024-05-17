package io.orkk.vietnam.model.game

enum class GameElapsedTimeType(val code: Int, val description: String) {
    FIRST_HALF_START_TIME(1, "First half start time"),
    FIRST_HALF_END_TIME(2, "First half end time"),
    SECOND_HALF_START_TIME(3, "Second half start time"),
    SECOND_HALF_END_TIME(4, "Second half end time"),
    ADD_GAME_START_TIME(5, "Add game start time"),
    ADD_GAME_END_TIME(6, "Add game end time")
}