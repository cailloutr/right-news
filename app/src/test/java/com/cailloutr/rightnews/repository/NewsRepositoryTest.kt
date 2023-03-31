package com.cailloutr.rightnews.repository

import com.cailloutr.rightnews.constants.Constants
import com.cailloutr.rightnews.data.network.responses.news.NewsRoot
import com.cailloutr.rightnews.data.network.responses.sections.SectionsRoot
import com.cailloutr.rightnews.data.network.service.TheGuardianApiHelper
import com.cailloutr.rightnews.enums.OrderBy
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class NewsRepositoryTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var theGuardianApi: TheGuardianApiHelper

    private lateinit var repository: NewsRepository

    @Before
    fun setUp() {
        repository = NewsRepository(theGuardianApi)
    }

    @Test
    fun `get All Sections when api result is success then return a Response of success`() =
        runTest {
            coEvery {
                theGuardianApi.getAllSections()
            }.returns(Response.success(null))

            val result = repository.getAllSections()

            assertThat(result.isSuccessful).isTrue()
        }

    @Test
    fun `get All Sections when api result is error then return a Resource of error`() = runTest {
        val error = "Error"
        val response = Response.error<SectionsRoot>(
            404,
            error.toResponseBody()
        )
        coEvery { theGuardianApi.getAllSections() }.returns(
            response
        )

        val result = repository.getAllSections()

        assertThat(result.isSuccessful).isFalse()
        assertThat(result.message()).isEqualTo(response.message())
    }

    @Test
    fun `get news ordered by date when response is error should return a Resource error`() = runTest {
        val error = "Error"
        val response = Response.error<NewsRoot>(
            404,
            error.toResponseBody()
        )
        coEvery { theGuardianApi.getNewsOrderedByDate(
            OrderBy.NEWEST,
            Constants.API_CALL_FIELDS
        ) }.returns(
            response
        )

        val result: Response<NewsRoot> = repository.getNewsOrderedByDate(
            OrderBy.NEWEST,
            Constants.API_CALL_FIELDS
        )

        assertThat(result).isEqualTo(response)
    }

    @Test
    fun `get news of sections when response is error should return a Resource error`() = runTest {
        val error = "Error"
        val response = Response.error<NewsRoot>(
            404,
            error.toResponseBody()
        )

        coEvery { theGuardianApi.getNewsOfSection("section") }.returns(
            response
        )

        val result = repository.getNewsBySection("section")

        assertThat(result.isSuccessful).isFalse()
    }

    @Test
    fun `get news of sections when response is success should return a Resource seccess`() = runTest {
        coEvery { theGuardianApi.getNewsOfSection("section") }.returns(
            Response.success(null)
        )

        val result = repository.getNewsBySection("section")

        assertThat(result.isSuccessful).isTrue()
    }
}