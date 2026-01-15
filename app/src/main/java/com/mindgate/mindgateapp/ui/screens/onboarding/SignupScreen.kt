package com.mindgate.mindgateapp.ui.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mindgate.mindgateapp.reg_font
import com.mindgate.mindgateapp.semibold_font
import java.text.SimpleDateFormat
import java.util.*
import com.mindgate.mindgateapp.R
import com.mindgate.mindgateapp.greenColor
import com.mindgate.mindgateapp.ui.components.ActionButton
import com.mindgate.mindgateapp.viewmodels.OnboardingViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    vm : OnboardingViewModel = hiltViewModel(),
    onGoogleConnect: () -> Unit = {},
    onRegister: () -> Unit = {}
) {
    // --- State Variables ---
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("User") }

    // Date Picker State
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }

    // Colors
    val inputBgColor = Color(0xFFF3F4F6)
    val activeGreen = Color(0xFF006400) // Your greenColor

    val googleEmail by vm.userEmail.collectAsState()

    LaunchedEffect(googleEmail) {
        googleEmail?.let {
            email = it // Auto-fill the text box
        }
    }

    // Determine if the field is locked (read-only)
    val isEmailLocked = googleEmail != null



    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->

        // Use a Box to layer the background image behind the form
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Respect system bars
        ) {
            // 1. Background Decoration (Bottom Graffiti)
            Image(
                painter = painterResource(id = R.drawable.bottom_graffiti),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                contentScale = ContentScale.FillWidth
            )

            // 2. Main Scrollable Form
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
                    .padding(top = 20.dp, bottom = 20.dp)
            ) {

                // --- Header ---
                Spacer(modifier = Modifier.height(20.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.mindgate_logo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Sign Up",
                        fontFamily = semibold_font,
                        color = activeGreen,
                        fontSize = 28.sp
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                // --- Username ---
                InputLabel(text = "Username", color = activeGreen)
                CustomInputField(
                    value = username,
                    onValueChange = { username = it },
                    placeholder = "Create a username",
                    bgColor = inputBgColor
                )

                Spacer(modifier = Modifier.height(20.dp))

                // --- Email & Google Button ---
                InputLabel(text = "Email", color = activeGreen)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Email Field
                    Box(modifier = Modifier.weight(1f)) {
                        CustomInputField(
                            value = email,
                            onValueChange = { email = it },
                            placeholder = "email@domain.com",
                            bgColor = inputBgColor,
                            enabled = !isEmailLocked
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // Google Icon Button
                    Box(
                        modifier = Modifier
                            .size(56.dp) // Height matches text field
                            .clip(RoundedCornerShape(12.dp))
                            .background(inputBgColor)
                            .clickable {
                                vm.signInWithGoogle(context)
                                onGoogleConnect()
                            }
                            .border(1.dp, Color.LightGray.copy(0.5f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.google_logo),
                            contentDescription = "Connect with Google",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // --- Password ---
                InputLabel(text = "Password", color = activeGreen)
                CustomInputField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Create a password",
                    isPassword = true,
                    bgColor = inputBgColor
                )

                Spacer(modifier = Modifier.height(20.dp))

                // --- Date of Birth ---
                InputLabel(text = "Date of Birth", color = activeGreen)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(inputBgColor)
                        .clickable { showDatePicker = true }
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (dob.isEmpty()) "DD / MM / YYYY" else dob,
                            color = if (dob.isEmpty()) Color.Gray.copy(0.6f) else Color.Black,
                            fontFamily = reg_font,
                            fontSize = 15.sp
                        )
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = activeGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(25.dp))

                // --- Role Selector ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(40.dp))
                        .background(inputBgColor)
                        .padding(4.dp)
                ) {
                    RoleButton(
                        text = "User",
                        isSelected = selectedRole == "User",
                        modifier = Modifier.weight(1f),
                        activeColor = activeGreen,
                        onClick = { selectedRole = "User" }
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    RoleButton(
                        text = "Professional",
                        isSelected = selectedRole == "Professional",
                        modifier = Modifier.weight(1f),
                        activeColor = activeGreen,
                        onClick = { selectedRole = "Professional" }
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                ActionButton(
                    text = "Register",
                    placeholder = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = greenColor
                        )
                    },
                    plcHldrRight = true,
                    modifier = Modifier
                        .padding(vertical = 5.dp, horizontal = 15.dp )
                        .align(Alignment.CenterHorizontally)
                ) {
                    onRegister()
                }
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }

    // --- Date Picker Dialog Logic ---
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            dob = formatter.format(Date(millis))
                        }
                        showDatePicker = false
                    }
                ) { Text("OK", color = activeGreen) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = activeGreen,
                    todayDateBorderColor = activeGreen,
                    todayContentColor = activeGreen
                )
            )
        }
    }
}

@Composable
fun RoleButton(
    text: String,
    isSelected: Boolean,
    modifier: Modifier,
    activeColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(40.dp))
            .background(if (isSelected) activeColor else Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontFamily = if (isSelected) semibold_font else reg_font,
            color = if (isSelected) Color.White else Color.Gray,
            fontSize = 14.sp
        )
    }
}

@Composable
fun CustomInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    bgColor: Color,
    isPassword: Boolean = false,
    enabled : Boolean = true
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = bgColor,
            unfocusedContainerColor = bgColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color(0xFF006400)
        ),
        placeholder = {
            Text(
                text = placeholder,
                color = Color.Gray.copy(alpha = 0.6f),
                fontFamily = reg_font,
                fontSize = 14.sp
            )
        },
        textStyle = TextStyle(
            fontFamily = reg_font,
            fontSize = 15.sp,
            color = Color.Black
        ),
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
    )
}

@Preview
@Composable
private fun SignupScreenPrev() {
//    SignupScreen()
}