package com.cailloutr.rightnews.data.network.service

import com.cailloutr.rightnews.data.network.responses.news.NewsRoot
import com.cailloutr.rightnews.data.network.responses.sections.SectionsRoot
import com.cailloutr.rightnews.enums.OrderBy
import retrofit2.Response

interface TheGuardianApiHelper {

    suspend fun getAllSections(): Response<SectionsRoot>

    suspend fun getNewsOfSection(
        section: String
    ): Response<NewsRoot>


    suspend fun getNewsOrderedByDate(
        orderBy: OrderBy,
        fields: String,
        page: Int
    ): Response<NewsRoot>

    suspend fun searchNews(
        orderBy: OrderBy,
        fields: String,
        searchQuery: String
    ): Response<NewsRoot>
}