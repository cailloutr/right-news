package com.cailloutr.rightnews.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.cailloutr.rightnews.TestCoroutineDispatcher
import com.cailloutr.rightnews.constants.Constants
import com.cailloutr.rightnews.constants.Constants.LATEST_NEWS
import com.cailloutr.rightnews.data.network.responses.news.toNewsContainer
import com.cailloutr.rightnews.data.network.responses.sections.toRoomSections
import com.cailloutr.rightnews.model.NewsContainer
import com.cailloutr.rightnews.model.SectionWrapper
import com.cailloutr.rightnews.other.Resource
import com.cailloutr.rightnews.usecases.NewsUseCases
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ArticleViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    private lateinit var viewModel: NewsViewModel

    @MockK
    private lateinit var newsUseCase: NewsUseCases

    private lateinit var testDispatcher: TestCoroutineDispatcher


    @Before
    fun setUp() {
        testDispatcher = TestCoroutineDispatcher()
        viewModel = NewsViewModel(
            testDispatcher,
            newsUseCase
        )
    }

    @Test
    fun `get sections filtered by id given a list should update sectionsListState with a filtered list`() =
        runTest {
            val listOfSections = Constants.fakeResponseSectionRoot.body()?.response?.results?.map {
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
                assertThat(viewModel.sectionsListState.value).isEqualTo(response)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun test_getLatestNewsShouldUpdateLatestNewsState() {
        val sectionWrapper = SectionWrapper(LATEST_NEWS, "")
        runTest {
            val response: Resource<NewsContainer> = Resource.success(
                data = Constants.fakeArticle.response.toNewsContainer(sectionWrapper.sectionName)
            )

    //        coEvery {
    //            newsUseCase.getRecentNewsUseCase()
    //        } returns (response)

            viewModel.latestNewsState.test {
                viewModel.getLatestNews(sectionWrapper)
                assertThat(viewModel.latestNewsState.value).isEqualTo(response)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun test_getNewsBySectionShouldUpdateSectionNewsList() = runTest {
        val sectionId = "article"
        val response: Resource<NewsContainer> =
            Resource.success(data = Constants.fakeArticle.response.toNewsContainer(sectionId))

/*        coEvery {
            newsUseCase.getNewsBySectionUseCase(
                testDispatcher.io,
                SectionWrapper(sectionId, sectionId)
            )
        } returns (response)*/

        viewModel.sectionsNewsState.test {
            viewModel.setSelectedSections(sectionId)
            viewModel.getNewsBySection()
            assertThat(viewModel.sectionsNewsState.value).isEqualTo(response)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun test_setSelectedSectionsShouldUpdateUiStateSelectedSections() {
        val sectionId = "article"

        viewModel.setSelectedSections(sectionId)

        assertThat(viewModel.selectedSectionsState.value).isEqualTo(sectionId)
    }
}
