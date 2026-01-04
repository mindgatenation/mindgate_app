package com.mindgate.mindgateapp.ui.screens.main.Home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.mindgate.mindgateapp.accentColor
import com.mindgate.mindgateapp.med_font

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Box(modifier = Modifier.fillMaxSize().background(Color.White)){
        Text(
            text = "Homescreen",
            color = accentColor,
            fontFamily = med_font,
            fontSize = 15.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Preview
@Composable
private fun MainScreenPrev() {
    HomeScreen()
}