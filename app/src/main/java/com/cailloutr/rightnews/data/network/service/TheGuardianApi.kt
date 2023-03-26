package com.cailloutr.rightnews.data.network.service

import com.cailloutr.rightnews.BuildConfig
import com.cailloutr.rightnews.data.network.responses.SectionsRoot
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TheGuardianApi {

    @GET("/sections?")
    suspend fun getAllSections(
        @Query("api-key") apiKey: String = BuildConfig.API_KEY
    ): Response<SectionsRoot>

}