package com.cailloutr.rightnews.repository

import com.cailloutr.rightnews.constants.Constants.API_CALL_FIELDS
import com.cailloutr.rightnews.data.network.responses.news.NewsRoot
import com.cailloutr.rightnews.data.network.responses.sections.SectionsRoot
import com.cailloutr.rightnews.enums.OrderBy
import com.cailloutr.rightnews.other.Resource
import retrofit2.Response

interface NewsRepositoryInterface {

    suspend fun getAllSections(): Resource<SectionsRoot>

    suspend fun getNewsOrderedByDate(
        orderBy: OrderBy = OrderBy.NEWEST,
        fields: String = API_CALL_FIELDS,
    ): Response<NewsRoot>

}