package com.cailloutr.rightnews.usecases

import com.cailloutr.rightnews.data.network.responses.news.toNewsContainer
import com.cailloutr.rightnews.enums.OrderBy
import com.cailloutr.rightnews.model.NewsContainer
import com.cailloutr.rightnews.other.Resource
import com.cailloutr.rightnews.repository.NewsRepositoryInterface
import javax.inject.Inject

class GetRecentNewsUseCase @Inject constructor(
    private val repository: NewsRepositoryInterface,
) {

    suspend operator fun invoke(
        orderBy: OrderBy,
        fields: String,
    ): Resource<NewsContainer> {
        try {
            val result = repository.getNewsOrderedByDate(
                orderBy = orderBy,
                fields = fields
            )
            if (result.isSuccessful) {
                return Resource.success(data = result.body()?.response?.toNewsContainer())
            }
            return Resource.error(msg = result.message(), data = null)
        } catch (e: Exception) {
            return Resource.error(msg = e.message.toString(), data = null)
        }
    }


}
