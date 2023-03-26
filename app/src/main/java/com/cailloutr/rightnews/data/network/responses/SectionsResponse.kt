package com.cailloutr.rightnews.data.network.responses

data class  SectionsResponse(
    val status: String,
    val userTier: String,
    val total: Long,
    val results: List<SectionsResult>
)
