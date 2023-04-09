package com.cailloutr.rightnews.usecases

import com.cailloutr.rightnews.constants.Constants
import com.cailloutr.rightnews.repository.FakeNewsRepository
import com.cailloutr.rightnews.repository.NewsRepositoryInterface
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchNewsUseCaseTest {

    private lateinit var repository: NewsRepositoryInterface
    private lateinit var useCase: SearchNewsUseCase

    @Before
    fun setUp() {
        repository = FakeNewsRepository()
        useCase = SearchNewsUseCase(repository)
    }

    @Test
    fun `when invoke called should return a news witch title contains the query content`() =
        runTest {
            val searchQuery = Constants.SEARCH_QUERY

            val response = useCase.invoke(searchQuery)

            response.data?.results?.forEach {
                assertThat(it.webTitle).ignoringCase().contains(searchQuery)
            }
        }
}