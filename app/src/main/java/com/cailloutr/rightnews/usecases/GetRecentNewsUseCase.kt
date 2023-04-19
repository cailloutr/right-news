package com.cailloutr.rightnews.usecases

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.cailloutr.rightnews.constants.Constants.PAGE_SIZE
import com.cailloutr.rightnews.data.NewsPagingSource
import com.cailloutr.rightnews.model.Article
import com.cailloutr.rightnews.repository.NewsRepositoryInterface
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentNewsUseCase @Inject constructor(
    private val repository: NewsRepositoryInterface,
) {

    operator fun invoke(): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { NewsPagingSource(repository) }
        ).flow
    }

/*    suspend operator fun invoke(
        orderBy: OrderBy,
        fields: String,
        page: Int
    ): Resource<NewsContainer> {
        try {
            val result = repository.getNewsOrderedByDate(
                orderBy = orderBy,
                fields = fields,
                page = page
            )
            if (result.isSuccessful) {
                return Resource.success(data = result.body()?.response?.toNewsContainer())
            }
            return Resource.error(msg = result.message(), data = null)
        } catch (e: Exception) {
            return Resource.error(msg = e.message.toString(), data = null)
        }
    }*/


}
