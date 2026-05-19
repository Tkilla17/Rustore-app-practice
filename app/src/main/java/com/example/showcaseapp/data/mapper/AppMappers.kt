package com.example.showcaseapp.data.mapper

import com.example.showcaseapp.data.remote.AppDto
import com.example.showcaseapp.data.remote.CategoryDto
import com.example.showcaseapp.domain.model.AppCategory
import com.example.showcaseapp.domain.model.AppInfo
import com.example.showcaseapp.domain.model.AppScreenshot
import com.example.showcaseapp.domain.model.CategorySummary

fun AppDto.toDomain(): AppInfo =
    AppInfo(
        id = id,
        name = name,
        developer = developer,
        category = AppCategory.fromTitle(category),
        shortDescription = shortDescription,
        fullDescription = fullDescription,
        ageRating = ageRating,
        iconGradient = iconGradient,
        screenshots = screenshots.map {
            AppScreenshot(
                id = it.id,
                title = it.title,
                accentColor = it.accentColor,
                backgroundColor = it.backgroundColor
            )
        },
        isPopular = popular
    )

fun CategoryDto.toDomain(): CategorySummary =
    CategorySummary(
        category = AppCategory.fromTitle(name),
        appsCount = count
    )
