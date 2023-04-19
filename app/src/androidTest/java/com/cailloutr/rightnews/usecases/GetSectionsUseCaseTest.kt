package com.cailloutr.rightnews.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.cailloutr.rightnews.TestCoroutineDispatcher
import com.cailloutr.rightnews.constants.Constants
import com.cailloutr.rightnews.data.local.NewsDatabase
import com.cailloutr.rightnews.data.network.service.TheGuardianApiImpl
import com.cailloutr.rightnews.repository.FakeNewsRepository
import com.cailloutr.rightnews.repository.NewsRepositoryInterface
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@SmallTest
class GetSectionsUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val mockKRule = MockKRule(this)

    private lateinit var newsRepository: NewsRepositoryInterface
    private lateinit var userCase: GetSectionsUseCase
    private lateinit var testCoroutineDispatcher: TestCoroutineDispatcher

    @Inject
    @Named("test_db")
    lateinit var database: NewsDatabase

    @MockK
    lateinit var api: TheGuardianApiImpl

    @Before
    fun setup() {
        hiltRule.inject()
        testCoroutineDispatcher = TestCoroutineDispatcher()
        newsRepository = FakeNewsRepository(
            api,
            database
        )
        userCase = GetSectionsUseCase(newsRepository)
    }

    @Test
    fun test_whenInvokeCalledAndShouldReturnAListOfSectionsFromDatabase() = runTest {
        coEvery {
            api.getAllSections()
        } returns (Constants.fakeResponseSectionRoot)

        val result = userCase(testCoroutineDispatcher.io)

        val databaseData = database.sectionDao.getAllSection().first()

        result.test {
            assertThat(awaitItem()).containsExactlyElementsIn(databaseData)
        }
    }
}