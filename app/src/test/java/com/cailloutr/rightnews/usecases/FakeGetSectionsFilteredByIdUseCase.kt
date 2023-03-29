package com.cailloutr.rightnews.usecases

import com.cailloutr.rightnews.model.Section

class FakeGetSectionsFilteredByIdUseCase {

    operator fun invoke(
        sections: List<String>?
    ): List<Section> {
        val listOfSections: List<Section>? = sections?.map {
            Section(
                id = it,
                title = it,
                webUrl = it,
                apiUrl = it,
                code = it
            )
        }

        return listOfSections ?: listOf()
    }
}