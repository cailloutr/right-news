package com.cailloutr.rightnews.data.network.service

import com.cailloutr.rightnews.BuildConfig
import com.cailloutr.rightnews.constants.Constants
import com.cailloutr.rightnews.data.network.responses.news.NewsRoot
import com.cailloutr.rightnews.data.network.responses.sections.SectionsRoot
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TheGuardianApi {

    @GET("/sections?")
    suspend fun getAllSections(
        @Query("api-key") apiKey: String = BuildConfig.API_KEY
    ): Response<SectionsRoot>

    @GET("/search?")
    suspend fun getNewsOfSection(
        @Query("section") section: String,
        @Query("api-key") apiKey: String = BuildConfig.API_KEY,
        @Query("show-fields") showFields: String = Constants.API_CALL_FIELDS
    ): Response<NewsRoot>


    @GET("/search?")
    suspend fun getNewsOrderedByDate(
        @Query("api-key") apiKey: String = BuildConfig.API_KEY,
        @Query("order-by") orderBy: String,
        @Query("show-fields") showFields: String = Constants.API_CALL_FIELDS
    ): Response<NewsRoot>


}