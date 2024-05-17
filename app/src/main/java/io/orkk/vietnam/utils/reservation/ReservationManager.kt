package io.orkk.vietnam.utils.reservation

import Reservation
import io.orkk.vietnam.model.course.CourseType
import io.orkk.vietnam.model.game.GameProgressType
import io.orkk.vietnam.model.reservation.ReservationGameType

class ReservationManager {
    val isRequestAddGameOk = false

    fun getGameProgressCourseCount(): Int {
        return if (isRequestAddGameOk || Reservation().course[ReservationGameType.INDEX_COURSE_FIRST.code] != CourseType.COURSE_NONE.code) {
            GameProgressType.ADDED_GAME.progressCourseCount
        } else {
            GameProgressType.ORDINARY_GAME.progressCourseCount
        }
    }

    fun isAddedGame(): Boolean {
        return isRequestAddGameOk || Reservation().course[ReservationGameType.INDEX_COURSE_FIRST.code] != CourseType.COURSE_NONE.code
    }
}