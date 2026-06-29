package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.Message
import com.example.ui.viewmodel.HealthViewModel
import com.example.ui.theme.TealSecondary
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MessagingScreen(
    viewModel: HealthViewModel,
    messages: List<Message>
) {
    val highContrast by viewModel.highContrastEnabled.collectAsState()
    var messageText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Portal Header
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            Text(
                text = "Secure Family & Medical Portal",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = if (highContrast) Color.Yellow else MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Encrypted communications with primary care doctors and caregivers.",
                style = MaterialTheme.typography.bodyMedium,
                color = if (highContrast) Color.White else Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Dr Card Quick Banner
        Card(
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (highContrast) Color.Black else Color.White
            ),
            border = if (highContrast) androidx.compose.foundation.BorderStroke(3.dp, Color.White) else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(Color.Green, RoundedCornerShape(5.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Connected with: Dr. Adrian Vance (Cardiologist)",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Chat bubbles viewport
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .border(
                    1.dp,
                    if (highContrast) Color.White else Color(0xFFE2E8F0),
                    RoundedCornerShape(32.dp)
                )
                .background(if (highContrast) Color.Black else Color(0xFFF7F9FC))
        ) {
            if (messages.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Encrypted chat channel initialized.\nSend a message to Dr. Adrian.",
                        color = Color.Gray,
                        modifier = Modifier.padding(24.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(messages) { msg ->
                        ChatBubble(message = msg, highContrast = highContrast)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // TextInput row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                placeholder = { Text("Type an encrypted message...") },
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_input_text"),
                shape = RoundedCornerShape(32.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (highContrast) Color.Yellow else TealSecondary,
                    unfocusedBorderColor = if (highContrast) Color.White else Color.Gray
                )
            )

            Button(
                onClick = {
                    if (messageText.isNotBlank()) {
                        viewModel.sendMessage("Marcus (Caregiver / Son)", messageText, isProfessional = false)
                        messageText = ""
                    }
                },
                modifier = Modifier
                    .size(54.dp)
                    .testTag("send_chat_button"),
                shape = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (highContrast) Color.Yellow else TealSecondary,
                    contentColor = if (highContrast) Color.Black else Color.White
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun ChatBubble(
    message: Message,
    highContrast: Boolean
) {
    val isProfessional = message.isProfessional
    val alignment = if (isProfessional) Alignment.Start else Alignment.End
    val bubbleColor = if (isProfessional) {
        if (highContrast) Color(0xFF222222) else Color(0xFFE1F5FE)
    } else {
        if (highContrast) Color.Yellow else MaterialTheme.colorScheme.primaryContainer
    }
    val contentColor = if (isProfessional) {
        if (highContrast) Color.White else Color(0xFF01579B)
    } else {
        if (highContrast) Color.Black else MaterialTheme.colorScheme.onPrimaryContainer
    }

    val formattedTime = remember(message.timestamp) {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        sdf.format(Date(message.timestamp))
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        // Sender Name indicator
        Text(
            text = message.sender,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = if (highContrast) Color.White else Color.Gray,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )

        Card(
            shape = RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 24.dp,
                bottomStart = if (isProfessional) 4.dp else 24.dp,
                bottomEnd = if (isProfessional) 24.dp else 4.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = bubbleColor,
                contentColor = contentColor
            ),
            border = if (highContrast) androidx.compose.foundation.BorderStroke(3.dp, Color.White) else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formattedTime,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (highContrast) Color.Cyan else Color.DarkGray,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}
