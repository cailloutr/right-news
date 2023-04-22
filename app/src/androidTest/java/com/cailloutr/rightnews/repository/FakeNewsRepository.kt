package com.cailloutr.rightnews.repository

import com.cailloutr.rightnews.constants.Constants
import com.cailloutr.rightnews.data.local.NewsDatabase
import com.cailloutr.rightnews.data.local.roommodel.RoomArticle
import com.cailloutr.rightnews.data.local.roommodel.RoomSection
import com.cailloutr.rightnews.data.local.roommodel.toArticle
import com.cailloutr.rightnews.data.network.responses.news.NewsRoot
import com.cailloutr.rightnews.data.network.responses.news.toRoomArticle
import com.cailloutr.rightnews.data.network.responses.news.toRoomNewsContainer
import com.cailloutr.rightnews.data.network.responses.sections.toRoomSections
import com.cailloutr.rightnews.data.network.service.TheGuardianApiHelper
import com.cailloutr.rightnews.enums.OrderBy
import com.cailloutr.rightnews.model.NewsContainer
import com.cailloutr.rightnews.model.SectionWrapper
import com.cailloutr.rightnews.other.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import retrofit2.Response

class FakeNewsRepository(
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

    override suspend fun refreshArticles(
        context: CoroutineDispatcher,
        section: SectionWrapper,
        responseStatus: (message: Resource<Exception>) -> Unit,
    ) {
        fetchNewsFromApi(context, section, responseStatus)
    }


    override suspend fun fetchSectionsFromApi(context: CoroutineDispatcher) {
        val response = theGuardianApi.getAllSections()

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
    }

    override suspend fun fetchNewsFromApi(context: CoroutineDispatcher, section: SectionWrapper, responseStatus: (message: Resource<Exception>) -> Unit) {
        var error: String = ""
        try {
            val response = withContext(context) {
                if (section.value.isNotEmpty()) {
                    theGuardianApi.getNewsOfSection(section.value)
                } else {
                    theGuardianApi.getNewsOrderedByDate(
                        OrderBy.NEWEST,
                        Constants.API_CALL_FIELDS,
                        Constants.API_INITIAL_INDEX
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
        } catch (_: Exception) {

        }
    }


    override suspend fun getNewsBySection(section: String): Response<NewsRoot> {
        return Response.success(
            Constants.fakeArticle
        )
    }

    override suspend fun getNewsOrderedByDate(
        orderBy: OrderBy,
        fields: String,
        page: Int,
    ): Response<NewsRoot> {
        return Response.success(Constants.fakeArticle)
    }

    override suspend fun searchNews(
        searchQuery: String,
        orderBy: OrderBy,
        fields: String,
    ): Response<NewsRoot> {
        return Response.success(Constants.fakeSearchArticle)
    }

    override suspend fun cleanCache(context: CoroutineDispatcher, section: String) {
        val cachedData: List<RoomArticle> =
            database.articleDao.getAllArticlesFromSection(section).first()

        database.articleDao.deleteArticle(*cachedData.toTypedArray())
    }
}