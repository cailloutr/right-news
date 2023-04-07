package com.cailloutr.rightnews.usecases

import com.cailloutr.rightnews.repository.FakeNewsRepository
import com.cailloutr.rightnews.repository.NewsRepositoryInterface
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetNewsBySectionUseCaseTest {

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

            Truth.assertThat(result.data?.results).isNotEmpty()
            result.data?.results?.filter {
                it.sectionId == section
            }?.forEach { news ->
                Truth.assertThat(news.sectionId).isEqualTo(section)
            }
        }
}