package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medications")
data class Medication(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val dosage: String,
    val time: String,
    val isTaken: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "vitals")
data class Vital(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String, // "Heart Rate", "Blood Pressure", "Blood Glucose", "Blood Oxygen", "Body Temp"
    val value: String,
    val unit: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String // "Normal", "Warning", "Critical"
)

@Entity(tableName = "appointments")
data class Appointment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val doctorName: String,
    val dateTime: String,
    val location: String,
    val isReminderEnabled: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sender: String,
    val recipient: String,
    val content: String,
    val isProfessional: Boolean, // true = from professional, false = from family
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_stats")
data class UserStats(
    @PrimaryKey val id: Int = 1,
    val points: Int = 0,
    val hydrationGlasses: Int = 0,
    val walkingMinutes: Int = 0,
    val completedChallengesCsv: String = "",
    val earnedBadgesCsv: String = ""
)

