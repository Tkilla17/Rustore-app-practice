package com.example.showcaseapp.ui.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.showcaseapp.ui.categories.CategoriesScreen
import com.example.showcaseapp.ui.detail.DetailScreen
import com.example.showcaseapp.ui.onboarding.OnboardingScreen
import com.example.showcaseapp.ui.screenshots.ScreenshotsScreen
import com.example.showcaseapp.ui.search.SearchScreen
import com.example.showcaseapp.ui.showcase.ShowcaseScreen

@Composable
fun ShowcaseApp(
    viewModel: AppViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val isOnboardingCompleted by viewModel.isOnboardingCompleted.collectAsStateWithLifecycle()
    val startDestination = if (isOnboardingCompleted) Routes.showcase() else Routes.ONBOARDING

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onContinue = {
                    viewModel.completeOnboarding()
                    navController.navigate(Routes.showcase()) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Routes.SHOWCASE,
            arguments = listOf(
                navArgument("category") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            ShowcaseScreen(
                onAppClick = { appId -> navController.navigate(Routes.detail(appId)) },
                onCategoriesClick = { navController.navigate(Routes.CATEGORIES) },
                onSearchClick = { navController.navigate(Routes.SEARCH) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.CATEGORIES) {
            CategoriesScreen(
                onBack = { navController.popBackStack() },
                onCategoryClick = { category ->
                    navController.navigate(Routes.showcase(category))
                }
            )
        }

        composable(Routes.SEARCH) {
            SearchScreen(
                onBack = { navController.popBackStack() },
                onAppClick = { appId -> navController.navigate(Routes.detail(appId)) }
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("appId") { type = NavType.StringType })
        ) {
            DetailScreen(
                onBack = { navController.popBackStack() },
                onScreenshotClick = { appId, index ->
                    navController.navigate(Routes.screenshots(appId, index))
                }
            )
        }

        composable(
            route = Routes.SCREENSHOTS,
            arguments = listOf(
                navArgument("appId") { type = NavType.StringType },
                navArgument("startIndex") { type = NavType.IntType }
            )
        ) {
            ScreenshotsScreen(onBack = { navController.popBackStack() })
        }
    }
}
