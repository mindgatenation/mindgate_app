package com.mindgate.mindgateapp.ui.navigation

import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.mindgate.mindgateapp.RootRoutes
import com.mindgate.mindgateapp.ui.screens.main.AIChat.AIChatScreen
import com.mindgate.mindgateapp.ui.screens.main.Community.CommunityScreen
import com.mindgate.mindgateapp.ui.screens.main.Home.HomeScreen
import com.mindgate.mindgateapp.ui.screens.main.Professionl.BookSessionScreen
import com.mindgate.mindgateapp.ui.screens.main.Professionl.ProfessionalScreen
import com.mindgate.mindgateapp.viewmodels.ProfessionalViewModel
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.compose.navigation
import com.mindgate.mindgateapp.di.ZegoCallManager


fun NavGraphBuilder.mainNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // 1. Define the visual order of your Bottom Nav tabs
    val tabOrder = listOf(
        MainScreens.Home.route,
        MainScreens.AIScreen.route,
        MainScreens.Professional.route,
        MainScreens.Community.route
    )

    navigation(
        startDestination = MainScreens.Home.route,
        route = RootRoutes.MAIN,

        // 2. Logic for New Screen entering
        enterTransition = {
            val initialIndex = tabOrder.indexOf(initialState.destination.route)
            val targetIndex = tabOrder.indexOf(targetState.destination.route)

            if (initialIndex != -1 && targetIndex != -1) {
                // It's a tab switch
                if (targetIndex > initialIndex) {
                    // Going Right -> Slide in from Right
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(200))
                } else {
                    // Going Left -> Slide in from Left
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(200))
                }
            } else {
                // Not a tab switch (e.g. Opening BookSession) -> Fade In
                fadeIn(tween(200))
            }
        },

        // 3. Logic for Old Screen leaving
        exitTransition = {
            val initialIndex = tabOrder.indexOf(initialState.destination.route)
            val targetIndex = tabOrder.indexOf(targetState.destination.route)

            if (initialIndex != -1 && targetIndex != -1) {
                // It's a tab switch
                if (targetIndex > initialIndex) {
                    // Going Right -> Old slides out to Left
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(200))
                } else {
                    // Going Left -> Old slides out to Right
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(200))
                }
            } else {
                // Not a tab switch -> Fade Out
                fadeOut(tween(200))
            }
        }
    ) {
        composable(MainScreens.Home.route) {
            HomeScreen(modifier)
        }

        composable(MainScreens.AIScreen.route) {
            AIChatScreen(modifier)
        }

        composable(MainScreens.Professional.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(RootRoutes.MAIN)
            }
            val sharedViewModel = hiltViewModel<ProfessionalViewModel>(parentEntry)

            ProfessionalScreen(
                modifier = modifier,
                vm = sharedViewModel,
                onBookClick = {
                    navController.navigate(MainScreens.BookSession.route)
                }
            )
        }

        // --- Book Session Screen ---
        // Note: Since this route isn't in 'tabOrder', it will use the Fade animation defined in 'else' block
        composable(MainScreens.BookSession.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(RootRoutes.MAIN)
            }
            val sharedViewModel = hiltViewModel<ProfessionalViewModel>(parentEntry)

            BookSessionScreen(
                vm = sharedViewModel,
                modifier = modifier
            )
        }

        composable(MainScreens.Community.route) {
            CommunityScreen()
        }
    }
}

sealed class MainScreens(val route: String){
    object Home : MainScreens("home")
    object AIScreen : MainScreens("ai_screen")
    object Professional : MainScreens("professional")
    object Community : MainScreens("community")
    object BookSession : MainScreens("book_session")
}



