package com.example.showcaseapp.di

import com.example.showcaseapp.data.remote.AppCatalogApi
import com.example.showcaseapp.data.remote.MockBackendInterceptor
import com.example.showcaseapp.data.repository.AppCatalogRepositoryImpl
import com.example.showcaseapp.domain.repository.AppCatalogRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAppCatalogRepository(
        repository: AppCatalogRepositoryImpl
    ): AppCatalogRepository
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(mockBackendInterceptor: MockBackendInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(mockBackendInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://mock.rustore.local/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideAppCatalogApi(retrofit: Retrofit): AppCatalogApi =
        retrofit.create(AppCatalogApi::class.java)
}
