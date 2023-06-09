package com.cailloutr.rightnews.usecases

import com.cailloutr.rightnews.constants.Constants.SEARCH_RESULT
import com.cailloutr.rightnews.data.network.responses.news.toNewsContainer
import com.cailloutr.rightnews.model.NewsContainer
import com.cailloutr.rightnews.other.Resource
import com.cailloutr.rightnews.repository.NewsRepositoryInterface
import javax.inject.Inject

class SearchNewsUseCase @Inject constructor(
    private val repository: NewsRepositoryInterface
) {

    suspend operator fun invoke(
        searchQuery: String
    ): Resource<NewsContainer> {
        try {
            val response = repository.searchNews(
                searchQuery = searchQuery
            )
            if (response.isSuccessful) {
                return Resource.success(data = response.body()?.response?.toNewsContainer(SEARCH_RESULT))
            }
            return Resource.error(msg = response.message(), data = null)
        } catch (e: Exception) {
            return Resource.error(msg = e.message.toString(), data = null)
        }
    }
}