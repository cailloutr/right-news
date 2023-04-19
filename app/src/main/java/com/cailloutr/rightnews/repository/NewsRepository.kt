package com.cailloutr.rightnews.repository

import android.util.Log
import com.cailloutr.rightnews.constants.Constants.API_CALL_FIELDS
import com.cailloutr.rightnews.constants.Constants.API_INITIAL_INDEX
import com.cailloutr.rightnews.data.local.NewsDatabase
import com.cailloutr.rightnews.data.local.roommodel.RoomArticle
import com.cailloutr.rightnews.data.local.roommodel.RoomSection
import com.cailloutr.rightnews.data.local.roommodel.toArticle
import com.cailloutr.rightnews.data.network.responses.news.NewsRoot
import com.cailloutr.rightnews.data.network.responses.news.toRoomArticle
import com.cailloutr.rightnews.data.network.responses.news.toRoomNewsContainer
import com.cailloutr.rightnews.data.network.responses.sections.toRoomSections
import com.cailloutr.rightnews.data.network.service.TheGuardianApiHelper
import com.cailloutr.rightnews.model.SectionWrapper
import com.cailloutr.rightnews.enums.OrderBy
import com.cailloutr.rightnews.enums.OrderBy.NEWEST
import com.cailloutr.rightnews.model.NewsContainer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

private const val TAG = "NewsRepository"

//TODO: Handle errors
class NewsRepository @Inject constructor(
    private val theGuardianApi: TheGuardianApiHelper,
    private val database: NewsDatabase,
) : NewsRepositoryInterface {
    override fun getAllSections(context: CoroutineDispatcher): Flow<List<RoomSection>> {
        return database.sectionDao.getAllSection()
    }

    override fun getNewsOrderedByDate(
        context: CoroutineDispatcher,
        section: SectionWrapper,
    ): Flow<NewsContainer> = channelFlow {
        database.newsContainerDao.getNewsContainer(section.sectionName)
            .collectLatest { roomNewsContainer ->
                roomNewsContainer?.let {
                    database.articleDao.getAllArticlesFromSection(roomNewsContainer.id)
                        .collectLatest { roomArticlesList ->
                            roomArticlesList?.let {
                                Log.i(TAG, "getNewsOrderedByDate: $roomArticlesList")
                                send(
                                    NewsContainer(
                                        id = roomNewsContainer.id,
                                        total = roomNewsContainer.total,
                                        startIndex = roomNewsContainer.startIndex,
                                        pageSize = roomNewsContainer.pageSize,
                                        currentPage = roomNewsContainer.currentPage,
                                        pages = roomNewsContainer.pages,
                                        orderBy = roomNewsContainer.orderBy,
                                        results = roomArticlesList.map { it.toArticle() }
                                    )
                                )
                            }
                        }
                }
            }
    }

    override suspend fun refreshSections(context: CoroutineDispatcher) {
        fetchSectionsFromApi(context)
    }

    override suspend fun refreshArticles(context: CoroutineDispatcher, section: SectionWrapper) {
        fetchNewsFromApi(context, section)
    }

    override suspend fun fetchSectionsFromApi(context: CoroutineDispatcher) {
        try {
            val response = withContext(context) {
                theGuardianApi.getAllSections()
            }

            if (response.isSuccessful) {
                val body: List<RoomSection>? = response.body()?.response?.results?.map {
                    it.toRoomSections()
                }

                body?.let {
                    withContext(context) {
                        database.sectionDao.insertSection(*it.toTypedArray())
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "fetchSectionsFromApi: ${e.printStackTrace()}")
        } catch (httpException: HttpException) {
            Log.e(TAG, "fetchNewsFromApi: ${httpException.printStackTrace()}")
        }
    }

    override suspend fun fetchNewsFromApi(
        context: CoroutineDispatcher,
        section: SectionWrapper,
    ) {
        var error: String = ""
        try {
            val response = withContext(context) {
                if (section.value.isNotEmpty()) {
                    theGuardianApi.getNewsOfSection(section.value)
                } else {
                    theGuardianApi.getNewsOrderedByDate(
                        NEWEST,
                        API_CALL_FIELDS,
                        API_INITIAL_INDEX
                    )
                }
            }

            if (response.isSuccessful) {

                cleanCache(context, section.sectionName)

                val container = response.body()?.response?.toRoomNewsContainer(section.sectionName)
                container?.let {
                    val articles = response.body()?.response?.results?.map {
                        it.toRoomArticle(container.id)
                    }

                    withContext(context) {
                        database.newsContainerDao.insertSection(container)
                        articles?.let {
                            database.articleDao.insertArticle(*articles.toTypedArray())
                        }
                    }
                }
            } else {
                error = response.errorBody().toString()
            }
        } catch (e: Exception) {
            Log.e(TAG, "fetchNewsFromApi: ${e.printStackTrace()}")
            Log.e(TAG, "Error: $error")
        } catch (httpException: HttpException) {
            Log.e(TAG, "fetchNewsFromApi: ${httpException.printStackTrace()}")
            Log.e(TAG, "Error: $error")
        }
    }


    override suspend fun getNewsBySection(section: String): Response<NewsRoot> =
        theGuardianApi.getNewsOfSection(section)

    override suspend fun getNewsOrderedByDate(
        orderBy: OrderBy,
        fields: String,
        page: Int,
    ): Response<NewsRoot> =
        theGuardianApi.getNewsOrderedByDate(
            orderBy = orderBy,
            fields = fields,
            page = page
        )

    override suspend fun searchNews(
        searchQuery: String,
        orderBy: OrderBy,
        fields: String,
    ): Response<NewsRoot> =
        theGuardianApi.searchNews(
            searchQuery = searchQuery,
            orderBy = orderBy,
            fields = fields
        )

    override suspend fun cleanCache(context: CoroutineDispatcher, section: String) {
        val cachedData: List<RoomArticle> =
            database.articleDao.getAllArticlesFromSection(section).first()

        database.articleDao.deleteArticle(*cachedData.toTypedArray())
    }

}
