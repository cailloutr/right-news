package com.cailloutr.rightnews.usecases

import com.cailloutr.rightnews.model.Sections
import com.cailloutr.rightnews.other.Resource
import com.cailloutr.rightnews.other.Status
import com.cailloutr.rightnews.repository.FakeNewsRepository
import com.cailloutr.rightnews.repository.NewsRepositoryInterface
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetSectionsUseCaseTest {

    private lateinit var newsRepository: NewsRepositoryInterface
    private lateinit var userCase: GetSectionsUseCase

    @Test
    fun test_whenInvokeCalledAndResponseIsSuccessShouldReturnResourceOfSuccess() = runTest {
        newsRepository = FakeNewsRepository(false)
        userCase = GetSectionsUseCase(newsRepository)

        val result: Resource<Sections> = userCase()

        assertThat(result.status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun test_whenInvokeCalledAndResponseIsErrorShouldReturnResourceOfError() = runTest {
        newsRepository = FakeNewsRepository(true)
        userCase = GetSectionsUseCase(newsRepository)

        val result: Resource<Sections> = userCase()

        assertThat(result.status).isEqualTo(Status.ERROR)
    }
}