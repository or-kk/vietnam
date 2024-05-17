package io.orkk.vietnam.model.game

enum class GameProgressType(val progressCourseCount: Int, val description: String) {
    ORDINARY_GAME(2, "Ordinary game type count"),
    ADDED_GAME(3, "Add game")
}