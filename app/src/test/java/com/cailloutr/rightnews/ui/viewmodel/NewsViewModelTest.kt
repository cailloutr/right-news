package com.cailloutr.rightnews.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.cailloutr.rightnews.TestCoroutineDispatcher
import com.cailloutr.rightnews.constants.Constants
import com.cailloutr.rightnews.model.Section
import com.cailloutr.rightnews.usecases.NewsUseCases
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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
    private lateinit var sectionsState: MutableStateFlow<List<Section>>


    @Before
    fun setUp() {
        sectionsState = MutableStateFlow(listOf())

        viewModel = NewsViewModel(
            TestCoroutineDispatcher(),
            newsUseCase
        )
    }

    @Test
    fun `get sections filtered by id given a list should update sectionsState`() =
        runTest {
            val listOfSectionsIds = Constants.DEFAULT_SECTIONS

            val listOfSections = listOfSectionsIds.map {
                Section(
                    id = it,
                    title = it,
                    webUrl = it,
                    apiUrl = it,
                    code = it
                )
            }

            coEvery {
                newsUseCase.getSectionsFilteredByIdUseCase(listOfSectionsIds)
            } returns (listOfSections)


            viewModel.sectionsState.test {
                viewModel.getSectionsFilteredById(listOfSectionsIds)
                val result = awaitItem()
                assertThat(result).isEqualTo(listOfSections)
            }
        }

}