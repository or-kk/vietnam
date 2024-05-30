package io.orkk.vietnam.model.section

enum class SectionType(val code: Int, val description: String) {
    HOLE_SECTION_NONE(-1, "None"),
    HOLE_SECTION_TEE(0, "Tee ground"),
    HOLE_SECTION_SECOND1(1, "Second 1"),
    HOLE_SECTION_SECOND2(2, "Second 2"),
    HOLE_SECTION_GREEN(3, "Green"),
    HOLE_SECTION_END(4, "End")
}