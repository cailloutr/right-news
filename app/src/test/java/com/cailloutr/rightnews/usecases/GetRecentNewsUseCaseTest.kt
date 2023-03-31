package com.cailloutr.rightnews.usecases

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
    fun `when invoke() called with a section string should return only news of that section`() =
        runTest {
            val section = "news"

            val result = userCase(section)

            assertThat(result.data).isNotEmpty()
            result.data?.filter {
                it.sectionId == section
            }?.forEach { news ->
                assertThat(news.sectionId).isEqualTo(section)
            }
        }
}