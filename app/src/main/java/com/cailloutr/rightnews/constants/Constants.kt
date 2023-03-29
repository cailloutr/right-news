package com.cailloutr.rightnews.constants

object Constants {
    const val BASE_URL = "https://content.guardianapis.com/"

    const val API_CALL_FIELDS = "trailText,thumbnail,headline"

    val DEFAULT_SECTIONS = listOf(
        "games",
        "sport",
        "tech",
        "books",
        "world-news",
        "politics",
        "culture",
        "education"
    )
}