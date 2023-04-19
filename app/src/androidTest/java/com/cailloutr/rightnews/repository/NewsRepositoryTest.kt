package com.cailloutr.rightnews.repository

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.cailloutr.rightnews.TestCoroutineDispatcher
import com.cailloutr.rightnews.constants.Constants
import com.cailloutr.rightnews.constants.Constants.API_CALL_FIELDS
import com.cailloutr.rightnews.constants.Constants.API_INITIAL_INDEX
import com.cailloutr.rightnews.constants.Constants.ROOM_NEWS_CONTAINER_DEFAULT_SECTION
import com.cailloutr.rightnews.data.local.NewsDatabase
import com.cailloutr.rightnews.data.local.roommodel.RoomArticle
import com.cailloutr.rightnews.data.local.roommodel.RoomNewsContainer
import com.cailloutr.rightnews.data.local.roommodel.RoomSection
import com.cailloutr.rightnews.data.local.roommodel.toSection
import com.cailloutr.rightnews.data.network.responses.news.NewsRoot
import com.cailloutr.rightnews.data.network.responses.news.toArticle
import com.cailloutr.rightnews.data.network.responses.sections.toRoomSections
import com.cailloutr.rightnews.data.network.responses.sections.toSections
import com.cailloutr.rightnews.data.network.service.TheGuardianApiImpl
import com.cailloutr.rightnews.enums.OrderBy.NEWEST
import com.cailloutr.rightnews.model.NewsContainer
import com.cailloutr.rightnews.model.Section
import com.cailloutr.rightnews.model.SectionWrapper
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

@OptIn(ExperimentalCoroutinesApi::class)
@SmallTest
@HiltAndroidTest
class NewsRepositoryTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @MockK
    lateinit var theGuardianApi: TheGuardianApiImpl

    private lateinit var repository: NewsRepository
    private lateinit var testDispatcher: TestCoroutineDispatcher

    @Inject
    @Named("test_db")
    lateinit var database: NewsDatabase


    @Before
    fun setUp() {
        hiltRule.inject()
        testDispatcher = TestCoroutineDispatcher()
        MockKAnnotations.init(this, true)
        repository = spyk(
            NewsRepository(
                theGuardianApi,
                database
            )
        )
    }


    @Test
    fun getNewsOrderedByDateShouldRetrieveSectionsFromDatabase() = runTest {
        val response: Response<NewsRoot> = Response.success(Constants.fakeArticle)

        coEvery {
            theGuardianApi.getNewsOrderedByDate(
                orderBy = NEWEST,
                fields = API_CALL_FIELDS,
                page = API_INITIAL_INDEX
            )
        } returns (response)

        coEvery {
            theGuardianApi.getNewsOfSection(ROOM_NEWS_CONTAINER_DEFAULT_SECTION)
        } returns (response)

        repository.refreshArticles(
            testDispatcher.io,
            SectionWrapper(ROOM_NEWS_CONTAINER_DEFAULT_SECTION, ROOM_NEWS_CONTAINER_DEFAULT_SECTION)
        )

        val result: Flow<NewsContainer> =
            repository.getNewsOrderedByDate(
                testDispatcher.io,
                SectionWrapper(
                    ROOM_NEWS_CONTAINER_DEFAULT_SECTION,
                    ROOM_NEWS_CONTAINER_DEFAULT_SECTION
                )
            )

        result.test {
            val awaitItem: NewsContainer = awaitItem()
            val newsResponse = response.body()?.response!!

            assertThat(awaitItem.orderBy).isEqualTo(newsResponse.orderBy)
            assertThat(awaitItem.pages).isEqualTo(newsResponse.pages)
            assertThat(awaitItem.pageSize).isEqualTo(newsResponse.pageSize)
            assertThat(awaitItem.currentPage).isEqualTo(newsResponse.currentPage)
            assertThat(awaitItem.startIndex).isEqualTo(newsResponse.startIndex)
            assertThat(awaitItem.total).isEqualTo(newsResponse.total)

            assertThat(awaitItem.results).containsExactlyElementsIn(
                newsResponse.results.map { it.toArticle() }
            )
        }
    }

    @Test
    fun refreshArticlesShouldCallFetchArticlesFromApi() = runTest {
        repository.refreshArticles(
            testDispatcher.io,
            SectionWrapper(ROOM_NEWS_CONTAINER_DEFAULT_SECTION, ROOM_NEWS_CONTAINER_DEFAULT_SECTION)
        )

        coVerify {
            repository.fetchNewsFromApi(
                testDispatcher.io,
                SectionWrapper(
                    ROOM_NEWS_CONTAINER_DEFAULT_SECTION,
                    ROOM_NEWS_CONTAINER_DEFAULT_SECTION
                )
            )
        }
    }

    @Test
    fun fetchArticlesFromApiWhenSuccessfulShouldUpdateTheDatabase() = runTest {
        val response: Response<NewsRoot> = Response.success(Constants.fakeArticle)

        coEvery {
            theGuardianApi.getNewsOfSection(ROOM_NEWS_CONTAINER_DEFAULT_SECTION)
        } returns (response)

        repository.fetchNewsFromApi(
            testDispatcher.io,
            SectionWrapper(ROOM_NEWS_CONTAINER_DEFAULT_SECTION, ROOM_NEWS_CONTAINER_DEFAULT_SECTION)
        )

        val newsContainerResult: RoomNewsContainer =
            database.newsContainerDao.getNewsContainer(ROOM_NEWS_CONTAINER_DEFAULT_SECTION).first()

        Log.i("NewsRepositoryTest", ": $newsContainerResult")

        val apiResponse = response.body()?.response!!
        val articlesResult: List<RoomArticle> =
            database.articleDao.getAllArticlesFromSection(newsContainerResult.id).first()


        assertThat(newsContainerResult.total).isEqualTo(apiResponse.total)
        assertThat(newsContainerResult.startIndex).isEqualTo(apiResponse.startIndex)
        assertThat(newsContainerResult.pageSize).isEqualTo(apiResponse.pageSize)
        assertThat(newsContainerResult.pages).isEqualTo(apiResponse.pages)
        assertThat(newsContainerResult.currentPage).isEqualTo(apiResponse.currentPage)
        assertThat(newsContainerResult.orderBy).isEqualTo(apiResponse.orderBy)

        articlesResult.forEach {
            assertThat(it.containerId).isEqualTo(newsContainerResult.id)
        }

    }


    @Test
    fun getAllSectionsShouldRetrieveSectionsFromDatabase() = runTest {
        val response = Constants.fakeResponseSectionRoot
        coEvery { theGuardianApi.getAllSections() } returns (response)

        repository.refreshSections(testDispatcher.io)

        val result = repository.getAllSections(testDispatcher.io)

        result.test {
            assertThat(awaitItem()).containsExactlyElementsIn(response.body()?.response?.results?.map { it.toRoomSections() }!!)
        }
    }

    @Test
    fun refreshSectionsShouldCallFetchSectionsFromApi() = runTest {
        repository.refreshSections(testDispatcher.io)

        coVerify { repository.fetchSectionsFromApi(testDispatcher.io) }
    }

    @Test
    fun fetchSectionsFromApiWhenSuccessfulShouldUpdateTheDatabase() = runTest {
        val response = Constants.fakeResponseSectionRoot

        coEvery {
            theGuardianApi.getAllSections()
        } returns (response)

        repository.fetchSectionsFromApi(testDispatcher.io)

        val result: Flow<List<RoomSection>> = database.sectionDao.getAllSection()

        result.test {
            val resultList: List<Section> = awaitItem().map {
                it.toSection()
            }
            assertThat(resultList).containsExactlyElementsIn(response.body()?.response?.toSections()?.listOfSections)
        }
    }

    /*  @Test
      fun getAllSectionsWhenApiResultIsSuccessThenReturnResponseOfSuccess() =
          runTest {
              coEvery {
                  theGuardianApi.getAllSections()
              }.returns(Response.success(null))

              val result = repository.getAllSections()

              assertThat(result.isSuccessful).isTrue()
          }

      @Test
      fun getAllSectionsWhenApiResultIsErrorThenReturnResourceOfError() = runTest {
          val error = "Error"
          val response = Response.error<SectionsRoot>(
              404,
              error.toResponseBody()
          )
          coEvery { theGuardianApi.getAllSections() }.returns(
              response
          )

          val result = repository.getAllSections()

          assertThat(result.isSuccessful).isFalse()
          assertThat(result.message()).isEqualTo(response.message())
      }*/

    @Test
    fun getnewsOrderedByDateWhenResposneIsErrorShouldReturnResourceError() =
        runTest {
            val error = "Error"
            val response = Response.error<NewsRoot>(
                404,
                error.toResponseBody()
            )
            coEvery {
                theGuardianApi.getNewsOrderedByDate(
                    NEWEST,
                    API_CALL_FIELDS,
                    API_INITIAL_INDEX
                )
            }.returns(
                response
            )

            val result: Response<NewsRoot> = repository.getNewsOrderedByDate(
                NEWEST,
                API_CALL_FIELDS
            )

            assertThat(result).isEqualTo(response)
        }

    @Test
    fun getNewsOfSectionsWhenResponseIsErrorShouldReturnAResourceError() = runTest {
        val error = "Error"
        val response = Response.error<NewsRoot>(
            404,
            error.toResponseBody()
        )

        coEvery { theGuardianApi.getNewsOfSection("section") }.returns(
            response
        )

        val result = repository.getNewsBySection("section")

        assertThat(result.isSuccessful).isFalse()
    }

    @Test
    fun getNewsOfSectionsWhenResponseIsSuccessShouldReturnResourceSuccess() =
        runTest {
            coEvery { theGuardianApi.getNewsOfSection("section") }.returns(
                Response.success(null)
            )

            val result = repository.getNewsBySection("section")

            assertThat(result.isSuccessful).isTrue()
        }

    @Test
    fun searchNewsWhenResponseIsSuccessShouldReturnResourceSuccess() = runTest {
        val query = "query"

        coEvery {
            theGuardianApi.searchNews(
                NEWEST, API_CALL_FIELDS, query
            )
        } returns (Response.success(null))

        val result = repository.searchNews(
            searchQuery = query,
            orderBy = NEWEST,
            fields = API_CALL_FIELDS
        )

        assertThat(result.isSuccessful).isTrue()
    }

    @Test
    fun searchNewsWhenResponseIsErrorSHouldReturnResourceError() = runTest {
        val query = "query"

        coEvery {
            theGuardianApi.searchNews(
                NEWEST, API_CALL_FIELDS, query
            )
        } returns (Response.error(404, "error".toResponseBody()))

        val result = repository.searchNews(
            searchQuery = query,
            orderBy = NEWEST,
            fields = API_CALL_FIELDS
        )

        assertThat(result.isSuccessful).isFalse()
    }
}