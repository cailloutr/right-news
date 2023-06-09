package com.cailloutr.rightnews.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cailloutr.rightnews.data.local.dao.ArticleDao
import com.cailloutr.rightnews.data.local.dao.NewsContainerDao
import com.cailloutr.rightnews.data.local.dao.SectionDao
import com.cailloutr.rightnews.data.local.roommodel.RoomArticle
import com.cailloutr.rightnews.data.local.roommodel.RoomNewsContainer
import com.cailloutr.rightnews.data.local.roommodel.RoomSection

@Database(
    entities = [RoomArticle::class, RoomNewsContainer::class, RoomSection::class],
    version = 2,
    exportSchema = true
)
abstract class NewsDatabase : RoomDatabase() {

    abstract val articleDao: ArticleDao
    abstract val newsContainerDao: NewsContainerDao
    abstract val sectionDao: SectionDao
}