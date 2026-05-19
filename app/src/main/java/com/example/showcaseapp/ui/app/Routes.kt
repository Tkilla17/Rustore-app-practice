package com.example.showcaseapp.ui.app

import android.net.Uri

object Routes {
    const val ONBOARDING = "onboarding"
    const val SHOWCASE = "showcase?category={category}"
    const val CATEGORIES = "categories"
    const val SEARCH = "search"
    const val DETAIL = "detail/{appId}"
    const val SCREENSHOTS = "screenshots/{appId}/{startIndex}"

    fun showcase(category: String? = null): String =
        "showcase?category=${Uri.encode(category.orEmpty())}"

    fun detail(appId: String): String =
        "detail/${Uri.encode(appId)}"

    fun screenshots(appId: String, startIndex: Int): String =
        "screenshots/${Uri.encode(appId)}/$startIndex"
}
