package com.mindgate.mindgateapp

import android.Manifest
import android.os.Bundle
import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.mindgate.mindgateapp.di.ZegoCallManager
import com.mindgate.mindgateapp.ui.components.MindgateBottomNavigation
import com.mindgate.mindgateapp.ui.navigation.MainScreens
import com.mindgate.mindgateapp.ui.navigation.mainNavGraph
import com.mindgate.mindgateapp.ui.navigation.onboardNavGraph
import com.mindgate.mindgateapp.ui.screens.main.Home.HomeScreen
import com.mindgate.mindgateapp.ui.screens.onboarding.LoginScreen
import com.mindgate.mindgateapp.ui.screens.waiting.LoadingScreen
import com.mindgate.mindgateapp.ui.theme.MindgateTheme
import com.mindgate.mindgateapp.viewmodels.OnboardingViewModel
import com.permissionx.guolindev.PermissionX
import com.zegocloud.uikit.ZegoUIKit
import com.zegocloud.uikit.internal.ZegoUIKitLanguage
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService
import com.zegocloud.uikit.prebuilt.call.core.invite.ZegoCallInvitationData
import com.zegocloud.uikit.prebuilt.call.event.CallEndListener
import com.zegocloud.uikit.prebuilt.call.event.ErrorEventsListener
import com.zegocloud.uikit.prebuilt.call.event.SignalPluginConnectListener
import com.zegocloud.uikit.prebuilt.call.event.ZegoCallEndReason
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig.generateDefaultConfig
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoTranslationText
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoUIKitPrebuiltCallConfigProvider
import dagger.hilt.android.AndroidEntryPoint
import im.zego.zim.enums.ZIMConnectionEvent
import im.zego.zim.enums.ZIMConnectionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
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
                        startDestination = RootRoutes.ONBOARD,
                        modifier = Modifier // Padding is applied inside specific graphs now
                    ) {

                        // 1. ONBOARDING GRAPH
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

                        // 2. NEW: LOADING SCREEN ROUTE
                        composable(RootRoutes.LOADING) {
                            // 1. Get the ViewModel
                            // Note: Since OnboardingViewModel holds the currentUser flow and Repository is likely Singleton,
                            // a new instance here is fine, OR use hiltViewModel(this@MainActivity) if you want to share.
                            val vm = hiltViewModel<OnboardingViewModel>()

                            // 2. Observe the User
                            val currentUser by vm.currentUser.collectAsState()

                            // 3. Render the UI
                            LoadingScreen(modifier = Modifier.padding(innerPadding),vm){
                                // --- CHANGE: Navigate to MAIN instead of LOADING ---
                                navController.navigate(RootRoutes.MAIN){
                                    popUpTo(RootRoutes.LOADING){inclusive = true}
                                }
                            }

                            // 4. Logic: Wait for User != null, then go to Main
                            LaunchedEffect(currentUser) {
                                // Only proceed if we have a valid user object
                                if (currentUser != null) {
                                    // Optional: Add a small delay so the user actually sees your cool animation
                                    // otherwise it might flash too fast if the internet is fast.
                                    delay(2000)

                                    navController.navigate(RootRoutes.MAIN) {
                                        popUpTo(RootRoutes.LOADING) { inclusive = true }
                                    }
                                }
                            }
                        }

                        // 3. MAIN GRAPH
                        mainNavGraph(
                            navController = navController,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
        permissionHandling(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ZegoUIKitPrebuiltCallService.unInit()
    }

    private fun permissionHandling(activityContext: FragmentActivity) {
        PermissionX.init(activityContext).permissions(Manifest.permission.SYSTEM_ALERT_WINDOW)
            .onExplainRequestReason { scope, deniedList ->
                val message =
                    "We need your consent for the following permissions in order to use the offline call function properly"
                scope.showRequestReasonDialog(deniedList, message, "Allow", "Deny")
            }.request { allGranted, grantedList, deniedList -> }
    }

}


object RootRoutes {
    const val ONBOARD = "onboard_graph"
    const val MAIN = "main_graph"
    const val LOADING = "loading_screen" // <-
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MindgateTheme {
    }
}