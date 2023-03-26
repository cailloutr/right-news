package com.cailloutr.rightnews.repository

import com.cailloutr.rightnews.data.network.responses.SectionsRoot
import com.cailloutr.rightnews.data.network.service.TheGuardianApiHelper
import com.cailloutr.rightnews.other.Resource
import com.cailloutr.rightnews.other.Status
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class NewsRepositoryTest {

    @MockK
    private lateinit var theGuardianApi: TheGuardianApiHelper

    private lateinit var repository: NewsRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this, true)
        repository = NewsRepository(theGuardianApi)
    }

    @Test
    fun `when api result is success then return a Resource of success`() = runTest {
        coEvery {
            theGuardianApi.getAllSections()
        }.returns(Response.success(null))

        val result: Resource<SectionsRoot> = repository.getAllSections()

        assertThat(result.status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun `when api result is error then return a Resource of error`() = runTest{
        val error = "Error"
        val response = Response.error<SectionsRoot>(
            404,
            error.toResponseBody()
        )
        coEvery { theGuardianApi.getAllSections() }.returns(
            response
        )

        val result: Resource<SectionsRoot> = repository.getAllSections()

        assertThat(result.status).isEqualTo(Status.ERROR)
        assertThat(result.message).isEqualTo(response.message())
    }
}