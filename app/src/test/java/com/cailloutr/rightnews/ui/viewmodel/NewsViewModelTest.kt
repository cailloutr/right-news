package com.cailloutr.rightnews.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.cailloutr.rightnews.TestCoroutineDispatcher
import com.cailloutr.rightnews.constants.Constants
import com.cailloutr.rightnews.constants.Constants.DEFAULT_SECTIONS
import com.cailloutr.rightnews.constants.Constants.FIRST_SECTIONS_ID
import com.cailloutr.rightnews.constants.Constants.LATEST_NEWS
import com.cailloutr.rightnews.data.local.roommodel.RoomSection
import com.cailloutr.rightnews.data.network.responses.sections.toRoomSections
import com.cailloutr.rightnews.model.Article
import com.cailloutr.rightnews.model.NewsContainer
import com.cailloutr.rightnews.model.SectionWrapper
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
class NewsViewModelTest {

    @get:Rule
    val mockKRule = MockKRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var testDispatcher: TestCoroutineDispatcher
    private lateinit var viewModel: NewsViewModel

    @MockK
    lateinit var useCases: NewsUseCases

    @Before
    fun setUp() {
        testDispatcher = TestCoroutineDispatcher()
        viewModel = NewsViewModel(
            testDispatcher,
            useCases
        )
    }

    @Test
    fun test_setSelectedSectionsShouldUpdateSelectedSectionState() = runTest {
        val section = "games"

        viewModel.selectedSectionsState.test {
            viewModel.setSelectedSections(section)
            assertThat(viewModel.selectedSectionsState.value).isEqualTo(section)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun test_getSectionsFilteredById() = runTest {
        val sections = DEFAULT_SECTIONS
        val response = flow<List<RoomSection>> {
            emit(Constants.fakeResponseSectionRoot.body()?.response?.results?.map { it.toRoomSections() }!!)
        }

        coEvery { useCases.getSectionsUseCase(testDispatcher.io) } returns (response)

        viewModel.sectionsListState.test {
            viewModel.getSectionsFilteredById(sections)
            viewModel.sectionsListState.value.forEach {
                assertThat(sections).contains(it.id)
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun test_getLatestNewsShouldUpdateLatestNewsState() = runTest {
        val section = SectionWrapper(LATEST_NEWS, "")
        val response = flow {
            emit(
                NewsContainer(
                    id = section.sectionName,
                    total = 100,
                    startIndex = 1,
                    pages = 10,
                    currentPage = 1,
                    pageSize = 10,
                    orderBy = "newest",
                    results = listOf<Article>()
                )
            )
        }

        coEvery {
            useCases.getNewsBySectionUseCase(
                testDispatcher.io,
                section
            )
        } returns (response)

        viewModel.latestNewsState.test {
            viewModel.getLatestNews(section)
            assertThat(viewModel.latestNewsState.value?.id!!).isEqualTo(section.sectionName)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun test_getNewsBySectionShouldUpdateSectionsNewsState() = runTest {
        val selectedSection = SectionWrapper(
            sectionName = FIRST_SECTIONS_ID,
            value = FIRST_SECTIONS_ID
        )
        val response = flow {
            emit(
                NewsContainer(
                    id = selectedSection.sectionName,
                    total = 100,
                    startIndex = 1,
                    pages = 10,
                    currentPage = 1,
                    pageSize = 10,
                    orderBy = "newest",
                    results = listOf<Article>()
                )
            )
        }

        coEvery {
            useCases.getNewsBySectionUseCase(
                testDispatcher.io,
                selectedSection
            )
        } returns (response)

        viewModel.sectionsNewsState.test {
            viewModel.setSelectedSections(FIRST_SECTIONS_ID)
            viewModel.getNewsBySection()
            assertThat(viewModel.sectionsNewsState.value?.id).isEqualTo(viewModel.selectedSectionsState.value)
            cancelAndIgnoreRemainingEvents()
        }
    }
}