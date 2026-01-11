package com.mindgate.mindgateapp.ui.screens.waiting

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import com.mindgate.mindgateapp.R
import com.mindgate.mindgateapp.accentColor
import com.mindgate.mindgateapp.med_font
import com.mindgate.mindgateapp.viewmodels.LoginState
import com.mindgate.mindgateapp.viewmodels.OnboardingViewModel

// Reusing your colors

@Composable
fun LoadingScreen(
    modifier : Modifier,
    vm: OnboardingViewModel ,
    onLoginSuccess: () -> Unit
) {
    // List of messages to cycle through
    val loadingMessages = listOf(
        "Verifying credentials...",
        "Setting up your safe space...",
        "Curating professionals...",
        "Almost there..."
    )

    var messageIndex by remember { mutableIntStateOf(0) }

    val loginState by vm.loginState.collectAsState() // Assuming you added this from previous advice

    LaunchedEffect(loginState) {
        if (loginState is LoginState.Success) {
            onLoginSuccess() // This takes us to RootRoutes.LOADING
        }
    }

    // Cycle through messages every 2 seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            messageIndex = (messageIndex + 1) % loadingMessages.size
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // --- 1. Background Graffiti (Consistency) ---
        Image(
            painter = painterResource(id = R.drawable.bottom_graffiti), // Make sure you have this
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            contentScale = ContentScale.FillWidth
        )

        // --- 2. Center Content ---
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // The Pulsing Logo
            PulsingLogo()

            Spacer(modifier = Modifier.height(40.dp))

            // The Animated Text
            AnimatedContent(
                targetState = loadingMessages[messageIndex],
                transitionSpec = {
                    (fadeIn(animationSpec = tween(600)) + slideInVertically { it / 2 })
                        .togetherWith(fadeOut(animationSpec = tween(600)) + slideOutVertically { -it / 2 })
                },
                label = "loading_text"
            ) { targetText ->
                Text(
                    text = targetText,
                    color = accentColor,
                    fontSize = 16.sp,
                     fontFamily = med_font // Use your font here
                )
            }
        }
    }
}

@Composable
fun PulsingLogo() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")

    // Animate Scale (Breathing effect)
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    // Animate Alpha (Ripple effect)
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "alpha"
    )

    Box(contentAlignment = Alignment.Center) {
        // Outer Ripple Circle 1
        Box(
            modifier = Modifier
                .size(120.dp)
                .scale(scale * 1.2f)
                .alpha(alpha)
                .background(accentColor, CircleShape)
        )

        // Outer Ripple Circle 2 (Delayed/Smaller)
        Box(
            modifier = Modifier
                .size(120.dp)
                .scale(scale)
                .alpha(0.1f) // Constant faint glow
                .background(accentColor, CircleShape)
        )

        // The Logo Container
        Box(
            modifier = Modifier
                .size(100.dp)
                .scale(1f) // Keep logo size relatively stable or pulse slightly
                .clip(CircleShape)
                .background(Color.White)
                .padding(20.dp), // Padding inside the white circle
            contentAlignment = Alignment.Center
        ) {
            // Replace with your actual Mindgate Logo
            Image(
                painter = painterResource(id = R.drawable.mindgate_logo),
                contentDescription = "Logo",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview
@Composable
private fun LoginScreenPrev() {
//    LoadingScreen()
}