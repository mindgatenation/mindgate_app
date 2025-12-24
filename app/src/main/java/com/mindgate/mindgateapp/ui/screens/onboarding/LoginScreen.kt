package com.mindgate.mindgateapp.ui.screens.onboarding

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.East
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.mindgate.mindgateapp.BuildConfig
import com.mindgate.mindgateapp.R
import com.mindgate.mindgateapp.accentColor
import com.mindgate.mindgateapp.baseColor
import com.mindgate.mindgateapp.greenColor
import com.mindgate.mindgateapp.med_font
import com.mindgate.mindgateapp.mindgate_logo
import com.mindgate.mindgateapp.reg_font
import com.mindgate.mindgateapp.semibold_font
import com.mindgate.mindgateapp.ui.components.ActionButton
import com.mindgate.mindgateapp.viewmodels.OnboardingViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier.padding(horizontal = 20.dp),
    vm: OnboardingViewModel= hiltViewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val credentialManager = CredentialManager.create(context)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(baseColor)
    ) {
        Image(
            painterResource(R.drawable.mid_graffiti),
            contentDescription = null,
            modifier = Modifier
                .matchParentSize()
                .align(Alignment.Center),
        )
        Image(
            painterResource(R.drawable.bottom_graffiti),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            contentScale = ContentScale.FillWidth
        )
        Column(modifier=modifier)
        {
            Row(
                modifier = Modifier
                    .padding(vertical = 15.dp)
                    .fillMaxWidth(),
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
                    color = Color.Black,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp,
                )
            }

            TextBanner(modifier = modifier)
            Image(
                painterResource(R.drawable.banner_img),
                contentDescription = "mid_banner",
                modifier = Modifier
                    .fillMaxWidth(0.66f)
                    .fillMaxHeight(0.35f)
//                    .height(220.dp)
                    .offset(y = -40.dp)
                    .zIndex(-1f)
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape)
                    ,
                contentScale = ContentScale.Crop
            )
            Text(
                text = "Find the right people and the right care from community to AI to expert support, thoughtfully guided for your well-being.",
                fontFamily = reg_font,
                color = Color.Black,
                fontSize = 11.sp,
                modifier= Modifier
                    .offset(y = -10.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .align(Alignment.CenterHorizontally)
                    .background(greenColor)
                    .padding(30.dp)
                    .width(250.dp),
                lineHeight = 19.sp
            )
            Spacer(modifier = Modifier.weight(2f))
            ActionButton(
                text ="Login with Password",
                placeholder = {
                    Icon(Icons.Default.East, contentDescription = null, tint = greenColor)
                },
                plcHldrRight = true,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 7.dp, horizontal = 10.dp)
            ) {

            }
            ActionButton(
                text ="Continue with Google",
                placeholder = {
                    Image(
                        painterResource(R.drawable.google_logo),
                        contentDescription = "google_logo",
                        modifier = Modifier
                            .size(27.dp)
                            .padding(end = 5.dp)
                    )
                },
                plcHldrRight = false,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 5.dp, horizontal = 5.dp)
            ) {
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setAutoSelectEnabled(false)
                    .setServerClientId(BuildConfig.WEB_CLIENT_ID)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                coroutineScope.launch {
                    try {


                        val result = credentialManager.getCredential(
                            request = request,
                            context = context
                        )

                        val credential = result.credential

                        if (
                            credential is CustomCredential &&
                            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                        ) {
                            val googleIdTokenCredential =
                                GoogleIdTokenCredential.createFrom(credential.data)

                            val idToken = googleIdTokenCredential.idToken

                            vm.signInWithGoogle(idToken)

                        } else {
                            Log.e("CredExp", "Unexpected credential type")
                        }
                    }
                    catch (e: GetCredentialCancellationException) {
                            // User cancelled the One Tap prompt — just log it or ignore
                            Log.d("CredExp", "User cancelled One Tap")
                    }
                    catch (e: NoCredentialException) {
                            // No credentials saved on the device — show a toast or fallback
                            Toast.makeText(context, "No credentials available. Please add a Google Account on the device or check you internet connection.", Toast.LENGTH_SHORT).show()
                    }
                    catch (e: GetCredentialException) {
                            // Other credential errors
                            Log.e("CredExp", "Other credential error", e)
                    }
                    catch (e: Exception) {
                            // Any other unexpected exception
                            Log.e("CredExp", "Unexpected error", e)
                    }
                }

            }

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {

                Text(
                    text = "New to Mindgate?",
                    fontFamily = reg_font,
                    color = Color.Black.copy(0.5f),
                    fontSize = 13.sp,
                )
                TextButton(onClick = {

                },
                    contentPadding = PaddingValues(0.dp),
                ) {
                    Text(
                        text = "Sign Up",
                        fontFamily = med_font,
                        color = accentColor,
                        fontSize = 13.sp,
                        textDecoration = TextDecoration.Underline,
                    )
                }
            }

        }
    }
}

@Composable
fun TextBanner(modifier: Modifier = Modifier) {
    Column(modifier= Modifier.padding(start = 10.dp)) {
        Row(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(30.dp))
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        color = accentColor, shape = CircleShape
                    )

            )
            Text(
                text = "Powered by AI",
                fontFamily = reg_font,
                color = accentColor,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 5.dp)
            )
        }
        Row(
            modifier = Modifier
                .padding(horizontal = 22.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Designed to guide you",
                fontFamily = reg_font,
                color = greenColor,
                fontSize = 22.sp,
                modifier = Modifier
//                    .padding(horizontal = 8.dp)
                    .background(
                        accentColor,
                        RoundedCornerShape(topEnd = 17.dp)
                    )
                    .padding(10.dp)
            )
        }
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth()
                .offset(y = -4.dp)
        ) {
            Text(
                text = "toward the support you",
                fontFamily = reg_font,
                color = greenColor,
                fontSize = 22.sp,
                modifier = Modifier
//                    .padding(horizontal = 8.dp)
                    .background(
                        accentColor,
                        RoundedCornerShape(17.dp)
                    )
                    .padding(vertical = 10.dp, horizontal = 17.dp)
            )
        }
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth()
                .offset(y = -6.dp)
        ) {
            Text(
                text = "deserve.",
                fontFamily = reg_font,
                color = greenColor,
                fontSize = 22.sp,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .background(
                        accentColor,
                        RoundedCornerShape(bottomStart = 17.dp)
                    )
                    .padding(10.dp)
            )
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    LoginScreen()
}