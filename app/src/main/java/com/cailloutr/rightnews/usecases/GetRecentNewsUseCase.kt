package com.cailloutr.rightnews.usecases

import com.cailloutr.rightnews.data.network.responses.news.NewsResponse
import com.cailloutr.rightnews.enums.OrderBy
import com.cailloutr.rightnews.other.Resource
import com.cailloutr.rightnews.repository.NewsRepository
import javax.inject.Inject

class GetRecentNewsUseCase @Inject constructor(
    private val repository: NewsRepository,
) {

    suspend operator fun invoke(
        orderBy: OrderBy,
        fields: String,
    ): Resource<NewsResponse> {
        try {
            val result = repository.getNewsOrderedByDate(
                orderBy = orderBy,
                fields = fields
            )
            if (result.isSuccessful) {
                return Resource.success(data = result.body()?.response)
            }
            return Resource.error(msg = result.message(), data = null)
        } catch (e: Exception) {
            return Resource.error(msg = e.message.toString(), data = null)
        }
    }


}
