package com.example.showcaseapp.domain.repository

import com.example.showcaseapp.domain.model.AppCategory
import com.example.showcaseapp.domain.model.AppInfo
import com.example.showcaseapp.domain.model.CategorySummary

interface AppCatalogRepository {
    suspend fun getApps(category: AppCategory? = null): List<AppInfo>
    suspend fun getApp(id: String): AppInfo
    suspend fun getCategories(): List<CategorySummary>
    suspend fun searchApps(query: String): List<AppInfo>
    suspend fun getPopularApps(): List<AppInfo>
}
