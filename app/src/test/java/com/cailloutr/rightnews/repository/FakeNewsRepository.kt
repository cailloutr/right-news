package com.cailloutr.rightnews.repository

import com.cailloutr.rightnews.constants.Constants
import com.cailloutr.rightnews.data.network.responses.news.NewsRoot
import com.cailloutr.rightnews.data.network.responses.sections.SectionsRoot
import com.cailloutr.rightnews.enums.OrderBy
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class FakeNewsRepository(
    private var shouldReturnError: Boolean = false,
) : NewsRepositoryInterface {


    override suspend fun getAllSections(): Response<SectionsRoot> {
        return if (shouldReturnError) Response.error(
            404,
            "Error".toResponseBody()
        ) else Constants.fakeResponseSectionRoot
    }

    override suspend fun getNewsBySection(section: String): Response<NewsRoot> {
        return Response.success(
            Constants.fakeNews
        )
    }

    override suspend fun getNewsOrderedByDate(
        orderBy: OrderBy,
        fields: String,
    ): Response<NewsRoot> {
        return Response.success(Constants.fakeNews)
    }
}