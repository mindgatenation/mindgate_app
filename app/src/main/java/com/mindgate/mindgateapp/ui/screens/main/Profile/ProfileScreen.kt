package com.mindgate.mindgateapp.ui.screens.main.Profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mindgate.mindgateapp.InputGrayColor
import com.mindgate.mindgateapp.accentColor
import com.mindgate.mindgateapp.viewmodels.HomeViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    vm: HomeViewModel = hiltViewModel(),
    onSignOut: () -> Unit = {}
) {
    // Collect the user state
    val user by vm.currentUser.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // --- Header ---
            Text(
                text = "Profile",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )

            Spacer(modifier = Modifier.height(50.dp))

            // --- Profile Picture ---
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(5.dp, accentColor, CircleShape)
                    .background(InputGrayColor),
                contentAlignment = Alignment.Center
            ) {
                if (user?.profile_pic != null) {
                    AsyncImage(
                        model = user?.profile_pic,
                        contentDescription = "Profile Picture",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Fallback Icon if no image
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(60.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Name ---
            Text(
                text = user?.name ?: "User",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            // --- Email (Optional but looks good) ---
            Text(
                text = user?.email ?: "",
                fontSize = 14.sp,
                color = Color.Gray
            )

            // --- Spacer to push button to bottom ---
            Spacer(modifier = Modifier.weight(1f))

            // --- Sign Out Button ---
            Button(
                onClick = {
                    // 1. Call VM to clear data
                    // vm.signOut()

                    // 2. Navigate away
                    onSignOut()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFEBEE), // Light Red background
                    contentColor = Color(0xFFD32F2F)    // Red Text
                ),
                shape = RoundedCornerShape(50.dp),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Sign Out",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}