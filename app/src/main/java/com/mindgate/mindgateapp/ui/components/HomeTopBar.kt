package com.mindgate.mindgateapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mindgate.mindgateapp.R
import com.mindgate.mindgateapp.accentColor
import com.mindgate.mindgateapp.mindgate_logo
import com.mindgate.mindgateapp.reg_font
import com.mindgate.mindgateapp.semibold_font
import com.mindgate.mindgateapp.viewmodels.HomeViewModel
import java.time.LocalDateTime

@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    vm : HomeViewModel,
    onProfileClick : () -> Unit
) {
    val currUser = vm.currentUser.collectAsState()
    val greetingText = when(LocalDateTime.now().hour){
        in 0..11 -> "Good Morning"
        in 12..16 -> "Good Afternoon"
        else -> "Good Evening"
    }
    Box(modifier = Modifier.fillMaxWidth()) {
        Image(
            painterResource(R.drawable.mid_graffiti),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomEnd)
        )
        Column(
            modifier = modifier
                .fillMaxWidth() // Ensure row fills width to push items apart
                .padding(
                    horizontal = 20.dp,
                    vertical = 10.dp
                ), // Added vertical padding for spacing
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painterResource(mindgate_logo),
                    contentDescription = "mindgate_logo",
                    modifier = Modifier.padding(end = 5.dp)
                )
                Text(
                    text = "MINDGATE",
                    fontFamily = semibold_font,
                    color = accentColor,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp,
                )

            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${greetingText},\n${currUser.value?.name?.split(" ")[0]}",
                    fontFamily = reg_font,
                    color = accentColor,
                    fontSize = 18.sp,
                )
                AsyncImage(
                    model = currUser.value?.profile_pic,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(50.dp)
                        .border(
                            width = 4.dp,
                            color = accentColor,//.copy(0.7f),
                            shape = RoundedCornerShape(50.dp)
                        )
                        .clip(RoundedCornerShape(50.dp))
                        .clickable {
                            onProfileClick()
                        }, // Clip image to circle
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
}