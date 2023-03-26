package com.cailloutr.rightnews.enums

enum class Code(val value: String) {
    Au("au"),
    Default("default"),
    Uk("uk"),
    Us("us");

    companion object {
        public fun fromValue(value: String): Code = when (value) {
            "au"      -> Au
            "default" -> Default
            "uk"      -> Uk
            "us"      -> Us
            else      -> throw IllegalArgumentException()
        }
    }
}