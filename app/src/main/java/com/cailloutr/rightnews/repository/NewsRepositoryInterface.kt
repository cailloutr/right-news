package com.cailloutr.rightnews.repository

import com.cailloutr.rightnews.data.network.responses.SectionsRoot
import com.cailloutr.rightnews.other.Resource

interface NewsRepositoryInterface {

    suspend fun getAllSections(): Resource<SectionsRoot>
}