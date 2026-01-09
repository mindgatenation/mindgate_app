package com.mindgate.mindgateapp.ui.screens.main.Home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mindgate.mindgateapp.BuildConfig
import com.mindgate.mindgateapp.MainActivity
import com.mindgate.mindgateapp.accentColor
import com.mindgate.mindgateapp.med_font
import com.mindgate.mindgateapp.ui.components.UpcomingSessionCard
import com.mindgate.mindgateapp.ui.components.UpcomingSessionItem
import com.mindgate.mindgateapp.viewmodels.HomeViewModel
import com.zegocloud.uikit.service.defines.ZegoUIKitUser
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    vm : HomeViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            vm.setCurrSessions("")
        }
    }

    val context = LocalContext.current as MainActivity
    LaunchedEffect(Unit) {
        val currUserEmail = vm.getCurrUser()?.email
        currUserEmail?.let {
            Log.d("HomeScreen", "HomeScreen: $currUserEmail")
            context.initZegoInviteService(BuildConfig.ZEGOCLOUD_APP_ID.toLong(), BuildConfig.ZEGOCLOUD_APP_SIGN, currUserEmail, currUserEmail)
        }
    }
    val targetUserId by remember { mutableStateOf("") }
    val currSessions = vm.currSessions.collectAsState().value
    Box(modifier = modifier.fillMaxSize().background(Color.White)){
        UpcomingSessionCard(currSessions, onCallConnect = {button,profEmail->
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
        })

        Text(
            text = "Homescreen",
            color = accentColor,
            fontFamily = med_font,
            fontSize = 15.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }

//    Column(modifier = modifier.fillMaxSize().background(Color.White)) {
//        UpcomingSessionItem(
//        name = "lov",
//        type = "profesional",
//        rating =  3.9,
//        imgUrl = ""
//    )
//    }
}

@Preview
@Composable
private fun MainScreenPrev() {
    HomeScreen()
}