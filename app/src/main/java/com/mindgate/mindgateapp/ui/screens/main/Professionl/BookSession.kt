package com.mindgate.mindgateapp.ui.screens.main.Professionl

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.East
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mindgate.mindgateapp.InputGrayColor
import com.mindgate.mindgateapp.accentColor
import com.mindgate.mindgateapp.greenColor
import com.mindgate.mindgateapp.med_font
import com.mindgate.mindgateapp.reg_font
import com.mindgate.mindgateapp.semibold_font
import com.mindgate.mindgateapp.ui.components.ActionButton
import com.mindgate.mindgateapp.viewmodels.ProfessionalViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun BookSessionScreen(
    vm : ProfessionalViewModel,
    modifier: Modifier
) {
    // State for inputs
    val professional = vm.selectedProfessional.value!!
    var noteText by remember { mutableStateOf("") }
    var selectedDateIndex by remember { mutableIntStateOf(0) }
    var selectedTimeIndex by remember { mutableIntStateOf(1) }

    var snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val dayFormatter = remember { DateTimeFormatter.ofPattern("EEE", Locale.getDefault()) }
    val today = remember { LocalDate.now() }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier,
        containerColor = Color.White,
        bottomBar = {
            // Footer with Price and Button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                // Session Info Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "50 min Session",
                        fontFamily = med_font,
                        color = Color.Gray.copy(0.8f),
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "₹${professional.price}",
                        fontFamily = semibold_font,
                        color = Color.Black.copy(0.7f),
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // The Requested Action Button
                ActionButton(
                    text = "Book Session",
                    placeholder = {
                        Icon(
                            Icons.Default.East,
                            contentDescription = null,
                            tint = Color.White, // Icon inside button should usually be white to contrast green
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    plcHldrRight = true,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 7.dp, horizontal = 10.dp)
                ) {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Proceed For Payment!")
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // 1. Header Texts
            Text(
                text = "Book Session",
                fontFamily = semibold_font,
                fontSize = 28.sp,
                color = accentColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Find the perfect time.",
                fontFamily = reg_font,
                fontSize = 14.sp,
                color = accentColor
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 2. Professional Summary Card
            ProfessionalSummaryCard(
                name = professional.name,
                type = professional.type,
                rating = professional.rating,
                imgUrl = professional.imgUrl
            )

            Spacer(modifier = Modifier.height(30.dp))

            // 3. Date & Time Selection
            Text(
                text = "Select a Date & Time", // Fixed typo "Data" to "Date"
                fontFamily = semibold_font,
                fontSize = 16.sp,
                color = accentColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dates Row
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(7) { index ->
                    val currentDate = today.plusDays(index.toLong())

                    DateChip(
                        day = currentDate.format(dayFormatter),
                        date = "${LocalDate.now().dayOfMonth + index}",
                        isSelected = selectedDateIndex == index,
                        onClick = { selectedDateIndex = index }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Times Row
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                val times = listOf("09:00 AM", "10:00 AM", "01:00 PM", "03:30 PM", "05:00 PM")
                items(times.size) { index ->
                    TimeChip(
                        time = times[index],
                        isSelected = selectedTimeIndex == index,
                        onClick = { selectedTimeIndex = index }
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 4. Note Input
            Text(
                text = "Before the Session Note.",
                fontFamily = semibold_font,
                fontSize = 16.sp,
                color = accentColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(InputGrayColor)
                    .padding(16.dp)
            ) {
                if (noteText.isEmpty()) {
                    Text(
                        text = "Write your thoughts....",
                        color = Color.Gray.copy(0.5f),
                        fontSize = 14.sp,
                        fontFamily = reg_font
                    )
                }
                BasicTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    textStyle = TextStyle(
                        fontFamily = reg_font,
                        fontSize = 14.sp,
                        color = Color.Black
                    ),
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// --- Component: Professional Summary Card ---
@Composable
fun ProfessionalSummaryCard(
    name: String,
    type: String,
    rating: Double,
    imgUrl: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
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
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$rating",
                        fontSize = 10.sp,
                        color = accentColor,
                        fontFamily = semibold_font
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(Icons.Default.Star, contentDescription = null, tint = accentColor, modifier = Modifier.size(10.dp))
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Down Arrow Button
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ArrowDownward,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// --- Component: Date Chip ---
@Composable
fun DateChip(day: String, date: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(60.dp)
            .height(70.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(if (isSelected) accentColor else InputGrayColor)
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = day,
            color = if (isSelected) Color.White.copy(0.7f) else Color.Gray,
            fontSize = 12.sp,
            fontFamily = reg_font
        )
        Text(
            text = date,
            color = if (isSelected) Color.White else Color.Black,
            fontSize = 16.sp,
            fontFamily = semibold_font
        )
    }
}

// --- Component: Time Chip ---
@Composable
fun TimeChip(time: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .background(if (isSelected) accentColor else InputGrayColor)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = time,
            color = if (isSelected) Color.White else Color.Black,
            fontSize = 13.sp,
            fontFamily = med_font
        )
    }
}

@Preview
@Composable
private fun BookSessionPrev() {
//    BookSessionScreen(
//    )
}