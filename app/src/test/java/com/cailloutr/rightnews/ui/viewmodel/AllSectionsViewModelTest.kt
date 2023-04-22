package com.cailloutr.rightnews.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.cailloutr.rightnews.TestCoroutineDispatcher
import com.cailloutr.rightnews.constants.Constants
import com.cailloutr.rightnews.data.local.roommodel.RoomSection
import com.cailloutr.rightnews.data.network.responses.sections.toRoomSections
import com.cailloutr.rightnews.usecases.NewsUseCases
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AllSectionsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    private lateinit var viewModel: AllSectionsViewModel

    @MockK
    lateinit var newsUseCase: NewsUseCases

    private lateinit var dispatchers: TestCoroutineDispatcher

    @Before
    fun setUp() {
        MockKAnnotations.init(this, true)
        dispatchers = TestCoroutineDispatcher()
        viewModel = AllSectionsViewModel(
            dispatchers = dispatchers,
            newsUseCases = newsUseCase
        )
    }

    @Test
    fun test_getAllSections() = runTest {
        val list: List<RoomSection> = Constants.fakeResponseSectionRoot.body()?.response?.results?.map { it.toRoomSections() }!!

        val response: Flow<List<RoomSection>> = flow {
            emit(list)
        }

        coEvery {
            newsUseCase.getSectionsUseCase(dispatchers.io)
        } returns (response)

        viewModel.allSectionsState.test {
            viewModel.getAllSections()
            assertThat(viewModel.allSectionsState.value).isEqualTo(response.first())
            cancelAndIgnoreRemainingEvents()
        }

    }
}