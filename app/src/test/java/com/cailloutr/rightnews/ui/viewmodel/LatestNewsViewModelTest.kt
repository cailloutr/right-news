package com.cailloutr.rightnews.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cailloutr.rightnews.TestCoroutineDispatcher
import com.cailloutr.rightnews.constants.Constants
import com.cailloutr.rightnews.constants.Constants.LATEST_NEWS
import com.cailloutr.rightnews.data.network.responses.news.toNewsContainer
import com.cailloutr.rightnews.model.SectionWrapper
import com.cailloutr.rightnews.usecases.NewsUseCases
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
    private lateinit var newsUseCases: NewsUseCases

    private lateinit var testDispatcher: TestCoroutineDispatcher

    @Before
    fun setUp() {
        testDispatcher = TestCoroutineDispatcher()
        viewModel = LatestNewsViewModel(
            testDispatcher,
            newsUseCases
        )
    }

    @Test
    fun `getLatestNews should update latestNewsState`() = runTest {
        // Arrange
        val expectedNewsContainer = Constants.fakeArticle.response.toNewsContainer("")

        coEvery {
            newsUseCases.getNewsBySectionUseCase(
                testDispatcher.io,
                SectionWrapper(LATEST_NEWS, ""),
                any()
            )
        } returns flowOf(expectedNewsContainer)

        // Act
        viewModel.getLatestNews { }

        assertThat(viewModel.latestNewsState.value).isEqualTo(expectedNewsContainer)


        coVerify {
            newsUseCases.getNewsBySectionUseCase(
                testDispatcher.io,
                SectionWrapper(LATEST_NEWS, ""),
                any()
            )
        }
    }

    @Test
    fun `getNewsOfSection should update latestNewsState`() = runTest {
        // Arrange
        val section = SectionWrapper("politics", "Politics")
        val expectedNewsContainer = Constants.fakeArticle.response.toNewsContainer(section.sectionName)

        coEvery {
            newsUseCases.getNewsBySectionUseCase(
                testDispatcher.io,
                section,
                any()
            )
        } returns flowOf(expectedNewsContainer)

        // Act
        viewModel.getNewsOfSection(section) { }

        assertThat(viewModel.latestNewsState.value).isEqualTo(expectedNewsContainer)
    }
}