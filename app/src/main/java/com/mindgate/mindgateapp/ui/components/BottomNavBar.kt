package com.mindgate.mindgateapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BubbleChart
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindgate.mindgateapp.accentColor
import com.mindgate.mindgateapp.greenColor
import com.mindgate.mindgateapp.semibold_font
import com.mindgate.mindgateapp.ui.navigation.MainScreens

// --- Colors based on your image ---
val NavContainerColor = greenColor // Light Mint Green
val NavItemColor = accentColor      // Dark Green (Icons/Text)
val NavSelectedBgColor = Color.White      // White pill background

// --- Data Class for Navigation Items ---
sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    // Replace these Icons with your painterResource(R.drawable.your_icon) later
    object Home : BottomNavItem(MainScreens.Home.route, Icons.Default.Home, "Home")
    object AI : BottomNavItem(MainScreens.AIScreen.route, Icons.Default.BubbleChart, "Talk to AI")
    object Professional : BottomNavItem(MainScreens.Professional.route, Icons.Default.SelfImprovement, "Professional")
    object Community : BottomNavItem(MainScreens.Community.route, Icons.Default.Group, "Community")
}

@Composable
fun MindgateBottomNavigation(
    currentRoute: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.AI,
        BottomNavItem.Professional,
        BottomNavItem.Community
    )

    // Main Container (The long green pill)
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .height(70.dp), // Adjust height as needed
        color = NavContainerColor,
        shape = RoundedCornerShape(50.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp), // Internal padding
            horizontalArrangement = Arrangement.SpaceBetween, // Spacing logic
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                MindgateNavItem(
                    item = item,
                    isSelected = currentRoute == item.route,
                    onItemClick = { onItemSelected(item.route) }
                )
            }
        }
    }
}

@Composable
fun MindgateNavItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onItemClick: () -> Unit
) {
    // The background shape changes based on selection
    val backgroundColor = if (isSelected) NavSelectedBgColor else Color.Transparent

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(onClick = onItemClick)
            .padding(vertical = 12.dp, horizontal = 16.dp), // Padding inside the white pill
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // --- ICON ---
            // Replace Icon() with Image() and painterResource if using custom drawables
            Icon(
                imageVector = item.icon,
                contentDescription = item.label,
                tint = NavItemColor,
                modifier = Modifier.size(24.dp)
            )

            // --- TEXT (Visible only when selected) ---
            AnimatedVisibility(visible = isSelected) {
                Row {
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = item.label,
                        color = NavItemColor,
                        fontSize = 14.sp,
                        maxLines = 1,
                        fontFamily = semibold_font
                    )
                }
            }
        }
    }
}

// --- Preview ---
@Preview(showBackground = true)
@Composable
fun PreviewNav() {
    var currentRoute by remember { mutableStateOf("home") }

    Box(modifier = Modifier.fillMaxSize().padding(top = 100.dp)) {
        MindgateBottomNavigation(
            currentRoute = currentRoute,
            onItemSelected = { currentRoute = it }
        )
    }
}