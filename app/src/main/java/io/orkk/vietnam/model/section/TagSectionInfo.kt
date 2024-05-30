package io.orkk.vietnam.model.section

data class TagSectionInfo(
    val courseId: Int,
    val sectionCount: Int,
    val tagSectionList: List<TagSectionItem>
)
