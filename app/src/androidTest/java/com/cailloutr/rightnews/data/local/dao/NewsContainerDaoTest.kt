package com.cailloutr.rightnews.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.cailloutr.rightnews.constants.Constants.ROOM_NEWS_CONTAINER_DEFAULT_SECTION
import com.cailloutr.rightnews.data.local.NewsDatabase
import com.cailloutr.rightnews.data.local.roommodel.RoomNewsContainer
import com.google.common.truth.Truth.assertThat
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
class NewsContainerDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: NewsDatabase

    @Inject
    @Named("test_news_container_dao")
    lateinit var dao: NewsContainerDao

    private lateinit var roomNewsContainerDao: RoomNewsContainer

    @Before
    fun setup() {
        hiltRule.inject()
        roomNewsContainerDao = RoomNewsContainer(
            id = ROOM_NEWS_CONTAINER_DEFAULT_SECTION,
            total = 10,
            startIndex = 1,
            pageSize = 10,
            currentPage = 1,
            pages = 200,
            orderBy = "newest"
        )
    }

    @Test
    fun test_insertSection() = runTest {
        dao.insertSection(roomNewsContainerDao)

        val retrievedContainer = dao.getNewsContainer(ROOM_NEWS_CONTAINER_DEFAULT_SECTION).first()

        assertThat(retrievedContainer).isEqualTo(roomNewsContainerDao)
    }

    @Test
    fun test_deleteNewsContainer() = runTest {
        dao.insertSection(roomNewsContainerDao)

        var retrievedContainer = dao.getAllNewsContainer().first()

        assertThat(retrievedContainer).contains(roomNewsContainerDao)

        dao.deleteNewsContainer(roomNewsContainerDao)

        retrievedContainer = dao.getAllNewsContainer().first()

        assertThat(retrievedContainer).doesNotContain(roomNewsContainerDao)
    }

    @Test
    fun test_getNewsContainer() = runTest {
        dao.insertSection(roomNewsContainerDao)

        val retrievedContainer = dao.getNewsContainer(roomNewsContainerDao.id).first()

        assertThat(retrievedContainer.id).isEqualTo(roomNewsContainerDao.id)
    }
}