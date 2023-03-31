package com.cailloutr.rightnews.model

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class SectionsKtTest {

    private lateinit var sectionsList: List<Section>
    private lateinit var sections: Sections
    private lateinit var sectionsId: List<String>

    @Before
    fun setUp() {
        sectionsList = List(5) {
            Section(
                id = it.toString(),
                title = it.toString(),
                webUrl = it.toString(),
                apiUrl = it.toString(),
                code = it.toString()
            )
        }

        sections = Sections(
            total = sectionsList.size.toLong(),
            listOfSections = sectionsList
        )

        sectionsId = listOf("1", "2")
    }

    @Test
    fun test_filterWithNoInputShouldReturnTheSameObject() {
        val result: Sections = sections.filter()

        assertThat(result).isEqualTo(sections)
    }

    @Test
    fun test_filterWithInputShouldReturnObjectWithDifferentListOfSections() {
        val result: Sections = sections.filter(sectionsId)

        assertThat(result).isNotEqualTo(sections)
    }

}