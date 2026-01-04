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
import androidx.compose.material.icons.filled.East
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
                        text = "${professional.rating}",
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

@Composable
fun ProfessionalDetailsContent(
    professional: Professional,
    onBookClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 30.dp), // Padding for bottom safe area
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Profile Image ---
        AsyncImage(
            model = professional.imgUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(1.dp, Color.Gray.copy(0.2f), CircleShape)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- Name & Type ---
        Text(
            text = professional.name,
            fontFamily = semibold_font,
            fontSize = 22.sp,
            color = accentColor
        )
        Text(
            text = professional.type,
            fontFamily = reg_font,
            fontSize = 12.sp,
            color = accentColor.copy(0.7f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // --- Rating ---
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "${professional.rating}/5",
                fontFamily = semibold_font,
                fontSize = 14.sp,
                color = accentColor
            )
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(14.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- Tags (Pills) ---
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            professional.tags.forEach { tag ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(greenColor) // Light green bg
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = tag,
                        color = accentColor,
                        fontFamily = med_font,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(25.dp))

        // --- About Section ---
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "About",
                fontFamily = semibold_font,
                fontSize = 12.sp,
                color = Color.Black.copy(0.5f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = professional.description, // Or use your lorem ipsum text
                fontFamily = med_font,
                fontSize = 13.sp,
                color = Color.Black.copy(0.7f),
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        // --- Price ---
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "₹${professional.price}/Session",
                fontFamily = semibold_font,
                fontSize = 15.sp,
                color = Color.Black.copy(0.7f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        ActionButton(
            text = "Book Session",
            placeholder = {
                Icon(
                    Icons.Default.East,
                    contentDescription = null,
                    tint = greenColor,
                    modifier = Modifier.size(20.dp)
                )
            },
            plcHldrRight = true,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 7.dp, horizontal = 10.dp)
        ) {onBookClick() }
    }
}

@Preview( showBackground = true)
@Composable
private fun PrevCardPrev() {
    ProfessionalDetailsContent (
        Professional(
            id = "1",
            name = "John Doe",
            type = "Psychologist",
            description = "Experienced psychologist with a focus on mental health.",
            imgUrl = "",
            rating = 4.0,
            price = 100,
            tags = listOf("google")
        ),
        {}
    )
}