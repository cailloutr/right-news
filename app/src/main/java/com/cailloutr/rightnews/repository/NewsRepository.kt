package com.cailloutr.rightnews.repository

import com.cailloutr.rightnews.data.network.responses.news.NewsRoot
import com.cailloutr.rightnews.data.network.responses.sections.SectionsRoot
import com.cailloutr.rightnews.data.network.service.TheGuardianApiHelper
import com.cailloutr.rightnews.enums.OrderBy
import retrofit2.Response
import javax.inject.Inject

//private const val TAG = "NewsRepository"

class NewsRepository @Inject constructor(
    private val theGuardianApi: TheGuardianApiHelper,
) : NewsRepositoryInterface {

    override suspend fun getAllSections(): Response<SectionsRoot> =
        theGuardianApi.getAllSections()

    override suspend fun getNewsBySection(section: String): Response<NewsRoot> =
        theGuardianApi.getNewsOfSection(section)


    override suspend fun getNewsOrderedByDate(
        orderBy: OrderBy,
        fields: String,
    ): Response<NewsRoot> =
        theGuardianApi.getNewsOrderedByDate(
            orderBy = orderBy,
            fields = fields
        )

    override suspend fun searchNews(
        searchQuery: String,
        orderBy: OrderBy,
        fields: String
    ): Response<NewsRoot> =
        theGuardianApi.searchNews(
            searchQuery = searchQuery,
            orderBy = orderBy,
            fields = fields
        )

}
