package com.mindgate.mindgateapp.ui.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.East
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindgate.mindgateapp.InputGrayColor
import com.mindgate.mindgateapp.R
import com.mindgate.mindgateapp.accentColor
import com.mindgate.mindgateapp.greenColor
import com.mindgate.mindgateapp.reg_font
import com.mindgate.mindgateapp.semibold_font
import com.mindgate.mindgateapp.ui.components.ActionButton

// Define colors based on the image if not already in your theme

@Composable
fun LoginPasswordScreen(
    modifier: Modifier = Modifier,
    onProceed: (String, String) -> Unit = { _, _ -> },
    onResetPassword: () -> Unit = {}
) {
    // State for inputs
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // or baseColor
    ) {
        // 1. Background Decoration (Bottom Shapes)
        // Assuming you have a drawable for the bottom shapes like in your previous code
        Image(
            painter = painterResource(id = R.drawable.bottom_graffiti), // Replace with your bottom shapes drawable
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            contentScale = ContentScale.FillWidth
        )

        // 2. Main Content
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 60.dp, bottom = 20.dp), // Adjust padding as needed
        ) {

            // --- Logo ---
            Image(
                painter = painterResource(id = R.drawable.mindgate_logo), // Replace with your logo resource
                contentDescription = "Logo",
                modifier = Modifier
                    .size(50.dp)
                    .padding(bottom = 20.dp)
            )

            // --- Header Text ---
            Text(
                text = "Login",
                fontFamily = semibold_font, // Using your font
                color = accentColor, // Use your greenColor
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 40.dp)
            )

            // --- Email Input ---
            InputLabel(text = "Username/Email",accentColor)
            CustomInputField(
                value = email,
                onValueChange = { email = it },
                placeholder = "examplemail@dot.com"
            )

            Spacer(modifier = Modifier.height(25.dp))

            // --- Password Input ---
            InputLabel(text = "Password", color = accentColor)
            CustomInputField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Your Password",
                isPassword = true
            )

            // Spacer to push the button down, but keep it reachable
            Spacer(modifier = Modifier.weight(1f))

            ActionButton(
                text ="Proceed",
                placeholder = {
                    Icon(Icons.Default.East, contentDescription = null, tint = greenColor)
                },
                plcHldrRight = true,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 7.dp, horizontal = 40.dp)
            ) {

            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- Footer (Forgot Password) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Forgot your Password? ",
                    fontFamily = reg_font,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Text(
                    text = "Reset",
                    fontFamily = semibold_font, // Bold font for action
                    color = accentColor,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onResetPassword() }
                )
            }

            // Extra spacing for bottom safe area if needed
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// --- Helper Composables ---

@Composable
fun InputLabel(text: String,color: Color) {
    Text(
        text = text,
        fontFamily = reg_font, // Using your font
        color = color, // Use your greenColor
        fontSize = 16.sp,
        modifier = Modifier.padding(bottom = 10.dp)
    )
}

@Composable
fun CustomInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)), // Rounded corners
        colors = TextFieldDefaults.colors(
            focusedContainerColor = InputGrayColor,
            unfocusedContainerColor = InputGrayColor,
            focusedIndicatorColor = Color.Transparent, // Remove underline
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = accentColor
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
private fun PasswordScreenView() {
    LoginPasswordScreen {  }
}