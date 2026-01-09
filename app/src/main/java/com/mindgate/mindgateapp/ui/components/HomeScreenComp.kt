package com.mindgate.mindgateapp.ui.components

import android.se.omapi.Session
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.East
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.mindgate.mindgateapp.accentColor
import com.mindgate.mindgateapp.data.dao.SessionType
import com.mindgate.mindgateapp.data.dao.Sessions
import com.mindgate.mindgateapp.greenColor
import com.mindgate.mindgateapp.reg_font
import com.mindgate.mindgateapp.semibold_font
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton
import com.zegocloud.uikit.service.defines.ZegoUIKitUser

@Composable
fun UpcomingSessionCard(sessions: List<Sessions>, onCallConnect: (ZegoSendCallInvitationButton, String) -> Unit, modifier: Modifier = Modifier) {
    Scaffold (modifier = Modifier.fillMaxWidth()){ innerPadding ->
        LazyRow(modifier = Modifier.padding(innerPadding),
            horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(sessions.size) { index ->
                UpcomingSessionItem(name = "test$index",
                    type = "profesional",
                    rating =  3.9,
                    imgUrl = "",
                    session = sessions[index]
                )
            }
        }
    }
}

// --- Component: Professional Summary Card ---
@Composable
fun UpcomingSessionItem(
    name: String,
    type: String,
    rating: Double,
    imgUrl: String,
    session : Sessions
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(40.dp)) // Large pill shape
            .background(greenColor)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Image
        AsyncImage(
            model = imgUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Color.White)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Info
        Column(verticalArrangement = Arrangement.Center) {
            Text(
                text = type,
                fontSize = 10.sp,
                color = accentColor.copy(0.6f),
                fontFamily = reg_font
            )
            Text(
                text = name,
                fontSize = 16.sp,
                color = accentColor,
                fontFamily = semibold_font
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Small Rating Pill
            Text(
                text = "This is a test session. Session Description",
                fontFamily = reg_font,
                fontSize = 11.sp,
                color = accentColor.copy(0.5f),
                modifier = Modifier.width(200.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Down Arrow Button
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.White)
                ,
            contentAlignment = Alignment.Center
        ) {
//            Icon(
//                imageVector = if (sessionType == SessionType.AUDIO_SESSION) Icons.Default.Call else
//                    Icons.Default.Videocam,
//                contentDescription = null,
//                tint = accentColor,
//                modifier = Modifier.size(20.dp)
//            )
            CallButton(
                isVideoCall = session.sessionType == SessionType.VIDEO_SESSION,
                session.professionEmail
            )
        }
    }
}

@Composable
fun CallButton(isVideoCall : Boolean,inviteEmail : String, modifier: Modifier = Modifier) {
    AndroidView(
        factory = {context ->
            val button = ZegoSendCallInvitationButton(context)
            button.setIsVideoCall(isVideoCall)
            button.resourceID = "zego_data"
            button
        }
    ){zegoButton ->
        if (inviteEmail.isNotEmpty()) {
            val targetUser = ZegoUIKitUser(inviteEmail, inviteEmail)
            zegoButton.setInvitees(listOf(targetUser))
        }

        zegoButton.setIsVideoCall(isVideoCall)

    }
}

@Preview
@Composable
private fun HomeCompPrev() {
//    UpcomingSessionCard(listOf(Sessions(
//        sessionId = "test_1",
//        sessionType = SessionType.VIDEO_SESSION,
//        professionEmail = "architanant5@gmail.com",
//        userEmail = "test.sorea@gmail.com",
//        callDuration = 1000L
//    ),
//        Sessions(
//            sessionId = "test_1",
//            sessionType = SessionType.VIDEO_SESSION,
//            professionEmail = "architanant5@gmail.com",
//            userEmail = "test.sorea@gmail.com",
//            callDuration = 1000L
//        )))
//    UpcomingSessionItem(
//        name = "lov",
//        type = "profesional",
//        rating =  3.9,
//        imgUrl = ""
//    )
}