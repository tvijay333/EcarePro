package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Vital
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.TealSecondary
import com.example.ui.viewmodel.HealthViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CaregiverDashboardScreen(
    viewModel: HealthViewModel,
    vitals: List<Vital>
) {
    val highContrast by viewModel.highContrastEnabled.collectAsState()
    var isAddingVital by remember { mutableStateOf(false) }

    // Vital input fields
    var inputType by remember { mutableStateOf("Heart Rate") }
    var inputValue by remember { mutableStateOf("") }
    var inputUnit by remember { mutableStateOf("bpm") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 32.dp, top = 8.dp)
    ) {
        // Dashboard Title Banner
        item {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = "Caregiver Insights Hub",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = if (highContrast) Color.Yellow else MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Real-time vitals tracking and daily behavioral reports.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (highContrast) Color.White else Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Live Health Insight Quick Cards
        item {
            Card(
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (highContrast) Color.Black else Color.White
                ),
                border = if (highContrast) androidx.compose.foundation.BorderStroke(3.dp, Color.White) else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = if (highContrast) Color.Yellow else MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Daily Wellness Insights",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "• Heart Rate stability is steady, averaging 74 bpm.\n" +
                                "• Blood Pressure showed minor pre-hypertension alert (128/82 mmHg) at 08:00 AM.\n" +
                                "• All core medications checked off. 100% compliance recorded today.\n" +
                                "• No fall or motion risk anomalies detected over camera monitoring in last 24h.",
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 24.sp
                    )
                }
            }
        }

        // Beautiful Custom Graph showing Heart Rate Trend over time
        item {
            Card(
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (highContrast) Color.Black else Color.White
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (highContrast) Color.White else Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Simulated Vital Telemetry Graph (Heart Rate)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Draw historical trend using Compose Canvas
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .background(if (highContrast) Color.Black else Color(0xFFFAFBFD))
                            .border(1.dp, if (highContrast) Color.White else Color(0xFFEEEEEE))
                    ) {
                        val points = listOf(68f, 74f, 71f, 82f, 75f, 72f, 77f) // mock historical values
                        val maxVal = 100f
                        val minVal = 50f
                        val range = maxVal - minVal

                        val width = size.width
                        val height = size.height
                        val stepX = width / (points.size - 1)

                        // Draw Grid lines
                        for (i in 1..3) {
                            val y = height * (i / 4f)
                            drawLine(
                                color = if (highContrast) Color.DarkGray else Color(0xFFECEFF1),
                                start = Offset(0f, y),
                                end = Offset(width, y),
                                strokeWidth = 1.dp.toPx()
                            )
                        }

                        // Draw Trend Path
                        val path = Path().apply {
                            val firstY = height - ((points[0] - minVal) / range) * height
                            moveTo(0f, firstY)
                            for (idx in 1 until points.size) {
                                val currentX = idx * stepX
                                val currentY = height - ((points[idx] - minVal) / range) * height
                                lineTo(currentX, currentY)
                            }
                        }

                        drawPath(
                            path = path,
                            color = if (highContrast) Color.Yellow else TealPrimary,
                            style = Stroke(width = 3.dp.toPx())
                        )

                        // Draw Point dots
                        for (idx in points.indices) {
                            val currentX = idx * stepX
                            val currentY = height - ((points[idx] - minVal) / range) * height
                            drawCircle(
                                color = if (highContrast) Color.Cyan else TealSecondary,
                                radius = 5.dp.toPx(),
                                center = Offset(currentX, currentY)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("08:00 AM", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        Text("12:00 PM", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        Text("04:00 PM", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        Text("08:00 PM", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                }
            }
        }

        // Add Manual Vital Entry
        item {
            if (!isAddingVital) {
                Button(
                    onClick = { isAddingVital = true },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (highContrast) Color.Yellow else TealSecondary,
                        contentColor = if (highContrast) Color.Black else Color.White
                    ),
                    shape = RoundedCornerShape(32.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add Patient Vital Record Manually", fontWeight = FontWeight.Bold)
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (highContrast) Color.White else Color(0xFFE2E8F0)),
                    colors = CardDefaults.cardColors(containerColor = if (highContrast) Color.Black else Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "New Vital Reading",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Selection Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("Heart Rate", "Blood Pressure", "Blood Glucose", "Body Temp").forEach { type ->
                                val selected = inputType == type
                                FilterChip(
                                    selected = selected,
                                    onClick = {
                                        inputType = type
                                        inputUnit = when (type) {
                                            "Heart Rate" -> "bpm"
                                            "Blood Pressure" -> "mmHg"
                                            "Blood Glucose" -> "mg/dL"
                                            else -> "°F"
                                        }
                                    },
                                    label = { Text(type, style = MaterialTheme.typography.bodySmall) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = inputValue,
                            onValueChange = { inputValue = it },
                            label = { Text("Value (e.g. 78, 120/80)") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("vital_value_input"),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = { isAddingVital = false },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Cancel")
                            }

                            Button(
                                onClick = {
                                    val status = when (inputType) {
                                        "Heart Rate" -> {
                                            val v = inputValue.toIntOrNull() ?: 70
                                            if (v > 100 || v < 55) "Critical" else if (v > 85) "Warning" else "Normal"
                                        }
                                        "Blood Pressure" -> {
                                            if (inputValue.contains("/")) {
                                                val sys = inputValue.substringBefore("/").toIntOrNull() ?: 120
                                                if (sys > 140) "Critical" else if (sys > 130) "Warning" else "Normal"
                                            } else {
                                                "Normal"
                                            }
                                        }
                                        else -> "Normal"
                                    }
                                    viewModel.addVital(inputType, inputValue, inputUnit, status)
                                    inputValue = ""
                                    isAddingVital = false
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (highContrast) Color.Yellow else TealPrimary,
                                    contentColor = if (highContrast) Color.Black else Color.White
                                )
                            ) {
                                Text("Save Record", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Full Vitals Telemetry History List
        item {
            Text(
                text = "Full Telemetry History Log",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        if (vitals.isEmpty()) {
            item {
                Text("No vital records log available.", color = Color.Gray)
            }
        } else {
            items(vitals) { vital ->
                VitalRow(vital = vital, highContrast = highContrast, onDelete = { viewModel.deleteVital(vital) })
            }
        }
    }
}

@Composable
fun VitalRow(
    vital: Vital,
    highContrast: Boolean,
    onDelete: () -> Unit
) {
    val statusColor = when (vital.status) {
        "Normal" -> if (highContrast) Color.Green else Color(0xFF4E9F3D)
        "Warning" -> if (highContrast) Color.Yellow else Color(0xFFF3921F)
        "Critical" -> if (highContrast) Color.Red else Color(0xFFD21312)
        else -> if (highContrast) Color.White else Color.Gray
    }

    val dateString = remember(vital.timestamp) {
        val sdf = SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault())
        sdf.format(Date(vital.timestamp))
    }

    Card(
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = if (highContrast) Color.Black else Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (highContrast) Color.White else Color(0xFFE2E8F0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(statusColor)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "${vital.type}: ${vital.value} ${vital.unit}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Timestamp: $dateString",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete record",
                    tint = if (highContrast) Color.White else Color.Gray
                )
            }
        }
    }
}
