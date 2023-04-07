package com.cailloutr.rightnews.usecases

import com.cailloutr.rightnews.constants.Constants
import com.cailloutr.rightnews.data.network.responses.news.toNewsContainer
import com.cailloutr.rightnews.other.Status
import com.cailloutr.rightnews.repository.FakeNewsRepository
import com.cailloutr.rightnews.repository.NewsRepositoryInterface
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetRecentNewsUseCaseTest {

    private lateinit var newsRepository: NewsRepositoryInterface
    private lateinit var userCase: GetNewsBySectionUseCase

    @Before
    fun setup() {
        newsRepository = FakeNewsRepository()
        userCase = GetNewsBySectionUseCase(newsRepository)
    }

    @Test
    fun `when invoke() called should return a Resource of NewsContainer`() =
        runTest {
            val section = "news"

            val result = userCase(section)

            assertThat(result.status).isEqualTo(Status.SUCCESS)
            assertThat(result.data).isEqualTo(Constants.fakeNews.response.toNewsContainer())
        }
}