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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.mindgate.mindgateapp.ui.components.MindgateBottomNavigation
import com.mindgate.mindgateapp.ui.navigation.MainScreens
import com.mindgate.mindgateapp.ui.navigation.mainNavGraph
import com.mindgate.mindgateapp.ui.navigation.onboardNavGraph
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
            var startDest by remember { mutableStateOf(RootRoutes.ONBOARD) }
            var bottomSelection by remember { mutableStateOf(MainScreens.Home.route) }
            MindgateTheme {
                val coroutineScope = rememberCoroutineScope()
                val snackbarHostState = remember { SnackbarHostState() }
                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (startDest==RootRoutes.MAIN){
                            MindgateBottomNavigation(bottomSelection, {
                                bottomSelection = it
                                navController.navigate(it)
                            }, modifier = Modifier.padding(bottom = 10.dp))
                        }
                    }
                ) { innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination = startDest,
                    ) {
                        onboardNavGraph(navController,Modifier.padding(innerPadding)){
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Registration Clicked. Skipping to Home Screen")
                            }
                            navController.navigate(RootRoutes.MAIN){
                                popUpTo(RootRoutes.ONBOARD){
                                    inclusive = true
                                }
                            }
                            bottomSelection = MainScreens.Home.route
                            startDest = RootRoutes.MAIN
                        }
                        mainNavGraph(modifier = Modifier.padding(innerPadding))
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