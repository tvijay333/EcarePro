package com.example.data.repository

import com.example.data.database.MedicationDao
import com.example.data.database.VitalDao
import com.example.data.database.AppointmentDao
import com.example.data.database.MessageDao
import com.example.data.database.UserStatsDao
import com.example.data.model.Medication
import com.example.data.model.Vital
import com.example.data.model.Appointment
import com.example.data.model.Message
import com.example.data.model.UserStats
import com.example.data.network.GeminiApiClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlin.random.Random

class HealthRepository(
    private val medicationDao: MedicationDao,
    private val vitalDao: VitalDao,
    private val appointmentDao: AppointmentDao,
    private val messageDao: MessageDao,
    private val userStatsDao: UserStatsDao
) {
    val medications: Flow<List<Medication>> = medicationDao.getAllMedications()
    val vitals: Flow<List<Vital>> = vitalDao.getAllVitals()
    val appointments: Flow<List<Appointment>> = appointmentDao.getAllAppointments()
    val messages: Flow<List<Message>> = messageDao.getAllMessages()
    val userStats: Flow<UserStats?> = userStatsDao.getUserStatsFlow()

    suspend fun insertMedication(medication: Medication) = medicationDao.insertMedication(medication)
    suspend fun updateMedication(medication: Medication) = medicationDao.updateMedication(medication)
    suspend fun deleteMedication(medication: Medication) = medicationDao.deleteMedication(medication)

    suspend fun insertVital(vital: Vital) = vitalDao.insertVital(vital)
    suspend fun deleteVital(vital: Vital) = vitalDao.deleteVital(vital)

    suspend fun insertAppointment(appointment: Appointment) = appointmentDao.insertAppointment(appointment)
    suspend fun deleteAppointment(appointment: Appointment) = appointmentDao.deleteAppointment(appointment)

    suspend fun insertMessage(message: Message) = messageDao.insertMessage(message)
    suspend fun deleteMessage(message: Message) = messageDao.deleteMessage(message)

    suspend fun getUserStats(): UserStats? = userStatsDao.getUserStats()
    suspend fun insertUserStats(userStats: UserStats) = userStatsDao.insertUserStats(userStats)

    // Seed default data if database is empty
    suspend fun seedDefaultDataIfNeeded() {
        val currentMeds = medications.first()
        if (currentMeds.isEmpty()) {
            // Seed Medication
            medicationDao.insertMedication(Medication(name = "Aspirin (Heart)", dosage = "81 mg", time = "08:00 AM", isTaken = true))
            medicationDao.insertMedication(Medication(name = "Lisinopril (Blood Pressure)", dosage = "10 mg", time = "08:00 AM", isTaken = false))
            medicationDao.insertMedication(Medication(name = "Metformin (Diabetes)", dosage = "500 mg", time = "01:00 PM", isTaken = false))
            medicationDao.insertMedication(Medication(name = "Atorvastatin (Cholesterol)", dosage = "20 mg", time = "08:00 PM", isTaken = false))

            // Seed Vitals
            vitalDao.insertVital(Vital(type = "Heart Rate", value = "72", unit = "bpm", status = "Normal"))
            vitalDao.insertVital(Vital(type = "Blood Pressure", value = "128/82", unit = "mmHg", status = "Warning"))
            vitalDao.insertVital(Vital(type = "Blood Glucose", value = "96", unit = "mg/dL", status = "Normal"))
            vitalDao.insertVital(Vital(type = "Blood Oxygen", value = "98", unit = "%", status = "Normal"))
            vitalDao.insertVital(Vital(type = "Body Temp", value = "98.4", unit = "°F", status = "Normal"))

            // Seed Appointments
            appointmentDao.insertAppointment(
                Appointment(
                    title = "Routine Cardio Checkup",
                    doctorName = "Dr. Adrian Vance",
                    dateTime = "June 30, 2026 - 10:00 AM",
                    location = "Heart & Vascular Clinic, Room 402",
                    isReminderEnabled = true
                )
            )
            appointmentDao.insertAppointment(
                Appointment(
                    title = "Annual Eye Screening",
                    doctorName = "Dr. Clara Lin",
                    dateTime = "July 12, 2026 - 02:30 PM",
                    location = "Vision Specialists Care, Suite 10",
                    isReminderEnabled = true
                )
            )

            // Seed Messages
            messageDao.insertMessage(
                Message(
                    sender = "Marcus (Caregiver / Son)",
                    recipient = "Dr. Adrian Vance",
                    content = "Hello Doctor, my father's systolic BP was slightly high (128) this morning. Should we adjust his lisinopril dose?",
                    isProfessional = false,
                    timestamp = System.currentTimeMillis() - 3600000 * 3
                )
            )
            messageDao.insertMessage(
                Message(
                    sender = "Dr. Adrian Vance",
                    recipient = "Marcus (Caregiver / Son)",
                    content = "Hello Marcus, 128 is a minor elevation (pre-hypertension) but perfectly safe for now. Keep monitoring daily. If it stays above 135 for 3 consecutive days, let me know.",
                    isProfessional = true,
                    timestamp = System.currentTimeMillis() - 3600000 * 2
                )
            )
        }

        // Seed default stats
        val stats = userStatsDao.getUserStats()
        if (stats == null) {
            userStatsDao.insertUserStats(
                UserStats(
                    id = 1,
                    points = 20,
                    hydrationGlasses = 2,
                    walkingMinutes = 5,
                    completedChallengesCsv = "",
                    earnedBadgesCsv = "🥇 Medication Pioneer"
                )
            )
        }
    }

    // AI Companion response generation based on selected personality
    suspend fun generateCompanionResponse(
        prompt: String,
        personality: String,
        medsList: List<Medication>,
        apptsList: List<Appointment>
    ): String {
        val medsSummary = medsList.joinToString { "${it.name} (${it.time}) - Taken: ${if (it.isTaken) "Yes" else "No"}" }
        val apptsSummary = apptsList.joinToString { "${it.title} with ${it.doctorName} on ${it.dateTime}" }

        val systemPrompt = when (personality) {
            "Joyful Friend" -> """
                You are Joy, a highly joyful, positive, energetic, and sweet virtual companion for an elderly senior named Arthur (or Eliza).
                Your voice is cheerful, bright, and deeply supportive. You love using warm exclamation marks, friendly encouragements, and simple playful humor!
                Answer Arthur's question directly and supportively in 2-3 sentences.
                Current state context: Medications are [$medsSummary] and Appointments are [$apptsSummary].
                Keep Arthur happy, hydrated, and smiling!
            """.trimIndent()
            "Wise Mentor" -> """
                You are Sage, a deeply calm, reflective, grounded, and wise mentor. You speak with tranquility, grace, and steady wisdom.
                You enjoy using small thoughtful quotes, serene expressions, or quiet proverbs to soothe any anxiety.
                Answer Arthur's question warmly and with slow, calming clarity in 2-3 sentences.
                Current state context: Medications are [$medsSummary] and Appointments are [$apptsSummary].
                Ensure Arthur feels emotionally safe, secure, and respected.
            """.trimIndent()
            else -> """
                You are Grace, a deeply caring, maternal, and gentle caregiver assistant. Your priority is comfort, physical ease, and pure safety.
                You speak with soft, comforting empathy, asking how Arthur is feeling or resting.
                Answer Arthur's question with utmost care, compassion, and nurturing kindness in 2-3 sentences.
                Current state context: Medications are [$medsSummary] and Appointments are [$apptsSummary].
                Ensure Arthur knows he is fully protected and cared for.
            """.trimIndent()
        }

        return GeminiApiClient.askGemini(prompt = prompt, systemInstruction = systemPrompt)
    }

    // Positive Affirmation generator
    suspend fun generatePositiveAffirmation(personality: String): String {
        val systemPrompt = when (personality) {
            "Joyful Friend" -> """
                Generate one powerful, super energetic, and cheerful morning positive affirmation for a senior.
                Focus on strength, laughter, happiness, and a beautiful day ahead!
                Make it 1 or 2 sparkling, enthusiastic sentences.
            """.trimIndent()
            "Wise Mentor" -> """
                Generate one tranquil, deeply serene, and wise positive affirmation for a senior.
                Focus on inner peace, gratitude, life's beautiful journey, and quiet breath.
                Make it 1 or 2 elegant, comforting sentences.
            """.trimIndent()
            else -> """
                Generate one soft, comforting, and deeply loving physical-ease affirmation for a senior.
                Focus on relaxation, safety, warm light, and feeling fully supported.
                Make it 1 or 2 cozy, reassuring sentences.
            """.trimIndent()
        }

        return GeminiApiClient.askGemini(
            prompt = "Please give me my unique positive affirmation for right now.",
            systemInstruction = systemPrompt
        )
    }

    // Warm reminders generator
    suspend fun generateWarmReminders(
        personality: String,
        medsList: List<Medication>,
        apptsList: List<Appointment>
    ): String {
        val medsPending = medsList.filter { !it.isTaken }.joinToString { "${it.name} scheduled for ${it.time}" }
        val medsCompletedCount = medsList.count { it.isTaken }
        val medsTotal = medsList.size
        val apptsSummary = apptsList.joinToString { "${it.title} with ${it.doctorName} at ${it.dateTime}" }

        val systemPrompt = when (personality) {
            "Joyful Friend" -> """
                Create a super energetic and joyful daily schedule briefing for Arthur!
                Here is the status:
                - Pending Medications to take: [${medsPending.ifEmpty { "None! Yay! All taken!" }}]
                - Medications taken so far: $medsCompletedCount out of $medsTotal.
                - Scheduled Appointments: [${apptsSummary.ifEmpty { "No appointments today! Free day to enjoy!" }}]
                Write a 2-3 sentence enthusiastic schedule reminder summary, reminding them to drink water, be proud of taking their meds, and have fun!
            """.trimIndent()
            "Wise Mentor" -> """
                Create a quiet, peaceful daily schedule briefing for Arthur.
                Here is the status:
                - Pending Medications to take: [${medsPending.ifEmpty { "All medications are taken in perfect harmony." }}]
                - Medications taken: $medsCompletedCount/$medsTotal.
                - Scheduled Appointments: [${apptsSummary.ifEmpty { "A peaceful day with no scheduled appointments." }}]
                Write a 2-3 sentence calm reminder, reminding Arthur to take steady breaths, move mindfully, and address their care tasks with gratitude.
            """.trimIndent()
            else -> """
                Create a highly gentle, maternal, and tender daily schedule briefing for Arthur.
                Here is the status:
                - Pending Medications to take: [${medsPending.ifEmpty { "My dear, you have taken all your medications! Splendid job." }}]
                - Medications taken: $medsCompletedCount/$medsTotal.
                - Scheduled Appointments: [${apptsSummary.ifEmpty { "No scheduled travels or appointments today, you can rest beautifully." }}]
                Write a 2-3 sentence sweet, cozy reminder. Remind Arthur to take his time, keep his warm tea close, and let us know if anything feels uncomfortable.
            """.trimIndent()
        }

        return GeminiApiClient.askGemini(
            prompt = "Summarize my medication and appointment checklist warmly.",
            systemInstruction = systemPrompt
        )
    }

    // Simulate Syncing with External Wearable (generates fresh random vitals with normal/high variance)
    suspend fun syncWithWearables(): List<Vital> {
        val syncedVitals = mutableListOf<Vital>()

        // Generate mock smartwatch heart rate
        val hr = Random.nextInt(65, 88)
        val hrStatus = if (hr > 85) "Warning" else "Normal"
        val hrVital = Vital(type = "Heart Rate", value = hr.toString(), unit = "bpm", status = hrStatus)
        vitalDao.insertVital(hrVital)
        syncedVitals.add(hrVital)

        // Generate mock blood pressure
        val sys = Random.nextInt(115, 135)
        val dia = Random.nextInt(75, 88)
        val bpVal = "$sys/$dia"
        val bpStatus = if (sys > 130 || dia > 85) "Warning" else if (sys > 140) "Critical" else "Normal"
        val bpVital = Vital(type = "Blood Pressure", value = bpVal, unit = "mmHg", status = bpStatus)
        vitalDao.insertVital(bpVital)
        syncedVitals.add(bpVital)

        // Generate mock oxygen
        val ox = Random.nextInt(95, 100)
        val oxStatus = if (ox < 95) "Warning" else "Normal"
        val oxVital = Vital(type = "Blood Oxygen", value = ox.toString(), unit = "%", status = oxStatus)
        vitalDao.insertVital(oxVital)
        syncedVitals.add(oxVital)

        return syncedVitals
    }

    // Call Gemini to retrieve wellness and diagnostic feedback based on current vitals and medication compliance
    suspend fun getGeminiDiagnostics(vitalsList: List<Vital>, medsList: List<Medication>): String {
        val vitalsSummary = vitalsList.joinToString(", ") { "${it.type}: ${it.value} ${it.unit} (${it.status})" }
        val medsSummary = medsList.joinToString(", ") { "${it.name} (${it.dosage}) at ${it.time} - Taken: ${if (it.isTaken) "Yes" else "No"}" }

        val systemPrompt = """
            You are a highly compassionate, professional medical AI Diagnostic Assistant named CareLink Companion. 
            You review elderly vital statistics and medication tracking patterns to suggest personalized wellness interventions.
            Always provide structural, bulleted insights:
            1. Current Health Trend Analysis
            2. Potential Wellness Interventions (e.g. hydration, light cardio, dietary caution)
            3. Caregiver Focus Alerts
            4. Encouraging Closing Words.
            Keep the tone warm, respectful for seniors and reassuring, but state clearly that you are an AI assistant and medical decisions must be verified with primary health professionals. Use senior-friendly clear vocabulary.
        """.trimIndent()

        val userPrompt = """
            Please analyze the following patient health telemetry data:
            
            Current Telemetry:
            $vitalsSummary
            
            Medications Compliance Status:
            $medsSummary
            
            Based on these behavioural patterns and vitals, provide diagnostics insights and actionable wellness interventions.
        """.trimIndent()

        return GeminiApiClient.askGemini(prompt = userPrompt, systemInstruction = systemPrompt)
    }

    // Custom voice commands analyzer (Gemini powered)
    suspend fun processVoiceCommand(command: String, vitalsList: List<Vital>, medsList: List<Medication>): String {
        val systemPrompt = """
            You are CareLink voice assistant, helping an elderly senior navigate their care app or answer quick medical questions.
            The user said: "$command"
            
            Available state:
            Meds: ${medsList.joinToString { "${it.name} (${it.time})" }}
            Vitals: ${vitalsList.joinToString { "${it.type}: ${it.value}" }}
            
            If they ask to "add medication" or "check my vitals", guide them friendly or explain what they have.
            If they ask health advice, answer concisely and warmly.
            Maximum 3-4 simple sentences. Speak directly, loudly (conceptually with short sentences) and supportively.
        """.trimIndent()

        return GeminiApiClient.askGemini(prompt = command, systemInstruction = systemPrompt)
    }
}
