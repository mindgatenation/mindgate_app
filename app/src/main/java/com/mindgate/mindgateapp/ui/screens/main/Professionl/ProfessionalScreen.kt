package com.mindgate.mindgateapp.ui.screens.main.Professionl
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindgate.mindgateapp.accentColor
import com.mindgate.mindgateapp.data.dao.Professional
import com.mindgate.mindgateapp.med_font
import com.mindgate.mindgateapp.reg_font
import com.mindgate.mindgateapp.semibold_font
import com.mindgate.mindgateapp.ui.components.ProfessionalCard
import com.mindgate.mindgateapp.ui.components.ProfessionalDetailsContent
import com.mindgate.mindgateapp.viewmodels.ProfessionalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfessionalScreen(
    modifier: Modifier = Modifier,
    onBookClick: () -> Unit = {},
    vm : ProfessionalViewModel
) {
    var searchText by remember { mutableStateOf("") }
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }

    // --- Bottom Sheet State ---
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }
    // Store the clicked professional so the sheet knows who to display
    var selectedProfessional by remember { mutableStateOf<Professional?>(null) }

    // Dummy Data
    val sampleProfessional = Professional(
        id = "1",
        name = "Sarah Black",
        type = "Physiologists",
        description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.",
        imgUrl = "https://annemariesegal.com/wp-content/uploads/2017/04/adobestock_86346713-cropped-young-woman-in-suit.jpg?w=1680",
        rating = 4.0,
        price = 499,
        tags = listOf("Anxiety", "Stress", "Relationship")
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {
                Text(
                    text = "Professionals",
                    fontSize = 28.sp,
                    fontFamily = semibold_font,
                    color = accentColor,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Find the perfect match for your need,\nin the way you feel comfortable.",
                    fontSize = 15.sp,
                    color = accentColor,
                    lineHeight = 22.sp,
                    fontFamily = reg_font
                )
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .padding(WindowInsets.navigationBars.asPaddingValues())
                    .fillMaxWidth()
                ,
                contentAlignment = Alignment.Center
            ) {
                FilterSortBar()
            }
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                SearchBar(
                    value = searchText,
                    onValueChange = { searchText = it }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CategoryButton(
                        text = "Active Listeners",
                        isSelected = selectedCategoryIndex == 0,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedCategoryIndex = 0 }
                    )

                    CategoryButton(
                        text = "Physiologists",
                        isSelected = selectedCategoryIndex == 1,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedCategoryIndex = 1 }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // --- Professional Card with Click ---
                ProfessionalCard(
                    professional = sampleProfessional,
                    onClick = {
                        selectedProfessional = sampleProfessional
                        showBottomSheet = true
                    }
                )
            }
        }

        // --- The Bottom Sheet Logic ---
        if (showBottomSheet && selectedProfessional != null) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                containerColor = Color.White, // Ensure sheet is white
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
            ) {
                ProfessionalDetailsContent(
                    professional = selectedProfessional!!,
                    onBookClick = {
                        Log.d("ProfessionalScreen", "Book Clicked")
                        vm.setSelectedProfessional(selectedProfessional!!)
                        onBookClick()
                        showBottomSheet = false
                    }
                )
            }
        }
    }
}

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit
) {
    val backgroundColor = Color.Black.copy(alpha = 0.05f) // Very light gray

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(50.dp)) // Fully rounded
            .background(backgroundColor)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = Color.Black.copy(alpha = 0.3f),
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Box(modifier = Modifier.weight(1f)) {
            if (value.isEmpty()) {
                Text(
                    text = "Search using name or type",
                    color = Color.Black.copy(alpha = 0.3f),
                    fontSize = 14.sp
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontFamily = med_font
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun CategoryButton(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val containerColor = if (isSelected) accentColor else Color.Black.copy(alpha = 0.05f)
    val contentColor = if (isSelected) Color.White else accentColor

    Box(
        modifier = modifier
            .height(60.dp) // Match the chunky look in the image
            .clip(RoundedCornerShape(30.dp)) // Rounded rectangle
            .background(containerColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = contentColor,
            fontFamily = med_font, // Use your semibold_font here
            fontSize = 12.sp
        )
    }
}

@Composable
fun FilterSortBar() {
    val backgroundColor = Color.Black.copy(alpha = 0.05f)

    Row(
        modifier = Modifier
            .width(220.dp) // Fixed width for the floating bar
            .height(50.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(backgroundColor)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Filter Option
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { /* On Filter Click */ }
        ) {
            Icon(
                imageVector = Icons.Default.FilterList, // Or use a custom funnel icon
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Filter",
                color = accentColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Vertical Divider
        Box(
            modifier = Modifier
                .height(20.dp)
                .width(1.dp)
                .background(Color.Black.copy(alpha = 0.1f))
        )

        // Sort Option
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { /* On Sort Click */ }
        ) {
            Icon(
                imageVector = Icons.Default.Sort, // Or use a custom lines icon
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Sort By",
                color = accentColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview
@Composable
private fun ProfessionalScreenPrev() {
//    ProfessionalScreen()
}
