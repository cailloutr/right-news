package com.cailloutr.rightnews.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.cailloutr.rightnews.data.local.NewsDatabase
import com.cailloutr.rightnews.data.local.roommodel.RoomSection
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@OptIn(ExperimentalCoroutinesApi::class)
@SmallTest
@HiltAndroidTest
class SectionDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: NewsDatabase

    @Inject
    @Named("test_section_dao")
    lateinit var dao: SectionDao

    private lateinit var roomSection: RoomSection

    @Before
    fun setup() {
        hiltRule.inject()
        roomSection = RoomSection(
            id = "1",
            title = "Teste",
            webUrl = "Teste",
            apiUrl = "www.api.com",
            code = "UK"
        )
    }


    @Test
    fun test_insertSection() = runTest {
        dao.insertSection(roomSection)

        val retrievedContainer = dao.getAllSection().first()

        Truth.assertThat(retrievedContainer).contains(roomSection)
    }

    @Test
    fun test_deleteNewsContainer() = runTest {
        dao.insertSection(roomSection)

        var retrievedContainer = dao.getAllSection().first()

        Truth.assertThat(retrievedContainer).contains(roomSection)

        dao.deleteSection(roomSection)

        retrievedContainer = dao.getAllSection().first()

        Truth.assertThat(retrievedContainer).doesNotContain(roomSection)
    }

    @Test
    fun test_getNewsContainer() = runTest {
        dao.insertSection(roomSection)

        val retrievedContainer = dao.getSection(roomSection.id).first()

        Truth.assertThat(retrievedContainer.id).isEqualTo(roomSection.id)
    }
}