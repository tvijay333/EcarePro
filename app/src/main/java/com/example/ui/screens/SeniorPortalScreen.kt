package com.example.ui.screens
 
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
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
import com.example.data.model.Medication
import com.example.data.model.Vital
import com.example.data.model.UserStats
import com.example.ui.viewmodel.HealthViewModel
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.TealSecondary
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Brush
import com.example.R
 
@Composable
fun SeniorPortalScreen(
    viewModel: HealthViewModel,
    medications: List<Medication>,
    vitals: List<Vital>,
    onVoiceClick: () -> Unit
) {
    val highContrast by viewModel.highContrastEnabled.collectAsState()
    val isSyncing by viewModel.isSyncing.collectAsState()
    val syncProgress by viewModel.syncProgress.collectAsState()
    val syncMessage by viewModel.syncMessage.collectAsState()
 
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 32.dp, top = 8.dp)
    ) {
        // Welcome Banner
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (highContrast) Color.Black else Color.White
                ),
                border = if (highContrast) BorderStroke(3.dp, Color.White) else BorderStroke(1.dp, Color(0xFFE2E8F0)),
                shape = RoundedCornerShape(32.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("welcome_card")
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    // Modern Sleek Background Image for non-high contrast mode
                    if (!highContrast) {
                        Image(
                            painter = painterResource(id = R.drawable.img_wellness_hero_1782731060782),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        // Gradient Overlay to ensure text readability
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color(0xE60F172A) // Sleek Slate 900 gradient at bottom
                                        )
                                    )
                                )
                        )
                    }
 
                    // Content
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.weight(1f).padding(end = 8.dp)
                            ) {
                                Text(
                                    text = "WELCOME, ARTHUR",
                                    style = MaterialTheme.typography.displayMedium,
                                    fontWeight = FontWeight.Black,
                                    color = if (highContrast) Color.Yellow else Color.White
                                )
                                Text(
                                    text = "Your health care is active and monitored.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (highContrast) Color.White else Color(0xFFE2E8F0),
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(if (highContrast) Color.Yellow else Color(0x33FFFFFF))
                                    .border(1.dp, if (highContrast) Color.Yellow else Color.White, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "Profile",
                                    tint = if (highContrast) Color.Black else Color.White,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
 
                        // Quick buttons
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            SuggestionChip(
                                onClick = { viewModel.toggleHighContrast() },
                                label = { Text("High Contrast", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = if (highContrast) Color.Black else Color.White) },
                                icon = { Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(16.dp), tint = if (highContrast) Color.Black else Color.White) },
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = if (highContrast) Color.Yellow else Color(0x33FFFFFF),
                                    labelColor = if (highContrast) Color.Black else Color.White,
                                    iconContentColor = if (highContrast) Color.Black else Color.White
                                ),
                                border = if (highContrast) null else BorderStroke(1.dp, Color.White)
                            )
                            SuggestionChip(
                                onClick = { viewModel.toggleLargeText() },
                                label = { Text("Font Size", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = if (highContrast) Color.Black else Color.White) },
                                icon = { Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(16.dp), tint = if (highContrast) Color.Black else Color.White) },
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = if (highContrast) Color.Yellow else Color(0x33FFFFFF),
                                    labelColor = if (highContrast) Color.Black else Color.White,
                                    iconContentColor = if (highContrast) Color.Black else Color.White
                                ),
                                border = if (highContrast) null else BorderStroke(1.dp, Color.White)
                            )
                        }
                    }
                }
            }
        }

        // ------------------ DAILY HEALTH QUESTS & BADGES ------------------
        item {
            val stats by viewModel.userStats.collectAsState()
            val currentStats = stats ?: UserStats(points = 20, hydrationGlasses = 2, walkingMinutes = 5)
            val medsList by viewModel.medications.collectAsState()
            val totalMeds = medsList.size
            val takenMeds = medsList.count { it.isTaken }
            
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (highContrast) Color.Black else Color.White
                ),
                border = if (highContrast) BorderStroke(3.dp, Color.White) else BorderStroke(1.dp, Color(0xFFE2E8F0)),
                shape = RoundedCornerShape(32.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("gamified_challenges_card")
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    // Header: Points & Icon
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Points Star",
                                tint = if (highContrast) Color.Yellow else Color(0xFFF59E0B),
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Daily Health Quests",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                color = if (highContrast) Color.Yellow else TealPrimary
                            )
                        }
                        
                        // Points Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (highContrast) Color.Yellow else Color(0xFFFEF3C7))
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${currentStats.points} PTS",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Black,
                                color = if (highContrast) Color.Black else Color(0xFFB45309)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Quest 1: Medication Adherence
                    val medsCompleted = takenMeds == totalMeds && totalMeds > 0
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "💊 Medication Adherence Quest",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (totalMeds == 0) "No medications scheduled." else "Take all meds today ($takenMeds/$totalMeds completed)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                            if (medsCompleted) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Completed",
                                    tint = if (highContrast) Color.Green else Color(0xFF10B981),
                                    modifier = Modifier.size(28.dp)
                                )
                            } else {
                                Text(
                                    text = "+50 PTS",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = TealSecondary
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { if (totalMeds > 0) takenMeds.toFloat() / totalMeds else 0f },
                            modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                            color = if (highContrast) Color.Green else Color(0xFF10B981),
                            trackColor = Color(0xFFE2E8F0)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Quest 2: Hydration Quest
                    val hydrationCompleted = currentStats.hydrationGlasses >= 8
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "💧 Hydration Quest",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Drink 8 glasses of water (${currentStats.hydrationGlasses}/8 logged)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (hydrationCompleted) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Completed",
                                        tint = if (highContrast) Color.Green else Color(0xFF10B981),
                                        modifier = Modifier.size(28.dp)
                                    )
                                } else {
                                    Text(
                                        text = "+30 PTS  ",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = TealSecondary
                                    )
                                    IconButton(
                                        onClick = { viewModel.logHydrationGlass() },
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(if (highContrast) Color.Yellow else Color(0xFFDBEAFE), CircleShape)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "Add Water Glass",
                                            tint = if (highContrast) Color.Black else Color(0xFF1E40AF),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { (currentStats.hydrationGlasses.toFloat() / 8f).coerceAtMost(1f) },
                            modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                            color = if (highContrast) Color.Cyan else Color(0xFF3B82F6),
                            trackColor = Color(0xFFE2E8F0)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Quest 3: Walking Quest
                    val walkingCompleted = currentStats.walkingMinutes >= 15
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "🏃 Walking & Movement Quest",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Exercise 15 minutes (${currentStats.walkingMinutes}/15 logged)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (walkingCompleted) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Completed",
                                        tint = if (highContrast) Color.Green else Color(0xFF10B981),
                                        modifier = Modifier.size(28.dp)
                                    )
                                } else {
                                    Text(
                                        text = "+40 PTS  ",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = TealSecondary
                                    )
                                    Button(
                                        onClick = { viewModel.logWalkingMinutes(5) },
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                        modifier = Modifier.height(32.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (highContrast) Color.Yellow else Color(0xFFD1FAE5),
                                            contentColor = if (highContrast) Color.Black else Color(0xFF065F46)
                                        ),
                                        shape = RoundedCornerShape(16.dp)
                                    ) {
                                        Text("+5m", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { (currentStats.walkingMinutes.toFloat() / 15f).coerceAtMost(1f) },
                            modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                            color = if (highContrast) Color.Green else Color(0xFF10B981),
                            trackColor = Color(0xFFE2E8F0)
                        )
                    }
                    
                    // Badges Section
                    if (currentStats.earnedBadgesCsv.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(20.dp))
                        HorizontalDivider(color = Color(0xFFF1F5F9))
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text(
                            text = "🏅 UNLOCKED BADGES:",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Black,
                            color = if (highContrast) Color.Yellow else Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            currentStats.earnedBadgesCsv.split(",").forEach { badge ->
                                if (badge.trim().isNotEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(if (highContrast) Color.Black else Color(0xFFF8FAFC))
                                            .border(1.dp, if (highContrast) Color.White else Color(0xFFCBD5E1), RoundedCornerShape(16.dp))
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = badge.trim(),
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Bold,
                                            color = if (highContrast) Color.White else Color.Black
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // ------------------ AI VIRTUAL COMPANION CARD ------------------
        item {
            val companionPersonality by viewModel.companionPersonality.collectAsState()
            val companionOutput by viewModel.companionOutput.collectAsState()
            val isCompanionLoading by viewModel.isCompanionLoading.collectAsState()
            val companionAffirmation by viewModel.companionAffirmation.collectAsState()
            val isAffirmationLoading by viewModel.isAffirmationLoading.collectAsState()
            
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (highContrast) Color.Black else Color(0xFFF8FAFC)
                ),
                border = if (highContrast) BorderStroke(3.dp, Color.White) else BorderStroke(1.dp, Color(0xFFE2E8F0)),
                shape = RoundedCornerShape(32.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("virtual_companion_card")
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
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
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = when (companionPersonality) {
                                        "Joyful Friend" -> "Joy (Your Companion)"
                                        "Wise Mentor" -> "Sage (Your Mentor)"
                                        else -> "Grace (Your Caregiver)"
                                    },
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Black,
                                    color = if (highContrast) Color.Yellow else Color.Black
                                )
                                Text(
                                    text = "Friendly AI Virtual Companion",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                        }
                        
                        Button(
                            onClick = onVoiceClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (highContrast) Color.Yellow else TealSecondary,
                                contentColor = if (highContrast) Color.Black else Color.White
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(Icons.Default.Mic, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Talk", fontWeight = FontWeight.Bold)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Personality Selectors
                    Text(
                        text = "CHOOSE COMPANION PERSONALITY:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Black,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
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
                                        if (highContrast) Color.Yellow else TealPrimary
                                    } else {
                                        if (highContrast) Color.Black else Color(0xFFE2E8F0)
                                    },
                                    contentColor = if (isSelected) {
                                        if (highContrast) Color.Black else Color.White
                                    } else {
                                        if (highContrast) Color.White else Color.Black
                                    }
                                ),
                                shape = RoundedCornerShape(16.dp),
                                border = if (highContrast && !isSelected) BorderStroke(1.dp, Color.White) else null,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp),
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
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Speech Text Bubble
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (highContrast) Color.Black else Color(0xFFF1F5F9))
                            .border(1.dp, if (highContrast) Color.White else Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
                            .padding(16.dp)
                    ) {
                        if (isCompanionLoading) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = TealSecondary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Companion is thinking...", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            }
                        } else {
                            Text(
                                text = companionOutput,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = if (highContrast) Color.White else Color.Black
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Affirmation Bubble
                    if (companionAffirmation.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (highContrast) Color.Black else Color(0xFFFEF3C7))
                                .border(1.dp, if (highContrast) Color.Yellow else Color(0xFFFCD34D), RoundedCornerShape(20.dp))
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "🌟 Daily Affirmation:\n\"$companionAffirmation\"",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (highContrast) Color.Yellow else Color(0xFF92400E)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.getCompanionAffirmation() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (highContrast) Color.Yellow else Color(0xFFFCD34D),
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            enabled = !isAffirmationLoading
                        ) {
                            Text("🌟 Positive Affirmation", fontSize = 11.sp, fontWeight = FontWeight.Black)
                        }
                        
                        Button(
                            onClick = { viewModel.getCompanionReminders() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (highContrast) Color.Yellow else Color(0xFFDBEAFE),
                                contentColor = if (highContrast) Color.Black else Color(0xFF1E40AF)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            enabled = !isCompanionLoading
                        ) {
                            Text("📅 Check Reminders", fontSize = 11.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }

        // HUGE ACTION BUTTONS: SOS & VOICE ASSISTANCE
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // EMERGENCY SOS BUTTON
                Button(
                    onClick = { viewModel.triggerSOS() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (highContrast) Color.Red else Color(0xFFDC2626)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(96.dp)
                        .testTag("sos_button"),
                    shape = RoundedCornerShape(32.dp),
                    border = if (highContrast) BorderStroke(3.dp, Color.White) else null
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "SOS Alert",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "EMERGENCY SOS",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // VOICE ASSISTANT BUTTON
                Button(
                    onClick = onVoiceClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (highContrast) Color.Yellow else Color(0xFF2563EB),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(96.dp)
                        .testTag("voice_assistant_button"),
                    shape = RoundedCornerShape(32.dp),
                    border = if (highContrast) BorderStroke(3.dp, Color.White) else null
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Voice Assistance",
                            tint = if (highContrast) Color.Black else Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "VOICE HELP",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            color = if (highContrast) Color.Black else Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // Live Wearables Sync Dashboard
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (highContrast) Color.Black else Color.White
                ),
                border = if (highContrast) BorderStroke(3.dp, Color.White) else BorderStroke(1.dp, Color(0xFFE2E8F0)),
                shape = RoundedCornerShape(32.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("wearable_sync_card")
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                tint = if (highContrast) Color.Cyan else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Smart Watch & BP Sync",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = { viewModel.syncWithWearables() },
                            enabled = !isSyncing,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (highContrast) Color.Cyan else MaterialTheme.colorScheme.primary,
                                contentColor = if (highContrast) Color.Black else Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Sync Now", fontWeight = FontWeight.Bold)
                        }
                    }

                    if (isSyncing) {
                        Spacer(modifier = Modifier.height(12.dp))
                        LinearProgressIndicator(
                            progress = { syncProgress },
                            modifier = Modifier.fillMaxWidth(),
                            color = if (highContrast) Color.Cyan else MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = syncMessage,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (highContrast) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else if (syncMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = syncMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = if (highContrast) Color.Green else Color(0xFF4E9F3D)
                        )
                    }
                }
            }
        }

        // MEDICATION TO-DO LIST (Very large, readable, tactile checkboxes)
        item {
            Text(
                text = "Medications for Today",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        if (medications.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No medications scheduled today.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
            }
        } else {
            items(medications) { med ->
                MedicationRow(
                    medication = med,
                    highContrast = highContrast,
                    onCheckedChange = { viewModel.toggleMedication(med) }
                )
            }
        }

        // QUICK VITALS VIEW FOR SENIORS
        item {
            Text(
                text = "Latest Wearable Readings",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val latestHR = vitals.firstOrNull { it.type == "Heart Rate" }
                val latestBP = vitals.firstOrNull { it.type == "Blood Pressure" }

                VitalSummaryCard(
                    title = "Heart Rate",
                    value = latestHR?.value ?: "--",
                    unit = "bpm",
                    status = latestHR?.status ?: "Unknown",
                    icon = Icons.Default.Favorite,
                    iconColor = Color.Red,
                    modifier = Modifier.weight(1f),
                    highContrast = highContrast
                )

                VitalSummaryCard(
                    title = "Blood Pressure",
                    value = latestBP?.value ?: "--",
                    unit = "mmHg",
                    status = latestBP?.status ?: "Unknown",
                    icon = Icons.Default.Star,
                    iconColor = Color(0xFFE07A5F),
                    modifier = Modifier.weight(1f),
                    highContrast = highContrast
                )
            }
        }
    }
}

@Composable
fun MedicationRow(
    medication: Medication,
    highContrast: Boolean,
    onCheckedChange: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (medication.isTaken) {
                if (highContrast) Color(0xFF1E1E1E) else Color(0xFFECFDF5)
            } else {
                if (highContrast) Color.Black else Color.White
            }
        ),
        border = if (highContrast) BorderStroke(3.dp, if (medication.isTaken) Color.Green else Color.White) else BorderStroke(1.dp, if (medication.isTaken) Color(0xFFA7F3D0) else Color(0xFFE2E8F0)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange() }
            .testTag("med_row_${medication.id}")
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Large Checkbox Box for senior fingers (min 48dp target)
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            if (medication.isTaken) {
                                if (highContrast) Color.Green else Color(0xFF4E9F3D)
                            } else {
                                Color.Transparent
                            }
                        )
                        .border(
                            3.dp,
                            if (highContrast) (if (medication.isTaken) Color.Green else Color.White) else (if (medication.isTaken) Color(0xFF4E9F3D) else Color.Gray),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (medication.isTaken) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Taken",
                            tint = if (highContrast) Color.Black else Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = medication.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (highContrast) Color.White else Color.Black
                    )
                    Text(
                        text = "Dosage: ${medication.dosage}  |  Scheduled: ${medication.time}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (highContrast) Color.Yellow else Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            if (medication.isTaken) {
                Text(
                    text = "TAKEN",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (highContrast) Color.Green else Color(0xFF4E9F3D)
                )
            }
        }
    }
}

@Composable
fun VitalSummaryCard(
    title: String,
    value: String,
    unit: String,
    status: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    modifier: Modifier = Modifier,
    highContrast: Boolean
) {
    val statusColor = when (status) {
        "Normal" -> if (highContrast) Color.Green else Color(0xFF4E9F3D)
        "Warning" -> if (highContrast) Color.Yellow else Color(0xFFF3921F)
        "Critical" -> if (highContrast) Color.Red else Color(0xFFD21312)
        else -> if (highContrast) Color.White else Color.Gray
    }

    Card(
        modifier = modifier.height(130.dp),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (highContrast) Color.Black else Color.White
        ),
        border = if (highContrast) BorderStroke(3.dp, Color.White) else BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = if (highContrast) Color.White else Color.Gray
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (highContrast) Color.White else iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = if (highContrast) Color.White else Color.Black
                )
                Text(
                    text = " $unit",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (highContrast) Color.White else Color.Gray,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(statusColor)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = status.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = statusColor
                )
            }
        }
    }
}
