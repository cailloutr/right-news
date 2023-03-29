package com.cailloutr.rightnews.data.network.responses.sections

data class  SectionsResponse(
    val status: String,
    val userTier: String,
    val total: Long,
    val results: List<SectionsResult>
)
