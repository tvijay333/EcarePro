package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.Appointment
import com.example.ui.viewmodel.HealthViewModel
import com.example.ui.theme.TealSecondary

@Composable
fun AppointmentsScreen(
    viewModel: HealthViewModel,
    appointments: List<Appointment>
) {
    val highContrast by viewModel.highContrastEnabled.collectAsState()
    var isAddingAppointment by remember { mutableStateOf(false) }

    var apptTitle by remember { mutableStateOf("") }
    var apptDoctor by remember { mutableStateOf("") }
    var apptDateTime by remember { mutableStateOf("") }
    var apptLocation by remember { mutableStateOf("") }

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
                    text = "Medical Appointment Manager",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = if (highContrast) Color.Yellow else MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Automated reminders are active for Arthur and caregivers.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (highContrast) Color.White else Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Add Appointment form toggle
        item {
            if (!isAddingAppointment) {
                Button(
                    onClick = { isAddingAppointment = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("add_appt_button"),
                    shape = RoundedCornerShape(32.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (highContrast) Color.Yellow else TealSecondary,
                        contentColor = if (highContrast) Color.Black else Color.White
                    )
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Schedule New Appointment", fontWeight = FontWeight.Bold)
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (highContrast) Color.White else Color(0xFFE2E8F0)),
                    colors = CardDefaults.cardColors(containerColor = if (highContrast) Color.Black else Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Schedule Appointment",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            value = apptTitle,
                            onValueChange = { apptTitle = it },
                            label = { Text("Appointment Title (e.g. Heart Checkup)") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("appt_title_input")
                        )

                        OutlinedTextField(
                            value = apptDoctor,
                            onValueChange = { apptDoctor = it },
                            label = { Text("Physician/Specialist Name") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("appt_doctor_input")
                        )

                        OutlinedTextField(
                            value = apptDateTime,
                            onValueChange = { apptDateTime = it },
                            placeholder = { Text("e.g. June 30, 2026 - 10:00 AM") },
                            label = { Text("Date & Time") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("appt_datetime_input")
                        )

                        OutlinedTextField(
                            value = apptLocation,
                            onValueChange = { apptLocation = it },
                            label = { Text("Location (or Video Consult Link)") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("appt_location_input")
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = { isAddingAppointment = false },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Cancel")
                            }

                            Button(
                                onClick = {
                                    if (apptTitle.isNotBlank() && apptDoctor.isNotBlank() && apptDateTime.isNotBlank()) {
                                        viewModel.addAppointment(apptTitle, apptDoctor, apptDateTime, apptLocation)
                                        apptTitle = ""
                                        apptDoctor = ""
                                        apptDateTime = ""
                                        apptLocation = ""
                                        isAddingAppointment = false
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (highContrast) Color.Yellow else MaterialTheme.colorScheme.primary,
                                    contentColor = if (highContrast) Color.Black else Color.White
                                )
                            ) {
                                Text("Schedule", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Active appointments list
        if (appointments.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(48.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No upcoming medical appointments scheduled.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )
                    }
                }
            }
        } else {
            items(appointments) { appt ->
                AppointmentCard(
                    appointment = appt,
                    highContrast = highContrast,
                    onDelete = { viewModel.deleteAppointment(appt) }
                )
            }
        }
    }
}

@Composable
fun AppointmentCard(
    appointment: Appointment,
    highContrast: Boolean,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = if (highContrast) Color.Black else Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (highContrast) Color.White else Color(0xFFE2E8F0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = appointment.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (highContrast) Color.Yellow else MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Physician: ${appointment.doctorName}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Cancel Appointment",
                        tint = if (highContrast) Color.White else Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Date and Time",
                    tint = if (highContrast) Color.Cyan else Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = appointment.dateTime,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (appointment.location.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Location",
                        tint = if (highContrast) Color.Cyan else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = appointment.location,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Reminder active banner
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notification active",
                    tint = if (highContrast) Color.Green else Color(0xFF4E9F3D),
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "AUTOMATED REMINDERS ACTIVE (SMS/PUSH)",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (highContrast) Color.Green else Color(0xFF4E9F3D)
                )
            }
        }
    }
}
