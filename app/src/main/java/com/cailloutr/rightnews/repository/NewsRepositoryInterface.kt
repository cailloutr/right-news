package com.cailloutr.rightnews.repository

import com.cailloutr.rightnews.constants.Constants.API_CALL_FIELDS
import com.cailloutr.rightnews.constants.Constants.API_INITIAL_INDEX
import com.cailloutr.rightnews.data.local.roommodel.RoomSection
import com.cailloutr.rightnews.data.network.responses.news.NewsRoot
import com.cailloutr.rightnews.model.SectionWrapper
import com.cailloutr.rightnews.enums.OrderBy
import com.cailloutr.rightnews.model.NewsContainer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface NewsRepositoryInterface {

    fun getAllSections(context: CoroutineDispatcher): Flow<List<RoomSection>>

    fun getNewsOrderedByDate(context: CoroutineDispatcher, section: SectionWrapper): Flow<NewsContainer>

    suspend fun getNewsBySection(
        section: String
    ): Response<NewsRoot>

    suspend fun getNewsOrderedByDate(
        orderBy: OrderBy = OrderBy.NEWEST,
        fields: String = API_CALL_FIELDS,
        page: Int = API_INITIAL_INDEX
    ): Response<NewsRoot>

    suspend fun searchNews(
        searchQuery: String,
        orderBy: OrderBy = OrderBy.NEWEST,
        fields: String = API_CALL_FIELDS
    ): Response<NewsRoot>

    suspend fun cleanCache(context: CoroutineDispatcher, section: String)
    suspend fun refreshSections(context: CoroutineDispatcher)

    suspend fun refreshArticles(context: CoroutineDispatcher, section: SectionWrapper)

    suspend fun fetchSectionsFromApi(context: CoroutineDispatcher)

    suspend fun fetchNewsFromApi(context: CoroutineDispatcher, section: SectionWrapper)


}