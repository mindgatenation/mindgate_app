package com.mindgate.mindgateapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.mindgate.mindgateapp.ui.components.MindgateBottomNavigation
import com.mindgate.mindgateapp.ui.navigation.MainScreens
import com.mindgate.mindgateapp.ui.navigation.mainNavGraph
import com.mindgate.mindgateapp.ui.navigation.onboardNavGraph
import com.mindgate.mindgateapp.ui.screens.main.Home.HomeScreen
import com.mindgate.mindgateapp.ui.screens.onboarding.LoginScreen
import com.mindgate.mindgateapp.ui.theme.MindgateTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val snackbarHostState = remember { SnackbarHostState() }

            // 1. OBSERVE ROUTE: Get the current route to determine visibility
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            MindgateTheme {
                // 2. DEFINE VISIBILITY: Create a list of routes where the BottomBar should show
                val bottomBarRoutes = remember {
                    listOf(
                        MainScreens.Home.route,
                        MainScreens.AIScreen.route,
                        MainScreens.Professional.route,
                        MainScreens.Community.route
                    )
                }

                // Only show bar if current route is in the list
                val showBottomBar = currentRoute in bottomBarRoutes

                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        // 3. CONDITIONALLY SHOW: No need for "startDest" state
                        if (showBottomBar) {
                            MindgateBottomNavigation(
                                currentRoute = currentRoute ?: MainScreens.Home.route,
                                onItemSelected = { route ->
                                    navController.navigate(route) {
                                        // 4. FIX POPUP LOGIC: Pop to the start of the MAIN graph,
                                        // not the absolute root of the app.
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
                    // 5. STATIC NAVHOST: Do not use state variables for startDestination
                    NavHost(
                        navController = navController,
                        startDestination = RootRoutes.ONBOARD, // Hardcode the initial string
                        // Apply padding here, or pass it down.
                        // Note: If BottomBar is floating, consider if you actually want this padding
                        // applied to the whole graph.
                        modifier = Modifier//.padding(innerPadding)
                    ) {

                        // --- ONBOARDING GRAPH ---
                        onboardNavGraph(navController, modifier = Modifier.padding(innerPadding)) {
                            // On registration done:
                            navController.navigate(RootRoutes.MAIN) {
                                // Clear onboarding from backstack so back button closes app
                                popUpTo(RootRoutes.ONBOARD) { inclusive = true }
                            }
                        }

                        mainNavGraph(navController, modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}


object RootRoutes {
    const val ONBOARD = "onboard_graph"
    const val MAIN = "main_graph"
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MindgateTheme {
    }
}