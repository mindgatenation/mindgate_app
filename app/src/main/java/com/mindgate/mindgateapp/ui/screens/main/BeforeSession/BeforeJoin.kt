package com.mindgate.mindgateapp.ui.screens.main.BeforeSession

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mindgate.mindgateapp.accentColor
import com.mindgate.mindgateapp.data.dao.SessionType
import com.mindgate.mindgateapp.reg_font
import com.mindgate.mindgateapp.semibold_font
import com.mindgate.mindgateapp.ui.components.CallButton
import com.mindgate.mindgateapp.ui.screens.main.Professionl.ProfessionalSummaryCard
import com.mindgate.mindgateapp.viewmodels.SessionViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import com.mindgate.mindgateapp.InputGrayColor
import com.mindgate.mindgateapp.baseColor
import com.mindgate.mindgateapp.med_font
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BeforeJoin(
    sessionId: String,
    vm: SessionViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    LaunchedEffect(sessionId) {
        vm.getSession(sessionId)
        vm.getProfessional()
    }

    val prof = vm.professional.collectAsState().value
    val session = vm.session.collectAsState().value

    // --- Helper for Duration ---
    fun formatDuration(minutes: Long): String {
        return if (minutes < 60) {
            "$minutes Mins"
        } else {
            val hours = minutes / 60.0
            // Format to 1 decimal place if needed, e.g., 1.5 Hours
            String.format(Locale.US, "%.1f Hours", hours).replace(".0", "")
        }
    }

    // --- Helper for Date/Time ---
    fun formatDateTime(millis: Long): Pair<String, String> {
        val date = Date(millis)
        val dayFormat = SimpleDateFormat("dd MMM yy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return Pair(dayFormat.format(date), timeFormat.format(date))
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp)) {
                Text(
                    text = "Overview",
                    fontSize = 28.sp,
                    fontFamily = semibold_font,
                    color = accentColor,
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Before you begin!",
                    fontSize = 15.sp,
                    color = accentColor,
                    lineHeight = 22.sp,
                    fontFamily = reg_font
                )
            }
        },
        bottomBar = {
            if (session != null) {
                // Bottom Bar Container
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp), // Lift from bottom
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // --- Round Call Button ---
                    Box(
                        modifier = Modifier
                            .size(70.dp) // Size of the circle
                            .clip(CircleShape)
                            .background(InputGrayColor)
                        , // Green background
                        contentAlignment = Alignment.Center
                    ) {
                        // 1. Zego Logic (Invisible but active)
                        CallButton(
                            isVideoCall = session.sessionType == SessionType.VIDEO_SESSION,
                            inviteEmail = session.professionEmail,
                            modifier = Modifier.fillMaxSize().padding(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Please wait atleast 5 minutes for the professional before starting the call",
                        fontFamily = reg_font,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 20.dp),
                        color = Color.Black.copy(0.4f),
                        textAlign = TextAlign.Center
                    )
                }
            }
            else {
                Text(
                    text = "Loading...",
                    fontSize = 20.sp,
                    fontFamily = semibold_font
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // 1. Professional Card
            if (prof != null) {
                ProfessionalSummaryCard(
                    prof.name,
                    prof.type,
                    prof.rating.toDouble(),
                    prof.imgUrl
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 2. Session Info (Date, Time, Duration)
            if (session != null) {
                Text(
                    text = "Session Details",
                    fontSize = 18.sp,
                    fontFamily = semibold_font,
                    color = accentColor
                )
                Spacer(modifier = Modifier.height(15.dp))

                val (dateStr, timeStr) = formatDateTime(session.dateTime)

                // Date & Time Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(50.dp))
                        .background(baseColor)
                        .padding(20.dp)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Date", fontSize = 12.sp, color = Color.Gray, fontFamily = reg_font)
                        Text(dateStr, fontSize = 16.sp, color = accentColor, fontFamily = semibold_font)
                    }
                    // Vertical Divider
                    Box(modifier = Modifier.width(1.dp).height(30.dp).background(Color.Gray.copy(0.3f)))

                    Column(horizontalAlignment = Alignment.End) {
                        Text("Time", fontSize = 12.sp, color = Color.Gray, fontFamily = reg_font)
                        Text(timeStr, fontSize = 16.sp, color = accentColor, fontFamily = semibold_font)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ){
                    DetailChip(
                        icon = Icons.Default.Schedule,
                        label = formatDuration(session.callDuration),
                        modifier = Modifier.weight(1f)
                    )

                    DetailChip(
                        icon = if (session.sessionType == SessionType.VIDEO_SESSION) Icons.Default.Videocam else Icons.Default.Call,
                        label = if (session.sessionType == SessionType.VIDEO_SESSION) "Video Call" else "Audio Call",
                        modifier = Modifier.weight(1f)
                    )

                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 3. Instructions
            Text(
                text = "Instructions",
                fontSize = 18.sp,
                fontFamily = semibold_font,
                color = accentColor
            )
            Spacer(modifier = Modifier.height(10.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InstructionItem("Ensure you are in a quiet environment.")
                InstructionItem("Check your internet connection.")
                InstructionItem("Please be respectful during the session.")
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}
@Composable
fun DetailChip(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(baseColor)
            .padding(20.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = accentColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            fontFamily = med_font,
            color = accentColor,
            fontSize = 14.sp
        )
    }
}

// Helper for Instructions
@Composable
fun InstructionItem(text: String) {
    Row() {
        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .size(6.dp)
                .background(accentColor.copy(0.6f), CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontFamily = reg_font,
            color = Color.Black.copy(0.7f),
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
    }
}