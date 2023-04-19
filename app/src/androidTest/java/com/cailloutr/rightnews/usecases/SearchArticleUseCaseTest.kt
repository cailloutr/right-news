package com.cailloutr.rightnews.usecases

import androidx.test.filters.SmallTest
import com.cailloutr.rightnews.TestCoroutineDispatcher
import com.cailloutr.rightnews.constants.Constants.TEST_DB
import com.cailloutr.rightnews.data.local.NewsDatabase
import com.cailloutr.rightnews.data.network.service.TheGuardianApiImpl
import com.cailloutr.rightnews.repository.FakeNewsRepository
import com.cailloutr.rightnews.repository.NewsRepositoryInterface
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@SmallTest
class SearchArticleUseCaseTest {

    private lateinit var repository: NewsRepositoryInterface
    private lateinit var useCase: SearchNewsUseCase

    @MockK
    private lateinit var theGuardianApiImpl: TheGuardianApiImpl

    @Inject
    @Named(TEST_DB)
    lateinit var database: NewsDatabase

    private lateinit var testDispatcher: TestCoroutineDispatcher

    @Before
    fun setUp() {
        repository = FakeNewsRepository(
            theGuardianApiImpl, database
        )
        useCase = SearchNewsUseCase(repository)
        testDispatcher = TestCoroutineDispatcher()
    }

    @Test
    fun test_invokeShouldReturnNewsWitchTitleContainsTheQueryContent() =
        runTest {
            /*val searchQuery = Constants.SEARCH_QUERY

            val response = useCase.invoke(searchQuery)

            response.data?.results?.forEach {
                assertThat(it.webTitle).ignoringCase().contains(searchQuery)
            }*/
            TODO()
        }
}