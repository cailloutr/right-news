package com.cailloutr.rightnews.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.cailloutr.rightnews.TestCoroutineDispatcher
import com.cailloutr.rightnews.constants.Constants
import com.cailloutr.rightnews.data.network.responses.news.toNewsContainer
import com.cailloutr.rightnews.enums.OrderBy
import com.cailloutr.rightnews.model.NewsContainer
import com.cailloutr.rightnews.model.Section
import com.cailloutr.rightnews.model.Sections
import com.cailloutr.rightnews.other.Resource
import com.cailloutr.rightnews.usecases.NewsUseCases
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NewsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    private lateinit var viewModel: NewsViewModel

    @MockK
    private lateinit var newsUseCase: NewsUseCases


    @Before
    fun setUp() {
        viewModel = NewsViewModel(
            TestCoroutineDispatcher(),
            newsUseCase
        )
    }

    @Test
    fun `get sections filtered by id given a list should update sectionsListState with a filtered list`() =
        runTest {
            val listOfSectionsIds = Constants.DEFAULT_SECTIONS

            val response = Resource.success(
                data = Sections(
                    total = listOfSectionsIds.size.toLong(),
                    listOfSections = listOfSectionsIds.map {
                        Section(
                            id = it,
                            title = it,
                            webUrl = it,
                            apiUrl = it,
                            code = it
                        )
                    }
                )
            )

            coEvery {
                newsUseCase.getSectionsUseCase()
            } returns (response)


            viewModel.sectionsListState.test {
                viewModel.getSectionsFilteredById(listOfSectionsIds)
                assertThat(viewModel.sectionsListState.value).isEqualTo(response)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun test_getLatestNewsShouldUpdateLatestNewsState() = runTest {
        val response: Resource<NewsContainer> = Resource.success(
            data = Constants.fakeNews.response.toNewsContainer()
        )

        coEvery {
            newsUseCase.getRecentNewsUseCase(OrderBy.NEWEST, Constants.API_CALL_FIELDS)
        } returns (response)

        viewModel.latestNewsState.test {
            viewModel.getLatestNews(OrderBy.NEWEST, Constants.API_CALL_FIELDS)
            assertThat(viewModel.latestNewsState.value).isEqualTo(response)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun test_getNewsBySectionShouldUpdateSectionNewsList() = runTest {
        val sectionId = "article"
        val response: Resource<NewsContainer> =
            Resource.success(data = Constants.fakeNews.response.toNewsContainer())

        coEvery {
            newsUseCase.getNewsBySectionUseCase(sectionId)
        } returns (response)

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
