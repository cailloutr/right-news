package com.cailloutr.rightnews.repository

import com.cailloutr.rightnews.constants.Constants.API_CALL_FIELDS
import com.cailloutr.rightnews.constants.Constants.INITIAL_PAGE
import com.cailloutr.rightnews.data.network.responses.news.NewsRoot
import com.cailloutr.rightnews.data.network.responses.sections.SectionsRoot
import com.cailloutr.rightnews.enums.OrderBy
import retrofit2.Response

interface NewsRepositoryInterface {

    suspend fun getAllSections(): Response<SectionsRoot>
    suspend fun getNewsBySection(
        section: String
    ): Response<NewsRoot>

    suspend fun getNewsOrderedByDate(
        orderBy: OrderBy = OrderBy.NEWEST,
        fields: String = API_CALL_FIELDS,
        page: Int = INITIAL_PAGE
    ): Response<NewsRoot>

    suspend fun searchNews(
        searchQuery: String,
        orderBy: OrderBy = OrderBy.NEWEST,
        fields: String = API_CALL_FIELDS
    ): Response<NewsRoot>


}