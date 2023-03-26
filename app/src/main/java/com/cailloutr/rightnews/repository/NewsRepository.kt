package com.cailloutr.rightnews.repository

import com.cailloutr.rightnews.data.network.responses.SectionsRoot
import com.cailloutr.rightnews.data.network.service.TheGuardianApiHelper
import com.cailloutr.rightnews.other.Resource
import javax.inject.Inject

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
}