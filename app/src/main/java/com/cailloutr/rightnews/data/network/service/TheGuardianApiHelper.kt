package com.cailloutr.rightnews.data.network.service

import com.cailloutr.rightnews.data.network.responses.news.NewsRoot
import com.cailloutr.rightnews.data.network.responses.sections.SectionsRoot
import com.cailloutr.rightnews.enums.OrderBy
import retrofit2.Response

interface TheGuardianApiHelper {

    suspend fun getAllSections(): Response<SectionsRoot>

    suspend fun getNewsOrderedByDate(
        orderBy: OrderBy,
        fields: String,
    ): Response<NewsRoot>
}