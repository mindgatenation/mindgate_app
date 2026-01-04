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


fun NavGraphBuilder.mainNavGraph(
    navController : NavHostController,
    modifier: Modifier= Modifier
) {
    navigation(
        startDestination = MainScreens.Home.route,
        route = RootRoutes.MAIN
    ) {
        composable(MainScreens.Home.route) {
            HomeScreen()
        }

        composable (MainScreens.AIScreen.route){
            AIChatScreen(modifier)
        }
        composable(MainScreens.Professional.route) { backStackEntry ->
            // 1. Get the BackStackEntry of the parent graph (RootRoutes.MAIN)
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(RootRoutes.MAIN)
            }
            // 2. Init the ViewModel scoped to that parent
            val sharedViewModel = hiltViewModel<ProfessionalViewModel>(parentEntry)

            ProfessionalScreen(
                modifier = modifier,
                vm = sharedViewModel, // Pass the shared instance
                onBookClick = {
                    navController.navigate(MainScreens.BookSession.route)
                }
            )
        }

        // --- Book Session Screen ---
        composable(MainScreens.BookSession.route) { backStackEntry ->
            // 1. Get the SAME BackStackEntry (RootRoutes.MAIN)
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(RootRoutes.MAIN)
            }
            // 2. Get the SAME ViewModel instance
            val sharedViewModel = hiltViewModel<ProfessionalViewModel>(parentEntry)

            BookSessionScreen(
                vm = sharedViewModel ,// Pass the shared instance,
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



