package com.cailloutr.rightnews.di

import android.content.Context
import androidx.room.Room
import com.cailloutr.rightnews.data.local.NewsDatabase
import com.cailloutr.rightnews.data.local.dao.ArticleDao
import com.cailloutr.rightnews.data.local.dao.NewsContainerDao
import com.cailloutr.rightnews.data.local.dao.SectionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("test_db")
    fun provideInMemoryDatabase(@ApplicationContext context: Context): NewsDatabase =
        Room.inMemoryDatabaseBuilder(
            context,
            NewsDatabase::class.java
        ).allowMainThreadQueries().build()

    @Provides
    @Named("test_article_dao")
    fun provideArticleDao(database: NewsDatabase): ArticleDao =
        database.articleDao

    @Provides
    @Named("test_news_container_dao")
    fun provideNewsContainerDao(database: NewsDatabase): NewsContainerDao =
        database.newsContainerDao

    @Provides
    @Named("test_section_dao")
    fun provideSectionDao(database: NewsDatabase): SectionDao =
        database.sectionDao


}