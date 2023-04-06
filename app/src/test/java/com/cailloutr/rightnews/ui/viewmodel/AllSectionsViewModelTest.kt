package com.cailloutr.rightnews.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.cailloutr.rightnews.TestCoroutineDispatcher
import com.cailloutr.rightnews.model.Sections
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
class AllSectionsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    private lateinit var viewModel: AllSectionsViewModel

    @MockK
    private lateinit var newsUseCase: NewsUseCases

    @Before
    fun setUp() {
        viewModel = AllSectionsViewModel(
            dispatchers = TestCoroutineDispatcher(),
            newsUseCases = newsUseCase
        )
    }

    @Test
    fun test_getAllSections() = runTest {

        val response: Resource<Sections> = Resource.success(
            data = null
        )

        coEvery {
            newsUseCase.getSectionsUseCase()
        } returns (response)


        viewModel.sectionsListState.test {
            viewModel.getAllSections()
            assertThat(viewModel.sectionsListState.value).isEqualTo(response)
            cancelAndIgnoreRemainingEvents()
        }

    }
}