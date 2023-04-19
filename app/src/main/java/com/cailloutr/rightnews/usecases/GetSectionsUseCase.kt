package com.cailloutr.rightnews.usecases

import com.cailloutr.rightnews.data.local.roommodel.RoomSection
import com.cailloutr.rightnews.repository.NewsRepositoryInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

//private const val TAG = "GetSectionsUseCase"

// games, sport, tech, books, world-news, politics, culture, education
class GetSectionsUseCase @Inject constructor(
    private val newsRepository: NewsRepositoryInterface,
) {

    operator fun invoke(context: CoroutineDispatcher): Flow<List<RoomSection>> {
        CoroutineScope(context).launch {
            newsRepository.refreshSections(context)
        }
        return newsRepository.getAllSections(context)
    }

/*    suspend operator fun invoke(): Resource<Sections> {
        try {
            val response = newsRepository.getAllSections()
            if (response.isSuccessful) {
                return Resource.success(data = response.body()?.response?.toSections())
            }
            return Resource.error(msg = response.message(), data = null)
        } catch (e: java.lang.Exception) {
            return Resource.error(msg = e.message.toString(), data = null)
        }
    }*/
}