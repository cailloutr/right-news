package com.cailloutr.rightnews.usecases

import com.cailloutr.rightnews.data.network.responses.news.toNewsList
import com.cailloutr.rightnews.model.News
import com.cailloutr.rightnews.other.Resource
import com.cailloutr.rightnews.repository.NewsRepositoryInterface

class GetNewsBySectionUseCase(
    private val repository: NewsRepositoryInterface,
) {

    suspend operator fun invoke(
        section: String,
    ): Resource<List<News>> {
        try {
            val response = repository.getNewsBySection(section)

            if (response.isSuccessful) {
                return Resource.success(data = response.body()?.response?.toNewsList())
            }
            return Resource.error(msg = response.message(), data = null)
        } catch (e: Exception) {
            return Resource.error(msg = e.message.toString(), data = null)
        }
    }
}