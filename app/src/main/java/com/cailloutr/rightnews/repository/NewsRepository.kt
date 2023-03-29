package com.cailloutr.rightnews.repository

import com.cailloutr.rightnews.data.network.responses.news.NewsRoot
import com.cailloutr.rightnews.data.network.responses.sections.SectionsRoot
import com.cailloutr.rightnews.data.network.service.TheGuardianApiHelper
import com.cailloutr.rightnews.enums.OrderBy
import com.cailloutr.rightnews.other.Resource
import retrofit2.Response
import javax.inject.Inject

//private const val TAG = "NewsRepository"

class NewsRepository @Inject constructor(
    private val theGuardianApi: TheGuardianApiHelper,
) : NewsRepositoryInterface {

    override suspend fun getAllSections(): Resource<SectionsRoot> {
        try {
            val response = theGuardianApi.getAllSections()
            if (response.isSuccessful) {
                return Resource.success(data = response.body())
            }
            return Resource.error(msg = response.message(), data = null)
        } catch (e: java.lang.Exception) {
            return Resource.error(msg = e.message.toString(), data = null)
        }
    }

    override suspend fun getNewsOrderedByDate(
        orderBy: OrderBy,
        fields: String,
    ): Response<NewsRoot> =
        theGuardianApi.getNewsOrderedByDate(
            orderBy,
            fields
        )

}
