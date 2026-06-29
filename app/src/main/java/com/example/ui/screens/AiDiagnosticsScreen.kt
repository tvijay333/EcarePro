package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.HealthViewModel
import com.example.ui.theme.TealSecondary

@Composable
fun AiDiagnosticsScreen(
    viewModel: HealthViewModel
) {
    val highContrast by viewModel.highContrastEnabled.collectAsState()
    val isGenerating by viewModel.isGeneratingDiagnostics.collectAsState()
    val diagnosticsResponse by viewModel.diagnosticsResponse.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 32.dp, top = 8.dp)
    ) {
        // Title block
        item {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = "AI Diagnostics & Interventions",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = if (highContrast) Color.Yellow else MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Gemini-powered wellness diagnostics suggesting safe, custom interventions.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (highContrast) Color.White else Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Onboarding card about how AI is used
        item {
            Card(
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (highContrast) Color.Black else Color(0xFFECFDF5)
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (highContrast) Color.White else Color(0xFFA7F3D0)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = if (highContrast) Color.Green else Color(0xFF059669),
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "How this works",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = if (highContrast) Color.White else Color(0xFF064E3B)
                        )
                        Text(
                            text = "Arthur's heart rate, blood pressure, blood glucose levels, and daily medication completion logs are passed to securely analyze behavioral trends and suggest customized lifestyle or diagnostic interventions.",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (highContrast) Color.White else Color(0xFF047857)
                        )
                    }
                }
            }
        }

        // Generate Button
        item {
            Button(
                onClick = { viewModel.runAiDiagnostics() },
                enabled = !isGenerating,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("generate_ai_diagnostics_button"),
                shape = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (highContrast) Color.Yellow else TealSecondary,
                    contentColor = if (highContrast) Color.Black else Color.White
                )
            ) {
                if (isGenerating) {
                    CircularProgressIndicator(
                        color = if (highContrast) Color.Black else Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Analyzing Telemetry & Behavior...", fontWeight = FontWeight.Bold)
                } else {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Generate Diagnostic Suggestions", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Result viewport
        item {
            Card(
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (highContrast) Color.Black else Color.White
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (highContrast) Color.White else Color(0xFFE2E8F0)),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Diagnostics & Wellness Recommendation",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (highContrast) Color.Yellow else MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (diagnosticsResponse.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = Color.LightGray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No diagnostic reports generated today.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                            Text(
                                text = "Click 'Generate Diagnostic Suggestions' to analyze the current data.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    } else {
                        // Display the Markdown text cleanly
                        Text(
                            text = diagnosticsResponse,
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 24.sp,
                            modifier = Modifier.testTag("ai_diagnostics_output")
                        )
                    }
                }
            }
        }
    }
}
