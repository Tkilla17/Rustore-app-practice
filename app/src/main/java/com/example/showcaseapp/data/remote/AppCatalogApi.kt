package com.example.showcaseapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface AppCatalogApi {
    @GET("apps")
    suspend fun getApps(): List<AppDto>

    @GET("apps/{id}")
    suspend fun getApp(@Path("id") id: String): AppDto

    @GET("categories")
    suspend fun getCategories(): List<CategoryDto>

    @GET("popular")
    suspend fun getPopularApps(): List<AppDto>
}
