package com.cailloutr.rightnews.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.cailloutr.rightnews.TestCoroutineDispatcher
import com.cailloutr.rightnews.constants.Constants
import com.cailloutr.rightnews.data.local.roommodel.RoomSection
import com.cailloutr.rightnews.data.network.responses.news.toNewsContainer
import com.cailloutr.rightnews.data.network.responses.sections.toRoomSections
import com.cailloutr.rightnews.model.SectionWrapper
import com.cailloutr.rightnews.other.Resource
import com.cailloutr.rightnews.usecases.NewsUseCases
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ArticleViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var newsUseCase: NewsUseCases

    private lateinit var viewModel: NewsViewModel

    private lateinit var testDispatcher: TestCoroutineDispatcher


    @Before
    fun setUp() {
        MockKAnnotations.init(this, true)
        testDispatcher = TestCoroutineDispatcher()
        viewModel = NewsViewModel(
            testDispatcher,
            newsUseCase
        )
    }

    @Test
    fun `get sections filtered by id given a list should update sectionsListState`() =
        runTest {
            val listOfSections: List<RoomSection> =
                Constants.fakeResponseSectionRoot.body()?.response?.results?.map {
                    it.toRoomSections()
                }!!

            val response = flow {
                emit(listOfSections)
            }

            coEvery {
                newsUseCase.getSectionsUseCase(testDispatcher.io)
            } returns (response)


            viewModel.sectionsListState.test {
                viewModel.getSectionsFilteredById(Constants.DEFAULT_SECTIONS)
                assertThat(viewModel.sectionsListState.value).containsAnyIn(response.first())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun testGetLatestNews() = runTest {
        // Mock response data
        val section = SectionWrapper("news", "news")
        val expectedNews = Constants.fakeArticle.response.toNewsContainer(section.sectionName)

        // Mock NewsUseCases
        val newsUseCases = mockk<NewsUseCases>()

        coEvery { newsUseCases.getNewsBySectionUseCase(any(), section, any()) } returns flowOf(
            expectedNews
        )

        // Create NewsViewModel with mocked NewsUseCases
        val viewModel = NewsViewModel(testDispatcher, newsUseCases)

        // Call getLatestNews
        viewModel.getLatestNews(section) {}

        // Verify that latestNewsState was updated with the expected news
        assertThat(viewModel.latestNewsState.value).isEqualTo(expectedNews)
    }

    @Test
    fun test_getNewsBySectionShouldUpdateSectionNewsState() = runTest {
        // Arrange
        val sectionName = "sports"
        val newsContainer = Constants.fakeArticle.response.toNewsContainer(sectionName)
        val responseStatus = mockk<Resource<Exception>>()
        val newsUseCase = mockk<NewsUseCases>()

        coEvery { newsUseCase.getNewsBySectionUseCase(any(), any(), captureLambda()) } returns flowOf(newsContainer)

        val viewModel = NewsViewModel(
            testDispatcher,
            newsUseCase
        )

        // Act
        viewModel.setSelectedSections(sectionName)
        viewModel.getNewsBySection { }

        assertEquals(newsContainer, viewModel.sectionsNewsState.value)
    }

/*    @Test
    fun test_getNewsBySectionShouldUpdateSectionNewsList() = runTest {
        val section = SectionWrapper("article", "article")
        val expectedNews = Constants.fakeArticle.response.toNewsContainer(section.sectionName)

        // Mock NewsUseCases
        val newsUseCases = mockk<NewsUseCases>()

        coEvery {
            newsUseCase.getNewsBySectionUseCase(
                any(),
                section,
                any()
            )
        } returns flowOf(expectedNews)

        // Create NewsViewModel with mocked NewsUseCases
        val viewModel = NewsViewModel(testDispatcher, newsUseCases)

        viewModel.setSelectedSections(section.value)

        viewModel.getNewsBySection { }

        assertThat(viewModel.sectionsNewsState.value).isEqualTo(expectedNews)

    }*/

    @Test
    fun test_setSelectedSectionsShouldUpdateUiStateSelectedSections() {
        val sectionId = "article"

        viewModel.setSelectedSections(sectionId)

        assertThat(viewModel.selectedSectionsState.value).isEqualTo(sectionId)
    }
}
