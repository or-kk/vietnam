package io.orkk.vietnam.model.course

enum class CourseType(val code: Int, val description: String) {
    COURSE_NONE(0, "Wait area or none"),
    COURSE_FIRST(1, "First course"),
    COURSE_SECOND(2, "Second course"),
    COURSE_THIRD(3, "Third course"),
    COURSE_FOURTH(4, "Fourth course")
}