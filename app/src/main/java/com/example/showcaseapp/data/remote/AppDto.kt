package com.example.showcaseapp.data.remote

data class AppDto(
    val id: String,
    val name: String,
    val developer: String,
    val category: String,
    val shortDescription: String,
    val fullDescription: String,
    val ageRating: String,
    val iconGradient: List<String>,
    val screenshots: List<ScreenshotDto>,
    val popular: Boolean
)

data class ScreenshotDto(
    val id: String,
    val title: String,
    val accentColor: String,
    val backgroundColor: String
)

data class CategoryDto(
    val name: String,
    val count: Int
)
