package com.cailloutr.rightnews.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.cailloutr.rightnews.TestCoroutineDispatcher
import com.cailloutr.rightnews.constants.Constants.API_CALL_FIELDS
import com.cailloutr.rightnews.enums.OrderBy
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

@ExperimentalCoroutinesApi
class LatestNewsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    private lateinit var viewModel: LatestNewsViewModel

    @MockK
    private lateinit var useCases: NewsUseCases

    @Before
    fun setUp() {
        viewModel = LatestNewsViewModel(
            TestCoroutineDispatcher(),
            useCases
        )
    }


    @Test
    fun getLatestNewsShouldUpdateViewModelLatestNewsState() = runTest {
        val response: Resource<NewsContainer> = Resource.success( data = null)

        coEvery {
            useCases.getRecentNewsUseCase(OrderBy.NEWEST, API_CALL_FIELDS)
        } returns (response)

        viewModel.latestNewsState.test {
            viewModel.getLatestNews(OrderBy.NEWEST, API_CALL_FIELDS)
            assertThat(viewModel.latestNewsState.value).isEqualTo(response)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getNewsOfSectionShouldUpdateViewModelLatestNewsState() = runTest{
        val section = "about"
        val response: Resource<NewsContainer> = Resource.success( data = null)

        coEvery {
            useCases.getNewsBySectionUseCase(section)
        } returns (response)

        viewModel.latestNewsState.test {
            viewModel.getNewsOfSection(section)
            assertThat(viewModel.latestNewsState.value).isEqualTo(response)
            cancelAndIgnoreRemainingEvents()
        }
    }
}