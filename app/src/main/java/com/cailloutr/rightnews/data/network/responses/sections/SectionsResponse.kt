package com.cailloutr.rightnews.data.network.responses.sections

import com.cailloutr.rightnews.model.Sections

data class SectionsResponse(
    val status: String,
    val userTier: String,
    val total: Long,
    val results: List<SectionsResult>,
)

fun SectionsResponse.toSections(): Sections {
    return Sections(
        total = total,
        listOfSections = results.map { it.toDefaultSection() }
    )
}
