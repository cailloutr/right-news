package com.cailloutr.rightnews.data.network.service

import com.cailloutr.rightnews.data.network.responses.SectionsRoot
import retrofit2.Response
import javax.inject.Inject

class TheGuardianApiImpl @Inject constructor(
    private val theGuardianApi: TheGuardianApi,
) : TheGuardianApiHelper {

    override suspend fun getAllSections(): Response<SectionsRoot> {
        return theGuardianApi.getAllSections()
    }

}