package com.example.showcaseapp.domain.model

data class AppInfo(
    val id: String,
    val name: String,
    val developer: String,
    val category: AppCategory,
    val shortDescription: String,
    val fullDescription: String,
    val ageRating: String,
    val iconGradient: List<String>,
    val screenshots: List<AppScreenshot>,
    val isPopular: Boolean
)

data class AppScreenshot(
    val id: String,
    val title: String,
    val accentColor: String,
    val backgroundColor: String
)
