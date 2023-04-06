package com.cailloutr.rightnews.model

data class Sections(
    val total: Long,
    val listOfSections: List<Section>,
)

fun Sections.filter(section: List<String>? = null): Sections {
    if (section.isNullOrEmpty()) {
        return this
    }

    val filteredSections = mutableListOf<Section>()
    section.forEach { expectedId ->
        listOfSections.forEach { section ->
            if (section.id == expectedId) {
                filteredSections.add(section)
            }
        }
    }
    return this.copy(
        listOfSections = filteredSections
    )
}

fun Sections.toAllSectionsItem(): List<AllSectionsItem> {
    val initials = listOfSections.map {
        it.title[0]
    }

    val list = mutableListOf<AllSectionsItem>()

    initials.toSet().forEach { char ->
        list.add(
            AllSectionsItem(
                index = char.toString(),
                list = listOfSections.filter {
                    it.title.startsWith(char, true)
                }
            )
        )
    }

    return list
}