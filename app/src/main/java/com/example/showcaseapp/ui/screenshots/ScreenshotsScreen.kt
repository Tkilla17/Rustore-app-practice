package com.example.showcaseapp.ui.screenshots

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.showcaseapp.R
import com.example.showcaseapp.domain.model.AppScreenshot
import com.example.showcaseapp.ui.components.BackButton
import com.example.showcaseapp.ui.components.ErrorState
import com.example.showcaseapp.ui.components.LoadingState
import com.example.showcaseapp.ui.components.UiState
import com.example.showcaseapp.ui.components.toComposeColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenshotsScreen(
    onBack: () -> Unit,
    viewModel: ScreenshotsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = { BackButton(onClick = onBack) }
            )
        }
    ) { padding ->
        when (val state = uiState) {
            UiState.Loading -> LoadingState(modifier = Modifier.padding(padding))
            is UiState.Error -> ErrorState(
                message = state.message,
                onRetry = viewModel::retry,
                modifier = Modifier.padding(padding)
            )

            is UiState.Content -> PullToRefreshBox(
                isRefreshing = false,
                onRefresh = viewModel::refresh,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                val screenshots = state.data.app.screenshots
                val initialPage = state.data.startIndex.coerceIn(screenshots.indices)
                val pagerState = rememberPagerState(initialPage = initialPage) { screenshots.size }

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    FullscreenScreenshot(
                        screenshot = screenshots[page],
                        index = page,
                        total = screenshots.size
                    )
                }
            }
        }
    }
}

@Composable
private fun FullscreenScreenshot(
    screenshot: AppScreenshot,
    index: Int,
    total: Int
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(screenshot.backgroundColor.toComposeColor())
            .padding(28.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxWidth()
        ) {
            Text(
                text = screenshot.title,
                color = screenshot.accentColor.toComposeColor(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.82f)
                    .height(14.dp)
                    .clip(RoundedCornerShape(99.dp))
                    .background(screenshot.accentColor.toComposeColor().copy(alpha = 0.35f))
            )
            Spacer(modifier = Modifier.height(12.dp))

        }
        Text(
            text = stringResource(R.string.screenshot_counter_format, index + 1, total),
            modifier = Modifier.align(Alignment.BottomCenter),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold
        )
    }
}
