package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.TealSecondary
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.automirrored.filled.List
import com.example.ui.viewmodel.HealthViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: HealthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val highContrast by viewModel.highContrastEnabled.collectAsState()
            val largeText by viewModel.largeTextEnabled.collectAsState()

            MyApplicationTheme(
                highContrast = highContrast,
                largeText = largeText
            ) {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContent(viewModel: HealthViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    val highContrast by viewModel.highContrastEnabled.collectAsState()
    val largeText by viewModel.largeTextEnabled.collectAsState()

    val medications by viewModel.medications.collectAsState()
    val vitals by viewModel.vitals.collectAsState()
    val appointments by viewModel.appointments.collectAsState()
    val messages by viewModel.messages.collectAsState()

    // Emergency and Voice sheet visual controllers
    val sosActive by viewModel.sosActive.collectAsState()
    var voiceSheetOpen by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = if (highContrast) Color.Yellow else TealSecondary,
                            modifier = Modifier.size(28.dp)
                        )
                        Text(
                            text = "CARELINK PRO",
                            fontWeight = FontWeight.Black,
                            style = MaterialTheme.typography.titleLarge,
                            color = if (highContrast) Color.White else TealPrimary
                        )
                    }
                },
                actions = {
                    // Quick Toggle for high contrast accessibility
                    IconButton(
                        onClick = { viewModel.toggleHighContrast() },
                        modifier = Modifier.testTag("action_high_contrast_toggle")
                    ) {
                        Icon(
                            imageVector = if (highContrast) Icons.Default.Settings else Icons.Default.Info,
                            contentDescription = "Toggle High Contrast",
                            tint = if (highContrast) Color.Yellow else TealSecondary,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // Quick Toggle for font size
                    IconButton(
                        onClick = { viewModel.toggleLargeText() },
                        modifier = Modifier.testTag("action_large_text_toggle")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Scale Fonts",
                            tint = if (highContrast) Color.Yellow else TealSecondary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (highContrast) Color.Black else MaterialTheme.colorScheme.background,
                    titleContentColor = if (highContrast) Color.White else TealPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = if (highContrast) Color.Black else Color.White,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .border(
                        BorderStroke(1.dp, if (highContrast) Color.White else Color(0xFFF1F5F9)),
                        RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                    )
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                // Nav item 1: Senior view
                NavigationBarItem(
                    selected = currentTab == "senior",
                    onClick = { viewModel.setTab("senior") },
                    icon = { Icon(Icons.Default.AccountBox, contentDescription = "Senior Dashboard") },
                    label = { Text("Senior", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = if (highContrast) Color.Black else Color.White,
                        selectedTextColor = if (highContrast) Color.Yellow else TealSecondary,
                        indicatorColor = if (highContrast) Color.Yellow else TealSecondary,
                        unselectedIconColor = if (highContrast) Color.White else Color.Gray,
                        unselectedTextColor = if (highContrast) Color.White else Color.Gray
                    )
                )

                // Nav item 2: Caregiver Dashboard
                NavigationBarItem(
                    selected = currentTab == "caregiver",
                    onClick = { viewModel.setTab("caregiver") },
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Caregiver Insights") },
                    label = { Text("Caregiver", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = if (highContrast) Color.Black else Color.White,
                        selectedTextColor = if (highContrast) Color.Yellow else TealSecondary,
                        indicatorColor = if (highContrast) Color.Yellow else TealSecondary,
                        unselectedIconColor = if (highContrast) Color.White else Color.Gray,
                        unselectedTextColor = if (highContrast) Color.White else Color.Gray
                    )
                )

                // Nav item 3: Message Portal
                NavigationBarItem(
                    selected = currentTab == "messaging",
                    onClick = { viewModel.setTab("messaging") },
                    icon = { Icon(Icons.Default.Email, contentDescription = "Encrypted Message Portal") },
                    label = { Text("Portal", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = if (highContrast) Color.Black else Color.White,
                        selectedTextColor = if (highContrast) Color.Yellow else TealSecondary,
                        indicatorColor = if (highContrast) Color.Yellow else TealSecondary,
                        unselectedIconColor = if (highContrast) Color.White else Color.Gray,
                        unselectedTextColor = if (highContrast) Color.White else Color.Gray
                    )
                )

                // Nav item 4: Appointments
                NavigationBarItem(
                    selected = currentTab == "appointments",
                    onClick = { viewModel.setTab("appointments") },
                    icon = { Icon(Icons.Default.DateRange, contentDescription = "Appointments & Reminders") },
                    label = { Text("Schedules", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = if (highContrast) Color.Black else Color.White,
                        selectedTextColor = if (highContrast) Color.Yellow else TealSecondary,
                        indicatorColor = if (highContrast) Color.Yellow else TealSecondary,
                        unselectedIconColor = if (highContrast) Color.White else Color.Gray,
                        unselectedTextColor = if (highContrast) Color.White else Color.Gray
                    )
                )

                // Nav item 5: Video Feed
                NavigationBarItem(
                    selected = currentTab == "video_feed",
                    onClick = { viewModel.setTab("video_feed") },
                    icon = { Icon(Icons.Default.PlayArrow, contentDescription = "Simulated Camera Vision") },
                    label = { Text("Camera", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = if (highContrast) Color.Black else Color.White,
                        selectedTextColor = if (highContrast) Color.Yellow else TealSecondary,
                        indicatorColor = if (highContrast) Color.Yellow else TealSecondary,
                        unselectedIconColor = if (highContrast) Color.White else Color.Gray,
                        unselectedTextColor = if (highContrast) Color.White else Color.Gray
                    )
                )

                // Nav item 6: AI Diagnostics
                NavigationBarItem(
                    selected = currentTab == "ai_diagnostics",
                    onClick = { viewModel.setTab("ai_diagnostics") },
                    icon = { Icon(Icons.Default.Info, contentDescription = "Gemini Diagnostics") },
                    label = { Text("AI Reports", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = if (highContrast) Color.Black else Color.White,
                        selectedTextColor = if (highContrast) Color.Yellow else TealSecondary,
                        indicatorColor = if (highContrast) Color.Yellow else TealSecondary,
                        unselectedIconColor = if (highContrast) Color.White else Color.Gray,
                        unselectedTextColor = if (highContrast) Color.White else Color.Gray
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(if (highContrast) Color.Black else MaterialTheme.colorScheme.background)
        ) {
            // Animated Tab switcher
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = {
                    fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(220))
                },
                label = "ScreenTransition"
            ) { targetTab ->
                when (targetTab) {
                    "senior" -> SeniorPortalScreen(
                        viewModel = viewModel,
                        medications = medications,
                        vitals = vitals,
                        onVoiceClick = { voiceSheetOpen = true }
                    )
                    "caregiver" -> CaregiverDashboardScreen(
                        viewModel = viewModel,
                        vitals = vitals
                    )
                    "messaging" -> MessagingScreen(
                        viewModel = viewModel,
                        messages = messages
                    )
                    "appointments" -> AppointmentsScreen(
                        viewModel = viewModel,
                        appointments = appointments
                    )
                    "video_feed" -> VideoFeedScreen(
                        viewModel = viewModel
                    )
                    "ai_diagnostics" -> AiDiagnosticsScreen(
                        viewModel = viewModel
                    )
                }
            }

            // Emergency SOS Overlays (flashing, large, prioritized layout)
            if (sosActive) {
                EmergencySosDialog(viewModel = viewModel, highContrast = highContrast)
            }

            // Voice Assistant Sheet Overlay
            if (voiceSheetOpen) {
                VoiceAssistantDialog(
                    viewModel = viewModel,
                    highContrast = highContrast,
                    onDismiss = { voiceSheetOpen = false }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceAssistantDialog(
    viewModel: HealthViewModel,
    highContrast: Boolean,
    onDismiss: () -> Unit
) {
    val voiceOutput by viewModel.voiceOutput.collectAsState()
    val isVoiceLoading by viewModel.isVoiceLoading.collectAsState()
    val companionOutput by viewModel.companionOutput.collectAsState()
    val isCompanionLoading by viewModel.isCompanionLoading.collectAsState()
    val companionPersonality by viewModel.companionPersonality.collectAsState()
    
    var commandInput by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = if (highContrast) Color.Black else Color(0xFF1E293B),
        contentColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .windowInsetsPadding(WindowInsets.navigationBars),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Companion Icon & Personality
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(
                            when (companionPersonality) {
                                "Joyful Friend" -> Color(0xFFFCD34D)
                                "Wise Mentor" -> Color(0xFF818CF8)
                                else -> Color(0xFFF472B6)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when (companionPersonality) {
                            "Joyful Friend" -> "😊"
                            "Wise Mentor" -> "📚"
                            else -> "🌸"
                        },
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = when (companionPersonality) {
                            "Joyful Friend" -> "Chat with Joy"
                            "Wise Mentor" -> "Chat with Sage"
                            else -> "Chat with Grace"
                        },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = if (highContrast) Color.Yellow else Color.White
                    )
                    Text(
                        text = "Your comforting and friendly companion",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.LightGray
                    )
                }
            }

            // Personality Selectors inside the sheet
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val personalities = listOf("Joyful Friend", "Wise Mentor", "Gentle Caregiver")
                personalities.forEach { personality ->
                    val isSelected = companionPersonality == personality
                    Button(
                        onClick = { viewModel.setCompanionPersonality(personality) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) {
                                if (highContrast) Color.Yellow else Color(0xFF3B82F6)
                            } else {
                                Color(0xFF334155)
                            },
                            contentColor = if (isSelected) Color.Black else Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = when (personality) {
                                "Joyful Friend" -> "😊 Joyful"
                                "Wise Mentor" -> "📚 Wise"
                                else -> "🌸 Gentle"
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Chat response block
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0F172A))
                    .border(2.dp, if (highContrast) Color.White else Color(0xFF334155), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                if (isCompanionLoading || isVoiceLoading) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = if (highContrast) Color.Yellow else Color(0xFFF3921F))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Companion is responding...", color = Color.LightGray)
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        item {
                            Text(
                                text = companionOutput,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                lineHeight = 26.sp
                            )
                        }
                    }
                }
            }

            // Quick companion commands
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "TAP QUICK COMMANDS:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Black,
                    color = if (highContrast) Color.Yellow else Color.Gray
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { viewModel.submitCompanionChat("Give me today's positive affirmation!") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Affirmation please 🌟", color = Color.White, fontSize = 11.sp, maxLines = 1)
                    }

                    Button(
                        onClick = { viewModel.getCompanionReminders() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("My Schedule 📅", color = Color.White, fontSize = 11.sp, maxLines = 1)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { viewModel.submitCompanionChat("Is my blood pressure normal?") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("BP Normal? 🩺", color = Color.White, fontSize = 11.sp, maxLines = 1)
                    }

                    Button(
                        onClick = { viewModel.submitCompanionChat("Tell me a warm story or joke") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Story or Joke 🎭", color = Color.White, fontSize = 11.sp, maxLines = 1)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Text chat input field
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = commandInput,
                    onValueChange = { commandInput = it },
                    placeholder = { Text("Ask or type messages...") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("voice_command_input_field"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = if (highContrast) Color.Yellow else Color(0xFFF3921F),
                        unfocusedBorderColor = Color.White
                    )
                )

                Button(
                    onClick = {
                        if (commandInput.isNotBlank()) {
                            viewModel.submitCompanionChat(commandInput)
                            commandInput = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (highContrast) Color.Yellow else Color(0xFFF3921F),
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(54.dp)
                ) {
                    Text("Ask", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun EmergencySosDialog(
    viewModel: HealthViewModel,
    highContrast: Boolean
) {
    val countdown by viewModel.sosCountdown.collectAsState()
    val dispatched by viewModel.sosDispatched.collectAsState()

    // Breathing flashing background color animation
    val infiniteTransition = rememberInfiniteTransition(label = "SOS Flashing")
    val alertColor by infiniteTransition.animateColor(
        initialValue = Color(0xFF7A0000),
        targetValue = Color(0xFFFF1744),
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Color"
    )

    Dialog(
        onDismissRequest = { /* Force explicit cancel click */ },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (highContrast) Color.Black else alertColor),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(110.dp)
                )

                if (!dispatched) {
                    Text(
                        text = "EMERGENCY SOS IS ACTIVE",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Alerting dispatch services and family in:",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "$countdown",
                        fontSize = 90.sp,
                        fontWeight = FontWeight.Black,
                        color = if (highContrast) Color.Yellow else Color.White,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.cancelSOS() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .testTag("cancel_sos_button")
                    ) {
                        Text(
                            text = "CANCEL DISTRESS ALERT",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Black
                        )
                    }
                } else {
                    Text(
                        text = "SOS DISPATCH SENT!",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = if (highContrast) Color.Green else Color.Yellow,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "• GPS telemetries sent to Emergency Dispatch (911).\n" +
                                "• Primary Care Physician Dr. Adrian Vance notified.\n" +
                                "• Family Caregiver Marcus (Son) alerted via SMS/Call.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        lineHeight = 28.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.cancelSOS() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Close Alert", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
