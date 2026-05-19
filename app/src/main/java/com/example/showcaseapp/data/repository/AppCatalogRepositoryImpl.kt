package com.example.showcaseapp.data.repository

import com.example.showcaseapp.data.mapper.toDomain
import com.example.showcaseapp.data.remote.AppCatalogApi
import com.example.showcaseapp.domain.model.AppCategory
import com.example.showcaseapp.domain.model.AppInfo
import com.example.showcaseapp.domain.model.CategorySummary
import com.example.showcaseapp.domain.repository.AppCatalogRepository
import javax.inject.Inject

class AppCatalogRepositoryImpl @Inject constructor(
    private val api: AppCatalogApi
) : AppCatalogRepository {

    override suspend fun getApps(category: AppCategory?): List<AppInfo> {
        val apps = api.getApps().map { it.toDomain() }
        return category?.let { selected -> apps.filter { it.category == selected } } ?: apps
    }

    override suspend fun getApp(id: String): AppInfo =
        api.getApp(id).toDomain()

    override suspend fun getCategories(): List<CategorySummary> =
        api.getCategories().map { it.toDomain() }

    override suspend fun searchApps(query: String): List<AppInfo> {
        val normalizedQuery = query.trim()
        if (normalizedQuery.isBlank()) return getPopularApps()
        return api.getApps()
            .map { it.toDomain() }
            .filter { app -> app.name.contains(normalizedQuery, ignoreCase = true) }
    }

    override suspend fun getPopularApps(): List<AppInfo> =
        api.getPopularApps().map { it.toDomain() }
}
