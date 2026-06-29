package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.model.Appointment
import com.example.data.model.Medication
import com.example.data.model.Message
import com.example.data.model.Vital
import com.example.data.model.UserStats
import com.example.data.repository.HealthRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

class HealthViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val repository = HealthRepository(
        database.medicationDao(),
        database.vitalDao(),
        database.appointmentDao(),
        database.messageDao(),
        database.userStatsDao()
    )

    // Exposed Flows from Room
    val medications: StateFlow<List<Medication>> = repository.medications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val vitals: StateFlow<List<Vital>> = repository.vitals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val appointments: StateFlow<List<Appointment>> = repository.appointments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val messages: StateFlow<List<Message>> = repository.messages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userStats: StateFlow<UserStats?> = repository.userStats
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // AI Virtual Companion State
    private val _companionPersonality = MutableStateFlow("Joyful Friend") // "Joyful Friend", "Wise Mentor", "Gentle Caregiver"
    val companionPersonality: StateFlow<String> = _companionPersonality.asStateFlow()

    private val _companionOutput = MutableStateFlow("Hi Arthur! I'm your Joyful Friend, Joy! It is so wonderful to see you today! Let's chat or check your daily reminders together! 😊🌟")
    val companionOutput: StateFlow<String> = _companionOutput.asStateFlow()

    private val _companionAffirmation = MutableStateFlow("")
    val companionAffirmation: StateFlow<String> = _companionAffirmation.asStateFlow()

    private val _isCompanionLoading = MutableStateFlow(false)
    val isCompanionLoading: StateFlow<Boolean> = _isCompanionLoading.asStateFlow()

    private val _isAffirmationLoading = MutableStateFlow(false)
    val isAffirmationLoading: StateFlow<Boolean> = _isAffirmationLoading.asStateFlow()

    // UI Navigation State
    // Tabs: "senior", "caregiver", "ai_diagnostics", "messaging", "video_feed", "appointments"
    private val _currentTab = MutableStateFlow("senior")
    val currentTab: StateFlow<String> = _currentTab.asStateFlow()

    // Accessibility Configuration
    private val _highContrastEnabled = MutableStateFlow(false)
    val highContrastEnabled: StateFlow<Boolean> = _highContrastEnabled.asStateFlow()

    private val _largeTextEnabled = MutableStateFlow(true) // Default to large text for seniors
    val largeTextEnabled: StateFlow<Boolean> = _largeTextEnabled.asStateFlow()

    // Emergency SOS State
    private val _sosActive = MutableStateFlow(false)
    val sosActive: StateFlow<Boolean> = _sosActive.asStateFlow()

    private val _sosCountdown = MutableStateFlow(5)
    val sosCountdown: StateFlow<Int> = _sosCountdown.asStateFlow()

    private val _sosDispatched = MutableStateFlow(false)
    val sosDispatched: StateFlow<Boolean> = _sosDispatched.asStateFlow()

    private var sosJob: Job? = null

    // Sync state for Wearables
    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    private val _syncProgress = MutableStateFlow(0f)
    val syncProgress: StateFlow<Float> = _syncProgress.asStateFlow()

    private val _syncMessage = MutableStateFlow("")
    val syncMessage: StateFlow<String> = _syncMessage.asStateFlow()

    // AI Diagnostics State
    private val _diagnosticsResponse = MutableStateFlow("")
    val diagnosticsResponse: StateFlow<String> = _diagnosticsResponse.asStateFlow()

    private val _isGeneratingDiagnostics = MutableStateFlow(false)
    val isGeneratingDiagnostics: StateFlow<Boolean> = _isGeneratingDiagnostics.asStateFlow()

    // Voice Activated Assistant State
    private val _voiceOutput = MutableStateFlow("Hello! I am your CareLink Companion. Tap the microphone and ask me anything, like 'What is my medication schedule?' or 'Suggest some health advice.'")
    val voiceOutput: StateFlow<String> = _voiceOutput.asStateFlow()

    private val _voiceInputText = MutableStateFlow("")
    val voiceInputText: StateFlow<String> = _voiceInputText.asStateFlow()

    private val _isVoiceLoading = MutableStateFlow(false)
    val isVoiceLoading: StateFlow<Boolean> = _isVoiceLoading.asStateFlow()

    // Video Feed Simulation State
    private val _isVideoMonitoring = MutableStateFlow(true)
    val isVideoMonitoring: StateFlow<Boolean> = _isVideoMonitoring.asStateFlow()

    private val _motionAlerts = MutableStateFlow<List<String>>(
        listOf(
            "08:15 AM - Motion detected: Safe sitting in recliner.",
            "09:30 AM - Activity analysis: Morning breathing exercises completed.",
            "11:00 AM - Wellness check: Safe hydration walk to kitchen."
        )
    )
    val motionAlerts: StateFlow<List<String>> = _motionAlerts.asStateFlow()

    // Heart Rate & Breath Rate simulated live overlaid parameters from AI Camera monitoring
    private val _liveCamHeartRate = MutableStateFlow(72)
    val liveCamHeartRate: StateFlow<Int> = _liveCamHeartRate.asStateFlow()

    private val _liveCamBreathRate = MutableStateFlow(16)
    val liveCamBreathRate: StateFlow<Int> = _liveCamBreathRate.asStateFlow()

    private val _liveCamMotionIndex = MutableStateFlow(0.12f)
    val liveCamMotionIndex: StateFlow<Float> = _liveCamMotionIndex.asStateFlow()

    init {
        viewModelScope.launch {
            // Seed database with mock data on startup
            repository.seedDefaultDataIfNeeded()
            // Start periodic updates for simulated camera vitals
            launch {
                while (true) {
                    delay(2500)
                    if (_isVideoMonitoring.value) {
                        _liveCamHeartRate.value = Random.nextInt(68, 79)
                        _liveCamBreathRate.value = Random.nextInt(14, 19)
                        _liveCamMotionIndex.value = Random.nextFloat() * 0.4f
                    }
                }
            }
        }
    }

    fun setTab(tab: String) {
        _currentTab.value = tab
    }

    fun toggleHighContrast() {
        _highContrastEnabled.value = !_highContrastEnabled.value
    }

    fun toggleLargeText() {
        _largeTextEnabled.value = !_largeTextEnabled.value
    }

    // Medication actions
    fun toggleMedication(medication: Medication) {
        viewModelScope.launch {
            repository.updateMedication(medication.copy(isTaken = !medication.isTaken))
            delay(300) // Small delay for database to write
            claimMedicationAdherenceReward()
        }
    }

    fun addMedication(name: String, dosage: String, time: String) {
        if (name.isBlank() || dosage.isBlank() || time.isBlank()) return
        viewModelScope.launch {
            repository.insertMedication(Medication(name = name, dosage = dosage, time = time, isTaken = false))
        }
    }

    fun deleteMedication(medication: Medication) {
        viewModelScope.launch {
            repository.deleteMedication(medication)
        }
    }

    // AI Companion Actions
    fun setCompanionPersonality(personality: String) {
        _companionPersonality.value = personality
        val welcomeMsg = when (personality) {
            "Joyful Friend" -> "Hi Arthur! I'm your Joyful Friend, Joy! It is so wonderful to see you today! Let's chat or check your daily reminders together! 😊🌟"
            "Wise Mentor" -> "Greetings, Arthur. I am Sage, your quiet mentor. Let us walk mindfully together, reflect on your reminders, and discover tranquility today. 📚🕊️"
            else -> "Hello dear Arthur, I am Grace, your gentle caregiver assistant. I hope you are feeling comfortable and at ease. How can I nurture and assist you today? 🌸🍵"
        }
        _companionOutput.value = welcomeMsg
    }

    fun submitCompanionChat(prompt: String) {
        if (prompt.isBlank()) return
        _isCompanionLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.generateCompanionResponse(
                    prompt = prompt,
                    personality = _companionPersonality.value,
                    medsList = medications.value,
                    apptsList = appointments.value
                )
                _companionOutput.value = response
            } catch (e: Exception) {
                _companionOutput.value = "Companion Connection Error: ${e.localizedMessage}"
            } finally {
                _isCompanionLoading.value = false
            }
        }
    }

    fun getCompanionAffirmation() {
        _isAffirmationLoading.value = true
        _companionAffirmation.value = "Seeking inspirational thoughts..."
        viewModelScope.launch {
            try {
                val response = repository.generatePositiveAffirmation(_companionPersonality.value)
                _companionAffirmation.value = response
            } catch (e: Exception) {
                _companionAffirmation.value = "Affirmation Error: ${e.localizedMessage}"
            } finally {
                _isAffirmationLoading.value = false
            }
        }
    }

    fun getCompanionReminders() {
        _isCompanionLoading.value = true
        _companionOutput.value = "Reviewing schedules and tasks..."
        viewModelScope.launch {
            try {
                val response = repository.generateWarmReminders(
                    personality = _companionPersonality.value,
                    medsList = medications.value,
                    apptsList = appointments.value
                )
                _companionOutput.value = response
            } catch (e: Exception) {
                _companionOutput.value = "Reminders Error: ${e.localizedMessage}"
            } finally {
                _isCompanionLoading.value = false
            }
        }
    }

    // Gamification Challenge Actions
    fun logHydrationGlass() {
        viewModelScope.launch {
            val stats = repository.getUserStats() ?: UserStats()
            val nextGlasses = stats.hydrationGlasses + 1
            var extraPoints = 0
            var newCompleted = stats.completedChallengesCsv
            var newBadges = stats.earnedBadgesCsv

            if (nextGlasses >= 8 && !stats.completedChallengesCsv.contains("hydration_daily")) {
                extraPoints = 30
                newCompleted = if (newCompleted.isEmpty()) "hydration_daily" else "$newCompleted,hydration_daily"
                newBadges = if (newBadges.isEmpty()) "💧 Hydration Hero" else "$newBadges, 💧 Hydration Hero"
                _voiceOutput.value = "🎉 Phenomenal! You've completed your Hydration Quest by drinking 8 glasses of water today! You earned 30 extra points and the 'Hydration Hero' badge!"
            }

            val updatedStats = stats.copy(
                hydrationGlasses = nextGlasses,
                points = stats.points + 5 + extraPoints,
                completedChallengesCsv = newCompleted,
                earnedBadgesCsv = newBadges
            )
            repository.insertUserStats(updatedStats)
        }
    }

    fun logWalkingMinutes(mins: Int) {
        if (mins <= 0) return
        viewModelScope.launch {
            val stats = repository.getUserStats() ?: UserStats()
            val nextMins = stats.walkingMinutes + mins
            var extraPoints = 0
            var newCompleted = stats.completedChallengesCsv
            var newBadges = stats.earnedBadgesCsv

            if (nextMins >= 15 && !stats.completedChallengesCsv.contains("walking_daily")) {
                extraPoints = 40
                newCompleted = if (newCompleted.isEmpty()) "walking_daily" else "$newCompleted,walking_daily"
                newBadges = if (newBadges.isEmpty()) "🏃 Active Senior" else "$newBadges, 🏃 Active Senior"
                _voiceOutput.value = "🎉 Amazing work, Arthur! You've completed your Active Senior Quest by walking 15 minutes today! You earned 40 extra points and the 'Active Senior' badge!"
            }

            val updatedStats = stats.copy(
                walkingMinutes = nextMins,
                points = stats.points + (mins * 2) + extraPoints,
                completedChallengesCsv = newCompleted,
                earnedBadgesCsv = newBadges
            )
            repository.insertUserStats(updatedStats)
        }
    }

    fun claimMedicationAdherenceReward() {
        viewModelScope.launch {
            val stats = repository.getUserStats() ?: UserStats()
            val medsList = medications.value
            val totalMeds = medsList.size
            val takenMeds = medsList.count { it.isTaken }

            if (takenMeds == totalMeds && totalMeds > 0 && !stats.completedChallengesCsv.contains("med_compliance_daily")) {
                val extraPoints = 50
                val newCompleted = if (stats.completedChallengesCsv.isEmpty()) "med_compliance_daily" else "${stats.completedChallengesCsv},med_compliance_daily"
                val newBadges = if (stats.earnedBadgesCsv.isEmpty()) "🥇 Medication Master" else "${stats.earnedBadgesCsv}, 🥇 Medication Master"

                val updatedStats = stats.copy(
                    points = stats.points + extraPoints,
                    completedChallengesCsv = newCompleted,
                    earnedBadgesCsv = newBadges
                )
                repository.insertUserStats(updatedStats)
                _voiceOutput.value = "🎉 Incredible! You've taken all of today's medications on time! You earned 50 points and the prestigious 'Medication Master' badge!"
            }
        }
    }

    // Vitals actions
    fun addVital(type: String, value: String, unit: String, status: String) {
        if (value.isBlank()) return
        viewModelScope.launch {
            repository.insertVital(Vital(type = type, value = value, unit = unit, status = status))
        }
    }

    fun deleteVital(vital: Vital) {
        viewModelScope.launch {
            repository.deleteVital(vital)
        }
    }

    // Appointment actions
    fun addAppointment(title: String, doctorName: String, dateTime: String, location: String) {
        if (title.isBlank() || doctorName.isBlank() || dateTime.isBlank()) return
        viewModelScope.launch {
            repository.insertAppointment(
                Appointment(
                    title = title,
                    doctorName = doctorName,
                    dateTime = dateTime,
                    location = location,
                    isReminderEnabled = true
                )
            )
        }
    }

    fun deleteAppointment(appointment: Appointment) {
        viewModelScope.launch {
            repository.deleteAppointment(appointment)
        }
    }

    // Message actions
    fun sendMessage(sender: String, content: String, isProfessional: Boolean) {
        if (content.isBlank()) return
        viewModelScope.launch {
            val recipient = if (isProfessional) "Marcus (Caregiver / Son)" else "Dr. Adrian Vance"
            repository.insertMessage(
                Message(
                    sender = sender,
                    recipient = recipient,
                    content = content,
                    isProfessional = isProfessional
                )
            )
            // If family member sends a message, let's trigger a simulated professional auto-reply in 3 seconds to keep UI lively!
            if (!isProfessional) {
                delay(3000)
                repository.insertMessage(
                    Message(
                        sender = "Dr. Adrian Vance",
                        recipient = "Marcus (Caregiver / Son)",
                        content = "Thank you for the update. I have reviewed the entry and it is noted. Keep monitoring his daily wellness parameters.",
                        isProfessional = true
                    )
                )
            }
        }
    }

    // Emergency SOS Triggering
    fun triggerSOS() {
        _sosActive.value = true
        _sosCountdown.value = 5
        _sosDispatched.value = false
        sosJob?.cancel()
        sosJob = viewModelScope.launch {
            while (_sosCountdown.value > 0) {
                delay(1000)
                _sosCountdown.value = _sosCountdown.value - 1
            }
            _sosDispatched.value = true
            // Insert emergency log to vitals list to indicate critical state logged
            repository.insertVital(Vital(type = "Emergency Alert", value = "SOS Triggered", unit = "Alert", status = "Critical"))
        }
    }

    fun cancelSOS() {
        sosJob?.cancel()
        _sosActive.value = false
        _sosDispatched.value = false
    }

    // Wearables sync logic
    fun syncWithWearables() {
        _isSyncing.value = true
        _syncProgress.value = 0f
        _syncMessage.value = "Initializing Bluetooth Smartwatch Link..."
        viewModelScope.launch {
            delay(1000)
            _syncProgress.value = 0.35f
            _syncMessage.value = "Reading Blood Pressure Monitor Cuff (BLE)..."
            delay(1000)
            _syncProgress.value = 0.70f
            _syncMessage.value = "Syncing Heart Rate and Pulse Oximetry Data..."
            delay(1000)
            _syncProgress.value = 1f
            _syncMessage.value = "Transferring telemetry payload to secure medical database..."
            delay(500)

            // Actually generate real-time vitals into db
            val synced = repository.syncWithWearables()
            _isSyncing.value = false
            _syncMessage.value = "Successfully synced ${synced.size} devices! Vitals database updated in real-time."
        }
    }

    // AI Diagnostics Insights Generation
    fun runAiDiagnostics() {
        _isGeneratingDiagnostics.value = true
        _diagnosticsResponse.value = "Processing behavioral telemetry and compliance trends..."
        viewModelScope.launch {
            try {
                val response = repository.getGeminiDiagnostics(vitals.value, medications.value)
                _diagnosticsResponse.value = response
            } catch (e: Exception) {
                _diagnosticsResponse.value = "Diagnostics generation failed: ${e.localizedMessage}"
            } finally {
                _isGeneratingDiagnostics.value = false
            }
        }
    }

    // Voice Activation processing
    fun submitVoiceCommand(command: String) {
        if (command.isBlank()) return
        _voiceInputText.value = command
        _isVoiceLoading.value = true
        _voiceOutput.value = "Listening & analyzing request..."
        viewModelScope.launch {
            try {
                // Check for quick static actions
                val queryLower = command.lowercase()
                if (queryLower.contains("sos") || queryLower.contains("emergency") || queryLower.contains("help")) {
                    _voiceOutput.value = "Emergency SOS requested. Triggering primary distress sequence!"
                    triggerSOS()
                } else if (queryLower.contains("sync") || queryLower.contains("wearable") || queryLower.contains("smartwatch")) {
                    _voiceOutput.value = "Initiating device synchronization!"
                    syncWithWearables()
                } else {
                    // Call Gemini voice model config
                    val response = repository.processVoiceCommand(command, vitals.value, medications.value)
                    _voiceOutput.value = response
                }
            } catch (e: Exception) {
                _voiceOutput.value = "Voice Assistant Error: ${e.localizedMessage}"
            } finally {
                _isVoiceLoading.value = false
            }
        }
    }

    fun toggleVideoMonitoring() {
        _isVideoMonitoring.value = !_isVideoMonitoring.value
        if (_isVideoMonitoring.value) {
            _motionAlerts.value = _motionAlerts.value + "System re-initialized: Active vision diagnostics online."
        } else {
            _motionAlerts.value = _motionAlerts.value + "System deactivated: Room vision scanning paused."
        }
    }

    fun simulateMotionFallAlert() {
        viewModelScope.launch {
            val alert = "🚨 WARNING - AI detection alert: Room fall hazard identified! Verifying motion state..."
            _motionAlerts.value = listOf(alert) + _motionAlerts.value
            _liveCamMotionIndex.value = 0.95f
            delay(1500)
            _motionAlerts.value = listOf("🚨 VERIFIED - Assisted senior recovered securely. No manual intervention required.") + _motionAlerts.value
        }
    }
}
