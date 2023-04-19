package com.cailloutr.rightnews.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.cailloutr.rightnews.TestCoroutineDispatcher
import com.cailloutr.rightnews.constants.Constants.API_CALL_FIELDS
import com.cailloutr.rightnews.constants.Constants.API_INITIAL_INDEX
import com.cailloutr.rightnews.constants.Constants.LATEST_NEWS
import com.cailloutr.rightnews.constants.Constants.TEST_DB
import com.cailloutr.rightnews.constants.Constants.fakeArticle
import com.cailloutr.rightnews.data.local.NewsDatabase
import com.cailloutr.rightnews.data.network.service.TheGuardianApiImpl
import com.cailloutr.rightnews.enums.OrderBy.NEWEST
import com.cailloutr.rightnews.model.SectionWrapper
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
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@SmallTest
class GetRecentArticleUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private lateinit var newsRepository: NewsRepositoryInterface
    private lateinit var userCase: GetNewsBySectionUseCase

    @MockK
    private lateinit var theGuardianApiImpl: TheGuardianApiImpl

    @Inject
    @Named(TEST_DB)
    lateinit var database: NewsDatabase

    private lateinit var testDispatcher: TestCoroutineDispatcher

    @Before
    fun setup() {
        hiltRule.inject()
        newsRepository = FakeNewsRepository(
            theGuardianApiImpl, database
        )
        userCase = GetNewsBySectionUseCase(newsRepository)
        testDispatcher = TestCoroutineDispatcher()
    }

    @Test
    fun test_invokeShouldReturnShouldArticlesWhereContainerIdIsEqualToLatestNews() =
        runTest {
            val response = Response.success(fakeArticle)
            val sectionWrapper = SectionWrapper(LATEST_NEWS, "")

            coEvery { theGuardianApiImpl.getNewsOfSection(sectionWrapper.value) } returns (response)

            coEvery {
                theGuardianApiImpl.getNewsOrderedByDate(
                    NEWEST,
                    API_CALL_FIELDS,
                    API_INITIAL_INDEX
                )
            } returns (response)

            val result =
                userCase(testDispatcher.io, sectionWrapper).first()

            assertThat(result.id).isEqualTo(sectionWrapper.sectionName)
        }
}