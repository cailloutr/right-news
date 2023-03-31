package com.cailloutr.rightnews.usecases

import com.cailloutr.rightnews.data.network.responses.sections.toSections
import com.cailloutr.rightnews.model.Sections
import com.cailloutr.rightnews.other.Resource
import com.cailloutr.rightnews.repository.NewsRepositoryInterface
import javax.inject.Inject

//private const val TAG = "GetSectionsUseCase"

// games, sport, tech, books, world-news, politics, culture, education
class GetSectionsUseCase @Inject constructor(
    private val newsRepository: NewsRepositoryInterface,
) {

/*    suspend operator fun invoke(
        sections: List<String>?,
    ): List<Section> {
        val filteredSections = mutableListOf<Section>()
        val allSections = newsRepository.getAllSections()

        if (allSections.status == Status.SUCCESS) {
            if (sections.isNullOrEmpty() ) {
                allSections.data?.response?.results?.forEach { sectionResult ->
                    filteredSections.add(sectionResult.toDefaultSection())
                }
                return filteredSections
            }

            sections.forEach { expectedId ->
                allSections.data?.response?.results?.forEach { sectionResult ->
                    if (sectionResult.id == expectedId) {
                        filteredSections.add(sectionResult.toDefaultSection())
                    }
                }
            }
        }
        return filteredSections
    }*/

    suspend operator fun invoke(): Resource<Sections> {
        try {
            val response = newsRepository.getAllSections()
            if (response.isSuccessful) {
                return Resource.success(data = response.body()?.response?.toSections())
            }
            return Resource.error(msg = response.message(), data = null)
        } catch (e: java.lang.Exception) {
            return Resource.error(msg = e.message.toString(), data = null)
        }
    }
}