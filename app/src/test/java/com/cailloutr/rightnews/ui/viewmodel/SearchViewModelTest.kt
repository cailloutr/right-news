package com.cailloutr.rightnews.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.cailloutr.rightnews.TestCoroutineDispatcher
import com.cailloutr.rightnews.constants.Constants
import com.cailloutr.rightnews.model.NewsContainer
import com.cailloutr.rightnews.other.Resource
import com.cailloutr.rightnews.usecases.NewsUseCases
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    private lateinit var viewModel: SearchViewModel

    @MockK
    private lateinit var useCases: NewsUseCases

    @Before
    fun setup() {
        viewModel = SearchViewModel(
            dispatcher = TestCoroutineDispatcher(),
            newsUseCases = useCases
        )
    }

    @Test
    fun getSearchResultShouldUpdateSearchResultState() = runTest{
        val query = Constants.SEARCH_QUERY
        val response: Resource<NewsContainer> = Resource.success(data = null)

        coEvery {
            useCases.searchNewsUseCase(query)
        } returns (response)

        viewModel.searchResultState.test {
            viewModel.getSearchResult(query)
            assertThat(viewModel.searchResultState.value).isEqualTo(response)
            cancelAndIgnoreRemainingEvents()
        }
    }
}