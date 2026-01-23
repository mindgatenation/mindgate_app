package com.mindgate.mindgateapp.ui.screens.main.Home

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import com.mindgate.mindgateapp.accentColor
import com.mindgate.mindgateapp.data.dao.Sessions
import com.mindgate.mindgateapp.med_font
import com.mindgate.mindgateapp.reg_font
import com.mindgate.mindgateapp.ui.components.HomeTopBar
import com.mindgate.mindgateapp.ui.components.UpcomingSessionCard
import com.mindgate.mindgateapp.viewmodels.HomeViewModel
import com.permissionx.guolindev.PermissionX
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier, // Contains padding from Main Activity
    vm: HomeViewModel,
    onSessionClick : (Sessions) -> Unit,
    onProfileClick : () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        permissionHandling(context as FragmentActivity)
        coroutineScope.launch {
            vm.setCurrSessions("")
        }
    }
    val currUser =  vm.currentUser.collectAsState()

    val targetUserId by remember { mutableStateOf("") }
    val currSessions = vm.currSessions.collectAsState().value

    currUser.value?.let {
        vm.initZegoManager()
    }
    Scaffold(
        // 1. Apply the Parent Padding here!
        // This ensures the whole screen sits between the Top and Bottom bars of the Main Activity
        modifier = modifier,

        // 2. Prevent double status-bar spacing
        // Since 'modifier' likely already handles top/bottom padding,
        // we tell this inner scaffold NOT to add extra system bar spacing.
        contentWindowInsets = WindowInsets(0, 0, 0, 0),

        topBar = {
            HomeTopBar(
                // 3. Do NOT pass 'modifier' here.
                // Just use default Modifier or specific styling for the bar.
                modifier = Modifier,
                vm = vm
            ) {
                onProfileClick()
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Upcoming",
                fontFamily = med_font,
                fontSize = 27.sp,
                color = accentColor,
                modifier= Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            UpcomingSessionCard(currSessions, onSessionClick = {session ->
                onSessionClick(session)
            })
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Explore!",
                fontFamily = med_font,
                fontSize = 27.sp,
                color = accentColor,
                modifier= Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Find more ways to explore yourself!",
                fontFamily = reg_font,
                fontSize = 15.sp,
                color = accentColor,
                modifier= Modifier.padding(horizontal = 20.dp)
            )
        }
    }
}

fun permissionHandling(activityContext: FragmentActivity) {
    PermissionX.init(activityContext).permissions(Manifest.permission.SYSTEM_ALERT_WINDOW)
        .onExplainRequestReason { scope, deniedList ->
            val message =
                "We need your consent for the following permissions in order to use the offline call function properly"
            scope.showRequestReasonDialog(deniedList, message, "Allow", "Deny")
        }.request { allGranted, grantedList, deniedList -> }
}

@Preview
@Composable
private fun MainScreenPrev() {
//    HomeScreen()
}