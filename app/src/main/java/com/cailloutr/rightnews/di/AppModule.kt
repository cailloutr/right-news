package com.cailloutr.rightnews.di

import com.cailloutr.rightnews.BuildConfig
import com.cailloutr.rightnews.constants.Constants
import com.cailloutr.rightnews.data.network.service.TheGuardianApi
import com.cailloutr.rightnews.data.network.service.TheGuardianApiHelper
import com.cailloutr.rightnews.data.network.service.TheGuardianApiImpl
import com.cailloutr.rightnews.other.DefaultDispatchers
import com.cailloutr.rightnews.other.DispatchersProvider
import com.cailloutr.rightnews.repository.NewsRepository
import com.cailloutr.rightnews.usecases.GetNewsBySectionUseCase
import com.cailloutr.rightnews.usecases.GetRecentNewsUseCase
import com.cailloutr.rightnews.usecases.GetSectionsUseCase
import com.cailloutr.rightnews.usecases.NewsUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
            GetNewsBySectionUseCase(repository)
        )
    }

    @Provides
    fun provideDispatchersProvider(dispatchers: DefaultDispatchers): DispatchersProvider =
        dispatchers
}