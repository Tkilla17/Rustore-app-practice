package com.example.showcaseapp.ui.showcase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.showcaseapp.R
import com.example.showcaseapp.ui.components.AppListItem
import com.example.showcaseapp.ui.components.BackButton
import com.example.showcaseapp.ui.components.EmptyState
import com.example.showcaseapp.ui.components.ErrorState
import com.example.showcaseapp.ui.components.LoadingState
import com.example.showcaseapp.ui.components.UiState
import com.example.showcaseapp.ui.components.localizedTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowcaseScreen(
    onAppClick: (String) -> Unit,
    onCategoriesClick: () -> Unit,
    onSearchClick: () -> Unit,
    onBack: () -> Unit,
    viewModel: ShowcaseViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.screen_apps)) },
                navigationIcon = {
                    if (viewModel.selectedCategory != null) {
                        BackButton(onClick = onBack)
                    }
                },
                actions = {
                    TextButton(onClick = onSearchClick) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                        Text(stringResource(R.string.screen_search))
                    }
                }
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
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    state.data.selectedCategory?.let { category ->
                        item {
                            Text(
                                text = category.localizedTitle(),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    item {
                        TextButton(onClick = onCategoriesClick) {
                            Text(stringResource(R.string.all_categories))
                        }
                    }

                    if (state.data.apps.isEmpty()) {
                        item {
                            EmptyState(
                                title = stringResource(R.string.empty_apps_title),
                                message = stringResource(R.string.empty_apps_message)
                            )
                        }
                    } else {
                        items(state.data.apps, key = { it.id }) { app ->
                            AppListItem(
                                app = app,
                                onClick = { onAppClick(app.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}
