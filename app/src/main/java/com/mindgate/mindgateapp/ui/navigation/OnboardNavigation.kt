package com.mindgate.mindgateapp.ui.navigation

import android.app.Application
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
import com.mindgate.mindgateapp.ui.screens.onboarding.LoginPasswordScreen
import com.mindgate.mindgateapp.ui.screens.onboarding.LoginScreen
import com.mindgate.mindgateapp.ui.screens.onboarding.SignupScreen

fun NavGraphBuilder.onboardNavGraph(
    navController: NavHostController,
    modifier: Modifier,
    onRegisterClick : () -> Unit,
) {

    navigation(
        startDestination = OnboardScreens.LaunchScreen.route,
        route = RootRoutes.ONBOARD
    ) {

        composable(OnboardScreens.LaunchScreen.route) {
            LoginScreen(modifier = modifier, onPasswordLogin = {
                navController.navigate(OnboardScreens.PasswordScreen.route)
            }, onSignUpClick = {
                navController.navigate(OnboardScreens.SignupScreen.route)
            })
        }

        composable(OnboardScreens.PasswordScreen.route) {
            LoginPasswordScreen(
                modifier=modifier,
                onProceed = { email, password ->
                    // login logic
//                    navController.navigate(RootRoutes.MAIN) {
//                        popUpTo(RootRoutes.ONBOARD) {
//                            inclusive = true
//                        }
//                    }
                }
            )
        }

        composable (OnboardScreens.SignupScreen.route){
            SignupScreen(onRegister = {
                onRegisterClick()
            })
        }
    }
}

sealed class OnboardScreens(val route: String){
    object LaunchScreen: OnboardScreens("launch_screen")
    object PasswordScreen : OnboardScreens(route = "password_screen")
    object SignupScreen : OnboardScreens(route = "signup_screen")
}