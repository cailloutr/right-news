package com.cailloutr.rightnews.data.network.responses.news

import com.cailloutr.rightnews.data.local.roommodel.RoomNewsContainer
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

fun NewsResponse.toNewsList() = results.map { newsResult -> newsResult.toArticle() }

fun NewsResponse.toNewsContainer(id: String): NewsContainer {
    return NewsContainer(
        id = id,
        total = total,
        startIndex = startIndex,
        pageSize = pageSize,
        currentPage = currentPage,
        pages = pages,
        orderBy = orderBy,
        results = results.map { it.toArticle() }
    )
}

fun NewsResponse.toRoomNewsContainer(section: String): RoomNewsContainer {
    return RoomNewsContainer(
        total = total,
        startIndex = startIndex,
        pageSize = pageSize,
        currentPage = currentPage,
        pages = pages,
        orderBy = orderBy,
        id = section
    )
}