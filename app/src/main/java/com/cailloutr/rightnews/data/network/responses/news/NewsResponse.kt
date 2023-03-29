package com.cailloutr.rightnews.data.network.responses.news

data class NewsResponse(
    val status: String,
    val userTier: String,
    val total: Long,
    val startIndex: Long,
    val pageSize: Long,
    val currentPage: Long,
    val pages: Long,
    val orderBy: String,
    val results: List<NewsResult>,
)

fun NewsResponse.toNewsList() = results.map { newsResult -> newsResult.toNews() }
