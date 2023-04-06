package com.cailloutr.rightnews.model

import com.cailloutr.rightnews.constants.Constants
import com.cailloutr.rightnews.data.network.responses.sections.toSections
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

    @Test
    fun test_toAllSectionsShouldReturnAMapWithAllInitialsChars() {
        val sections1: Sections? = Constants.fakeResponseSectionRoot.body()?.response?.toSections()
        val initials = setOf('A', 'M', 'B')
        val expectedResult = mutableListOf<AllSectionsItem>()

        initials.forEach { char ->
            sections1?.listOfSections?.filter {
                it.title.startsWith(char, true)
            }?.let {
                expectedResult.add(
                    AllSectionsItem(
                        index = char.toString(),
                        list = it
                    )
                )
            }
        }

        val result: List<AllSectionsItem>? = sections1?.toAllSectionsItem()

        assertThat(result).containsExactlyElementsIn(expectedResult)
    }

}