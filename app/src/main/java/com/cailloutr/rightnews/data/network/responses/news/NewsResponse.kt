package com.cailloutr.rightnews.data.network.responses.news

import com.cailloutr.rightnews.model.NewsContainer

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

fun NewsResponse.toNewsContainer(): NewsContainer {
    return NewsContainer(
        total = total,
        startIndex = startIndex,
        pageSize = pageSize,
        currentPage = currentPage,
        pages = pages,
        orderBy = orderBy,
        results = results.map { it.toNews() }
    )
}