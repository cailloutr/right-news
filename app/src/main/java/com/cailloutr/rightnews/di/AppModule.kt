package com.cailloutr.rightnews.di

import android.content.Context
import androidx.room.Room
import com.cailloutr.rightnews.BuildConfig
import com.cailloutr.rightnews.constants.Constants
import com.cailloutr.rightnews.constants.Constants.DATABASE_NAME
import com.cailloutr.rightnews.data.local.NewsDatabase
import com.cailloutr.rightnews.data.local.dao.ArticleDao
import com.cailloutr.rightnews.data.local.dao.NewsContainerDao
import com.cailloutr.rightnews.data.local.dao.SectionDao
import com.cailloutr.rightnews.data.network.service.TheGuardianApi
import com.cailloutr.rightnews.data.network.service.TheGuardianApiHelper
import com.cailloutr.rightnews.data.network.service.TheGuardianApiImpl
import com.cailloutr.rightnews.other.DefaultDispatchers
import com.cailloutr.rightnews.other.DispatchersProvider
import com.cailloutr.rightnews.repository.NewsRepository
import com.cailloutr.rightnews.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NewsDatabase =
        Room.databaseBuilder(
            context,
            NewsDatabase::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun providesArticleDao(database: NewsDatabase): ArticleDao =
        database.articleDao

    @Provides
    @Singleton
    fun providesNewsContainerDao(database: NewsDatabase): NewsContainerDao =
        database.newsContainerDao

    @Provides
    @Singleton
    fun providesSectionDao(database: NewsDatabase): SectionDao =
        database.sectionDao

    @Provides
    fun provideBaseUrl() = Constants.BASE_URL

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else {
        OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .baseUrl(Constants.BASE_URL)
        .build()

    @Provides
    @Singleton
    fun provideTheGuardianApiService(retrofit: Retrofit): TheGuardianApi =
        retrofit.create(TheGuardianApi::class.java)

    @Provides
    @Singleton
    fun provideTheGuardianApiHelper(theGuardianApiImpl: TheGuardianApiImpl): TheGuardianApiHelper =
        theGuardianApiImpl


    @Provides
    fun providesNewsUseCases(repository: NewsRepository): NewsUseCases {
        return NewsUseCases(
            GetSectionsUseCase(repository),
            GetRecentNewsUseCase(repository),
            GetNewsBySectionUseCase(repository),
            SearchNewsUseCase((repository))
        )
    }

    @Provides
    fun provideDispatchersProvider(dispatchers: DefaultDispatchers): DispatchersProvider =
        dispatchers
}