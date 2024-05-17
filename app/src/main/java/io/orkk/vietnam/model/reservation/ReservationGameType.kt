package io.orkk.vietnam.model.reservation

enum class ReservationGameType(val code: Int, val description: String) {
    INDEX_COURSE_FIRST(0, "First game"),
    INDEX_COURSE_SECOND(1, "Second game"),
    INDEX_COURSE_THIRD(2, "Add game")
}