package com.mindgate.mindgateapp.ui.screens.main.Home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mindgate.mindgateapp.BuildConfig
import com.mindgate.mindgateapp.MainActivity
import com.mindgate.mindgateapp.accentColor
import com.mindgate.mindgateapp.di.ZegoCallManager
import com.mindgate.mindgateapp.med_font
import com.mindgate.mindgateapp.ui.components.HomeTopBar
import com.mindgate.mindgateapp.ui.components.UpcomingSessionCard
import com.mindgate.mindgateapp.ui.components.UpcomingSessionItem
import com.mindgate.mindgateapp.viewmodels.HomeViewModel
import com.zegocloud.uikit.service.defines.ZegoUIKitUser
import kotlinx.coroutines.launch
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier, // Contains padding from Main Activity
    vm: HomeViewModel = hiltViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
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
            ) { }
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
            Spacer(modifier = Modifier.height(10.dp))
            UpcomingSessionCard(currSessions, onCallConnect = { button, profEmail ->
                Log.d("HomeScreen", "onCallConnect: $profEmail")
                if (targetUserId.isNotEmpty()) {
                    button.setInvitees(
                        mutableListOf(
                            ZegoUIKitUser(
                                profEmail, profEmail
                            )
                        )
                    )
                }
            }
            )
        }
    }
}

@Preview
@Composable
private fun MainScreenPrev() {
//    HomeScreen()
}