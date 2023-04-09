package com.cailloutr.rightnews.data

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.cailloutr.rightnews.constants.Constants.API_CALL_FIELDS
import com.cailloutr.rightnews.constants.Constants.PAGE_SIZE
import com.cailloutr.rightnews.enums.OrderBy.NEWEST
import com.cailloutr.rightnews.model.News
import com.cailloutr.rightnews.other.Status
import com.cailloutr.rightnews.usecases.NewsUseCases
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NewsDataSource : PagingSource<Int, News>() {

    @Inject
    lateinit var newsUseCases: NewsUseCases

    override fun getRefreshKey(state: PagingState<Int, News>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, News> {
        try {
            val nextPage = params.key ?: 1
            val data = newsUseCases.getRecentNewsUseCase(
                orderBy = NEWEST,
                fields = API_CALL_FIELDS,
                page = nextPage
            )
            return if (data.status == Status.SUCCESS) {
                LoadResult.Page(
                    data = data.data?.results ?: listOf(),
                    prevKey = if (nextPage == 1) null else nextPage - 1,
                    nextKey = nextPage + 1
                )
            } else {
                LoadResult.Error(Exception(message = data.message))
            }
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    companion object {
        val pagingConfig = PagingConfig(
            pageSize = PAGE_SIZE,
            initialLoadSize = PAGE_SIZE,
            enablePlaceholders = true
        )
    }
}