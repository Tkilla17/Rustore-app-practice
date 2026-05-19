package com.example.showcaseapp.ui.components

import androidx.compose.ui.graphics.Color

fun String.toComposeColor(): Color =
    runCatching { Color(android.graphics.Color.parseColor(this)) }
        .getOrDefault(Color(0xFF1B6EF3))
