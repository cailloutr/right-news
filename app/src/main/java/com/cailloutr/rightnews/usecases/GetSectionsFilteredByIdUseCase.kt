package com.cailloutr.rightnews.usecases

import com.cailloutr.rightnews.data.network.responses.sections.toDefaultSection
import com.cailloutr.rightnews.model.Section
import com.cailloutr.rightnews.other.Status
import com.cailloutr.rightnews.repository.NewsRepositoryInterface
import javax.inject.Inject

// games, sport, tech, books, world-news, politics, culture, education
class GetSectionsFilteredByIdUseCase @Inject constructor(
    private val newsRepository: NewsRepositoryInterface,
) {
    suspend operator fun invoke(
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
    }
}