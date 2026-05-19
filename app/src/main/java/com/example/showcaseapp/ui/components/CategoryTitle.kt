package com.example.showcaseapp.ui.components

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.showcaseapp.R
import com.example.showcaseapp.domain.model.AppCategory

@StringRes
fun AppCategory.titleRes(): Int =
    when (this) {
        AppCategory.FINANCE -> R.string.category_finance
        AppCategory.TOOLS -> R.string.category_tools
        AppCategory.GAMES -> R.string.category_games
        AppCategory.GOVERNMENT -> R.string.category_government
        AppCategory.TRANSPORT -> R.string.category_transport
    }

@Composable
fun AppCategory.localizedTitle(): String =
    stringResource(titleRes())
