package com.mindgate.mindgateapp.ui.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.mindgate.mindgateapp.RootRoutes
import com.mindgate.mindgateapp.ui.screens.main.AIChatScreen
import com.mindgate.mindgateapp.ui.screens.main.CommunityScreen
import com.mindgate.mindgateapp.ui.screens.main.HomeScreen
import com.mindgate.mindgateapp.ui.screens.main.ProfessionalScreen
import com.mindgate.mindgateapp.ui.screens.onboarding.LoginPasswordScreen
import com.mindgate.mindgateapp.ui.screens.onboarding.LoginScreen


fun NavGraphBuilder.mainNavGraph(
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
        composable(MainScreens.Professional.route) {
            ProfessionalScreen(modifier)
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
}



