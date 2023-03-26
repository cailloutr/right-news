package com.cailloutr.rightnews.usecases

import com.cailloutr.rightnews.model.Section
import com.cailloutr.rightnews.repository.FakeNewsRepository
import com.cailloutr.rightnews.repository.NewsRepositoryInterface
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetSectionsFilteredByIdUseCaseTest {

    private lateinit var newsRepository: NewsRepositoryInterface
    private lateinit var userCase: GetSectionsFilteredByIdUseCase

    @Before
    fun setUp() {
        newsRepository = FakeNewsRepository()
        userCase = GetSectionsFilteredByIdUseCase(newsRepository)
    }


    @Test
    fun `when invoke() called with a list not empty should return sections that match`() = runTest {
        val sections = listOf("about", "music")

        val filteredSections: List<Section> = userCase(sections)

        filteredSections.forEach {
            assertThat(sections).contains(it.id)
        }
    }

    @Test
    fun `when invoke() called with a empty list should return all sections`() = runTest {
        val sections = listOf<String>()

        val allSections: Long? = newsRepository.getAllSections().data?.response?.total

        val filteredSections: List<Section> = userCase(sections)

        assertThat(filteredSections.size).isEqualTo(allSections)
    }
}