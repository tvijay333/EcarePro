package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ui.viewmodel.HealthViewModel
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.TealSecondary
import kotlin.random.Random

@Composable
fun VideoFeedScreen(
    viewModel: HealthViewModel
) {
    val highContrast by viewModel.highContrastEnabled.collectAsState()
    val isMonitoring by viewModel.isVideoMonitoring.collectAsState()
    val motionAlerts by viewModel.motionAlerts.collectAsState()

    val liveHR by viewModel.liveCamHeartRate.collectAsState()
    val liveBR by viewModel.liveCamBreathRate.collectAsState()
    val motionIndex by viewModel.liveCamMotionIndex.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 32.dp, top = 8.dp)
    ) {
        // Header
        item {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = "AI Computer Vision Diagnostics",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = if (highContrast) Color.Yellow else MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Automated room-safety motion detection and contactless vital scanning.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (highContrast) Color.White else Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Camera Feed Display panel
        item {
            Card(
                shape = RoundedCornerShape(32.dp),
                border = androidx.compose.foundation.BorderStroke(3.dp, if (highContrast) Color.White else TealPrimary),
                colors = CardDefaults.cardColors(containerColor = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.33f) // 4:3 camera aspect ratio
                    .testTag("camera_view_panel")
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    if (isMonitoring) {
                        // Drawing Simulated skeleton pose estimation nodes and bounding box overlay
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height

                            // Draw a simulated senior seating outline node skeleton
                            val head = Offset(w * 0.5f, h * 0.35f)
                            val neck = Offset(w * 0.5f, h * 0.42f)
                            val leftShoulder = Offset(w * 0.42f, h * 0.44f)
                            val rightShoulder = Offset(w * 0.58f, h * 0.44f)
                            val spineEnd = Offset(w * 0.5f, h * 0.68f)
                            val leftElbow = Offset(w * 0.38f, h * 0.58f)
                            val rightElbow = Offset(w * 0.62f, h * 0.58f)
                            val leftHip = Offset(w * 0.45f, h * 0.70f)
                            val rightHip = Offset(w * 0.55f, h * 0.70f)
                            val leftKnee = Offset(w * 0.42f, h * 0.82f)
                            val rightKnee = Offset(w * 0.58f, h * 0.82f)

                            // 1. Draw camera target crosshairs
                            val colorAccent = if (highContrast) Color.Yellow else Color(0xFF00FF00)
                            drawRect(
                                color = Color.DarkGray,
                                topLeft = Offset(w * 0.1f, h * 0.1f),
                                size = Size(w * 0.8f, h * 0.8f),
                                style = Stroke(width = 1.dp.toPx())
                            )

                            // 2. Draw skeleton bone linkages
                            val boneColor = if (highContrast) Color.Cyan else Color(0xFF00E5FF)
                            val strokeW = 3.dp.toPx()

                            drawLine(boneColor, head, neck, strokeW)
                            drawLine(boneColor, neck, leftShoulder, strokeW)
                            drawLine(boneColor, neck, rightShoulder, strokeW)
                            drawLine(boneColor, leftShoulder, leftElbow, strokeW)
                            drawLine(boneColor, rightShoulder, rightElbow, strokeW)
                            drawLine(boneColor, neck, spineEnd, strokeW)
                            drawLine(boneColor, spineEnd, leftHip, strokeW)
                            drawLine(boneColor, spineEnd, rightHip, strokeW)
                            drawLine(boneColor, leftHip, leftKnee, strokeW)
                            drawLine(boneColor, rightHip, rightKnee, strokeW)

                            // 3. Draw keypoint joints as glowing dots
                            val dotColor = Color.White
                            drawCircle(dotColor, 8.dp.toPx(), head)
                            drawCircle(dotColor, 5.dp.toPx(), neck)
                            drawCircle(dotColor, 5.dp.toPx(), leftShoulder)
                            drawCircle(dotColor, 5.dp.toPx(), rightShoulder)
                            drawCircle(dotColor, 5.dp.toPx(), leftElbow)
                            drawCircle(dotColor, 5.dp.toPx(), rightElbow)
                            drawCircle(dotColor, 5.dp.toPx(), leftHip)
                            drawCircle(dotColor, 5.dp.toPx(), rightHip)
                            drawCircle(dotColor, 5.dp.toPx(), leftKnee)
                            drawCircle(dotColor, 5.dp.toPx(), rightKnee)

                            // 4. Bounding box labeled 'Arthur'
                            val boxLeft = w * 0.32f
                            val boxTop = h * 0.22f
                            val boxW = w * 0.36f
                            val boxH = h * 0.68f
                            drawRect(
                                color = colorAccent,
                                topLeft = Offset(boxLeft, boxTop),
                                size = Size(boxW, boxH),
                                style = Stroke(width = 2.dp.toPx())
                            )
                        }

                        // Top bar status overlay
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(Color.Red)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "LIVE FEED • HD 1080P",
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0x99000000)),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "CAM 01: LIVING ROOM",
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(6.dp)
                                )
                            }
                        }

                        // Contactless Vital telemetry overlay
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(12.dp)
                                .background(Color(0x99000000), RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("AI VITAL SCANNER", color = Color.Yellow, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                            Text("• Contactless HR: $liveHR bpm", color = Color.White, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                            Text("• Contactless BR: $liveBR rpm", color = Color.White, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                            Text("• Motion Index: ${"%.2f".format(motionIndex)}", color = Color.White, style = MaterialTheme.typography.bodySmall)
                            Text("• Status: Seated Recliner (Stable)", color = Color.Green, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        // Vision Feed Paused
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "VISION FEED PAUSED",
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Live Feed Controllers: Pause toggle & Simulate hazard event!
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { viewModel.toggleVideoMonitoring() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isMonitoring) Color.Gray else (if (highContrast) Color.Yellow else TealSecondary),
                        contentColor = if (isMonitoring) Color.Black else Color.White
                    ),
                    shape = RoundedCornerShape(32.dp),
                    modifier = Modifier.weight(1f).height(56.dp)
                ) {
                    Text(if (isMonitoring) "Pause Scanner" else "Resume Scanner", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { viewModel.simulateMotionFallAlert() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (highContrast) Color.Red else Color(0xFFDC2626),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(32.dp),
                    modifier = Modifier.weight(1.2f).height(56.dp),
                    enabled = isMonitoring
                ) {
                    Icon(imageVector = Icons.Default.Warning, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Simulate Fall Hazard", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Motion & Fall Alerts History Panel
        item {
            Text(
                text = "Live Computer Vision Activity Alerts",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        items(motionAlerts) { alert ->
            Card(
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (alert.contains("🚨")) {
                        if (highContrast) Color.Red else Color(0xFFFFEBEE)
                    } else {
                        if (highContrast) Color.Black else Color.White
                    }
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (alert.contains("🚨")) Color.Red else (if (highContrast) Color.White else Color(0xFFE2E8F0))
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (alert.contains("🚨")) Icons.Default.Warning else Icons.Default.Info,
                        contentDescription = null,
                        tint = if (alert.contains("🚨")) {
                            if (highContrast) Color.White else Color.Red
                        } else {
                            if (highContrast) Color.Cyan else MaterialTheme.colorScheme.primary
                        },
                        modifier = Modifier.size(28.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = alert,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (alert.contains("🚨")) FontWeight.Bold else FontWeight.Normal,
                        color = if (alert.contains("🚨") && highContrast) Color.White else Color.Unspecified
                    )
                }
            }
        }
    }
}
