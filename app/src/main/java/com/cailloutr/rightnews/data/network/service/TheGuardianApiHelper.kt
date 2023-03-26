package com.cailloutr.rightnews.data.network.service

import com.cailloutr.rightnews.data.network.responses.SectionsRoot
import retrofit2.Response

interface TheGuardianApiHelper {

    suspend fun getAllSections(): Response<SectionsRoot>
}