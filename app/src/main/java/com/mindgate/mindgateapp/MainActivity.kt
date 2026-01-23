package com.mindgate.mindgateapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mindgate.mindgateapp.ui.components.MindgateBottomNavigation
import com.mindgate.mindgateapp.ui.navigation.MainScreens
import com.mindgate.mindgateapp.ui.navigation.mainNavGraph
import com.mindgate.mindgateapp.ui.navigation.onboardNavGraph
import com.mindgate.mindgateapp.ui.screens.waiting.LoadingScreen
import com.mindgate.mindgateapp.ui.theme.MindgateTheme
import com.mindgate.mindgateapp.viewmodels.OnboardingViewModel
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val snackbarHostState = remember { SnackbarHostState() }
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route


            MindgateTheme {
                val bottomBarRoutes = remember {
                    listOf(
                        MainScreens.Home.route,
                        MainScreens.AIScreen.route,
                        MainScreens.Professional.route,
                        MainScreens.Community.route
                    )
                }
                val vm = hiltViewModel<OnboardingViewModel>()
                val currentUser by vm.currentUser.collectAsState()

                val showBottomBar = currentRoute in bottomBarRoutes

                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showBottomBar) {
                            MindgateBottomNavigation(
                                currentRoute = currentRoute ?: MainScreens.Home.route,
                                onItemSelected = { route ->
                                    navController.navigate(route) {
                                        popUpTo(MainScreens.Home.route) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                modifier = Modifier.padding(bottom = 10.dp)
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = when(currentUser){
                            null -> RootRoutes.ONBOARD
                            else -> RootRoutes.MAIN
                        },
                        modifier = Modifier
                    ) {

                        onboardNavGraph(
                            navController = navController,
                            modifier = Modifier.padding(innerPadding),
                            onRegisterClick = {

                            }, onSuccessLogin = {
                                navController.navigate(RootRoutes.LOADING) {
                                    // Clear onboarding from backstack
                                    popUpTo(RootRoutes.ONBOARD) { inclusive = true }
                                }
                            }
                        )

                        composable(RootRoutes.LOADING) {
                            LoadingScreen(modifier = Modifier.padding(innerPadding))
                            LaunchedEffect(currentUser) {
                                if (currentUser != null) {
                                    delay(2000)
                                    navController.navigate(RootRoutes.MAIN) {
                                        popUpTo(RootRoutes.LOADING) { inclusive = true }
                                    }
                                }
                            }
                        }
                        mainNavGraph(
                            navController = navController,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ZegoUIKitPrebuiltCallService.unInit()
    }
}


object RootRoutes {
    const val ONBOARD = "onboard_graph"
    const val MAIN = "main_graph"
    const val LOADING = "loading_screen"
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MindgateTheme {
    }
}