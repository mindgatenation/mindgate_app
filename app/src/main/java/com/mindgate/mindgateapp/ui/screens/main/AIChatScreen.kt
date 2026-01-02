package com.mindgate.mindgateapp.ui.screens.main

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mindgate.mindgateapp.InputGrayColor
import com.mindgate.mindgateapp.accentColor
import com.mindgate.mindgateapp.data.dao.AiChat
import com.mindgate.mindgateapp.greenColor
import com.mindgate.mindgateapp.med_font
import com.mindgate.mindgateapp.reg_font
import com.mindgate.mindgateapp.semibold_font
import com.mindgate.mindgateapp.ui.components.MindgateBottomNavigation
import com.mindgate.mindgateapp.viewmodels.AIChatViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AIChatScreen(
    modifier: Modifier= Modifier,
    vm: AIChatViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentChat by vm.currentChat.collectAsState()
    val chatHistory by vm.history.collectAsState()
    var inputText by remember { mutableStateOf("") }

    val isKeyboardOpen = WindowInsets.ime.getBottom(LocalDensity.current) > 0

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White,
                modifier = Modifier.width(300.dp)
            ) {
                DrawerContent(
                    history = chatHistory,
                    onNewChat = {
                        vm.startNewChat()
                        scope.launch { drawerState.close() }
                    },
                    onChatSelected = { id ->
                        vm.selectChat(id)
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
            containerColor = Color.White,
            contentWindowInsets = WindowInsets.statusBars,
            topBar = {
                CustomTopBar(
                    title = currentChat?.chat_name ?: "Chat With Us",
                    onMenuClick = { scope.launch { drawerState.open() } },
                    modifier
                )
            },
//            modifier = modifier,
            bottomBar = {
                // Stack Input Field on top of Bottom Nav
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        // Add IME padding so keyboard pushes this up
                        .imePadding()
                ) {
                    InputArea(
                        value = inputText,
                        onValueChange = { inputText = it },
                        onSend = {
                            if (inputText.isNotBlank()) {
                                vm.sendMessage(inputText)
                                inputText = ""
                            }
                        }
                    )
                    if (!isKeyboardOpen) {
                        // Adjust this height (80.dp) to match your actual Bottom Nav height
                        Spacer(modifier = Modifier.height(90.dp))
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (currentChat == null) {
                    // --- EMPTY STATE ---
                    EmptyStateView()
                } else {
                    // --- CHAT LIST ---
                    ChatListView(chat = currentChat!!)
                }
            }
        }
    }
}

// --- Sub-Composables ---

@Composable
fun CustomTopBar(title: String, onMenuClick: () -> Unit,modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp)
            ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Drawer Button
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(greenColor)
                .clickable { onMenuClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = accentColor)
        }

        Spacer(modifier = Modifier.weight(1f))

        // Title
        Text(
            text = title,
            fontSize = 14.sp,
            color = accentColor,
            fontFamily = med_font
        )

        Spacer(modifier = Modifier.weight(1f))

        // Invisible box to balance layout center
        Box(modifier = Modifier.size(50.dp))
    }
}

@Composable
fun EmptyStateView() {
    val questions = listOf(
        "Improve my mood?",
        "Build better habits?",
        "Practice Self-Care?",
        "Reduce anxiety?"
    )
    var index by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            index = (index + 1) % questions.size
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Chat With Us",
            fontSize = 24.sp,
            color = accentColor,
            fontFamily = med_font
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Animated Text
        AnimatedContent(
            targetState = questions[index],
            transitionSpec = {
                (slideInVertically { height -> height } + fadeIn())
                    .togetherWith(slideOutVertically { height -> -height } + fadeOut())
            },
            label = "TextAnimation"
        ) { text ->
            Text(
                text = text,
                fontSize = 15.sp,
                color = accentColor,
                fontFamily = reg_font
            )
        }
        // Push content up slightly to match design
        Spacer(modifier = Modifier.height(150.dp))
    }
}

@Composable
fun ChatListView(chat: AiChat) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Top,
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        items(chat.chats) { pair ->
            // User Message (Right)
            UserBubble(text = pair.prompt)
            Spacer(modifier = Modifier.height(15.dp))

            // AI Message (Left)
            AiBubble(text = pair.response)
            Spacer(modifier = Modifier.height(25.dp))
        }
    }
}

@Composable
fun UserBubble(text: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f) // Max 80% width
                .background(
                    greenColor,
                    RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 20.dp)
                )
                .padding(20.dp)
        ) {
            Text(
                text = text,
                color = accentColor,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun AiBubble(text: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "MINDGATE",
            color = Color.Gray,
            fontSize = 10.sp,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(bottom = 5.dp, start = 5.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .background(
                    Color(0xFFE0E0E0),
                    RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 20.dp,
                        bottomEnd = 20.dp,
                        bottomStart = 20.dp
                    )
                )
                .padding(20.dp)
        ) {
            Text(
                text = text,
                color = Color.Black,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun InputArea(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .height(60.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(greenColor.copy(alpha = 0.5f))
            .padding(start = 20.dp, end = 15.dp)
                ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f)
            ,
            textStyle = TextStyle(fontSize = 16.sp, color = accentColor),
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = "Ask Something...",
                        color = accentColor,
                        fontSize = 12.sp,
                        fontFamily = med_font
                    )
                }
                innerTextField()
            }
        )

        // Send Button
        IconButton(
            onClick = onSend,
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(accentColor)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward, // Use rotation if needed to point UP
                contentDescription = "Send",
                tint = Color.White,
                modifier = Modifier.rotate(-90f) // Rotate arrow to point up
            )
        }
    }
}

@Composable
fun DrawerContent(
    history: List<AiChat>,
    onNewChat: () -> Unit,
    onChatSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Conversation History",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = accentColor,
            modifier = Modifier.padding(vertical = 20.dp)
        )

        // New Chat Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(accentColor)
                .clickable { onNewChat() }
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(10.dp))
            Text("New Chat", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            items(history) { chat ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(InputGrayColor)
                        .clickable { onChatSelected(chat.chat_id) }
                        .padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.History, contentDescription = null, tint = Color.Gray)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = chat.chat_name,
                        color = Color.Black,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

// Basic TextField helper to avoid styling overhead of Material TextField
@Composable
fun BasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default,
    decorationBox: @Composable (innerTextField: @Composable () -> Unit) -> Unit
) {
    androidx.compose.foundation.text.BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = textStyle,
        decorationBox = decorationBox,
        singleLine = true
    )
}

// Ext helper for rotation
fun Modifier.rotate(degrees: Float) = this.then(
    Modifier.graphicsLayer(rotationZ = degrees)
)

@Preview
@Composable
private fun AIChatScreenPrev() {
    AIChatScreen()
}