package com.cailloutr.rightnews.data.network.responses.news

import com.cailloutr.rightnews.data.local.roommodel.RoomArticle
import com.cailloutr.rightnews.model.Article

data class NewsResult(
    val id: String,
    val type: String,
    val sectionId: String,
    val sectionName: String,
    val webPublicationDate: String,
    val webTitle: String,
    val webUrl: String,
    val apiUrl: String,
    val fields: NewsFields,
    val isHosted: Boolean,
    val pillarId: String,
    val pillarName: String,
)

fun NewsResult.toArticle() = Article(
    id = id,
    type = type,
    sectionId = sectionId,
    sectionName = sectionName,
    webPublicationDate = webPublicationDate,
    webTitle = webTitle,
    webUrl = webUrl,
    apiUrl = apiUrl,
    isHosted = isHosted,
    pillarId = pillarId,
    pillarName = pillarName,
    trailText = fields.trailText,
    thumbnail = fields.thumbnail,
    headline = fields.headline,
    body = fields.body,
)

fun NewsResult.toRoomArticle(containerId: String) = RoomArticle(
    id = id,
    type = type,
    sectionId = sectionId,
    sectionName = sectionName,
    webPublicationDate = webPublicationDate,
    webTitle = webTitle,
    webUrl = webUrl,
    apiUrl = apiUrl,
    isHosted = isHosted,
    pillarId = pillarId,
    pillarName = pillarName,
    trailText = fields.trailText,
    thumbnail = fields.thumbnail,
    headline = fields.headline,
    body = fields.body,
    containerId = containerId
)