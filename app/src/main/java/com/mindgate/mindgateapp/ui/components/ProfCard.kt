package com.mindgate.mindgateapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mindgate.mindgateapp.accentColor
import com.mindgate.mindgateapp.baseColor
import com.mindgate.mindgateapp.data.dao.Professional
import com.mindgate.mindgateapp.greenColor
import com.mindgate.mindgateapp.med_font
import com.mindgate.mindgateapp.reg_font
import com.mindgate.mindgateapp.semibold_font
import org.jetbrains.annotations.Async


@Composable
fun ProfessionalCard(
    professional: Professional,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min) // Allows children to measure correctly
            .clip(RoundedCornerShape(40.dp)) // Large rounded corners
            .background(greenColor.copy(0.5f))
            .clickable { onClick() }
            .padding(vertical = 20.dp, horizontal = 26.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- 1. LEFT SECTION: Image & Rating ---
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(end = 12.dp)
        ) {
            // Profile Image
            AsyncImage(
                // Replace with painterResource(id = R.drawable.your_placeholder)
                // or AsyncImage(model = professional.imgUrl, ...) for URL loading
                model = professional.imgUrl.ifBlank { "https://annemariesegal.com/wp-content/uploads/2017/04/adobestock_86346713-cropped-young-woman-in-suit.jpg?w=1680" },
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(55.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(2.dp, Color.White, CircleShape)
            )


            Spacer(modifier = Modifier.height(8.dp))

            // Rating Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${professional.rating}.0",
                        color = accentColor,
                        fontSize = 12.sp,
                        fontFamily = semibold_font
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }

        // --- 2. MIDDLE SECTION: Info ---
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = professional.type,
                color = accentColor.copy(alpha = 0.5f),
                fontSize = 10.sp,
                fontFamily = med_font
            )

//            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = professional.name,
                color = accentColor,
                fontSize = 15.sp,
                fontFamily = med_font
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = professional.description,
                color = accentColor.copy(alpha = 0.7f),
                fontSize = 11.sp,
                lineHeight = 14.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                fontFamily = reg_font
            )
        }

        // --- 3. RIGHT SECTION: Action ---
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            // Arrow Button
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable { onClick() }, // Explicit click for button feel
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Expand",
                    tint = accentColor,
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(-45f) // Rotates arrow to point North-East
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Expand for more",
                color = accentColor.copy(alpha = 0.5f),
                fontSize = 10.sp,
                fontFamily = med_font
            )
        }
    }
}

@Preview
@Composable
private fun PrevCardPrev() {
    ProfessionalCard(
        Professional(
            id = "1",
            name = "John Doe",
            type = "Psychologist",
            description = "Experienced psychologist with a focus on mental health.",
            imgUrl = "",
            rating = 4
        )
    )
}