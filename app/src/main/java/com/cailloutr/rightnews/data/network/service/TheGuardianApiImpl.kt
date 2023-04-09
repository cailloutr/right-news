package com.cailloutr.rightnews.data.network.service

import com.cailloutr.rightnews.data.network.responses.news.NewsRoot
import com.cailloutr.rightnews.data.network.responses.sections.SectionsRoot
import com.cailloutr.rightnews.enums.OrderBy
import retrofit2.Response
import javax.inject.Inject

class TheGuardianApiImpl @Inject constructor(
    private val theGuardianApi: TheGuardianApi,
) : TheGuardianApiHelper {

    override suspend fun getAllSections(): Response<SectionsRoot> {
        return theGuardianApi.getAllSections()
    }

    override suspend fun getNewsOfSection(section: String): Response<NewsRoot> {
        return theGuardianApi.getNewsOfSection(section)
    }

    override suspend fun getNewsOrderedByDate(
        orderBy: OrderBy,
        fields: String,
        page: Int
    ): Response<NewsRoot> {
        return theGuardianApi.getNewsOrderedByDate(
            orderBy = orderBy.value,
            showFields = fields,
            page = page
        )
    }

    override suspend fun searchNews(
        orderBy: OrderBy,
        fields: String,
        searchQuery: String
    ): Response<NewsRoot> {
        return theGuardianApi.searchNews(
            orderBy = orderBy.value,
            showFields = fields,
            searchQuery = searchQuery
        )
    }

}