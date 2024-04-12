package io.orkk.vietnam.model.language

enum class LanguageType(val code: String, val codeName: String, val encoding: String) {
    LANGUAGE_TYPE_KOR("00", "KOR", "EUC-KR"),
    LANGUAGE_TYPE_JPN("01", "JPN", "SHIFT_JIS"),
    LANGUAGE_TYPE_USA("02", "ENG", "UTF_8")
}