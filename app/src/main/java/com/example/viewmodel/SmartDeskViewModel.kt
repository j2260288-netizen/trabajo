package com.example.viewmodel

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.model.DeskFile
import com.example.model.DeskSettings
import com.example.model.DrawStroke
import com.example.model.FileType
import com.example.model.MusicTrack
import com.example.model.NavDestination
import com.example.model.NotificationCategory
import com.example.model.NotificationItem
import com.example.model.SensorData
import com.example.ui.theme.NeonCyan
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SmartDeskViewModel : ViewModel() {

    private val _currentDestination = MutableStateFlow(NavDestination.HOME)
    val currentDestination: StateFlow<NavDestination> = _currentDestination.asStateFlow()

    private val navigationBackStack = mutableListOf<NavDestination>()

    // Media Player
    private val defaultTracks = listOf(
        MusicTrack(
            id = "track_1",
            title = "Cybernetic Echoes",
            artist = "Synth Waiver (Master 96kHz)",
            durationSeconds = 248,
            albumDrawableRes = R.drawable.img_album_synthwave,
            genre = "Hi-Res Cyber Ambient"
        ),
        MusicTrack(
            id = "track_2",
            title = "Glass Horizon (IPS Edition)",
            artist = "Aura Matrix",
            durationSeconds = 195,
            albumDrawableRes = R.drawable.ic_smartdesk_fg,
            genre = "Minimalist Electronic"
        ),
        MusicTrack(
            id = "track_3",
            title = "Obsidian Waves",
            artist = "Subtle Pulse Lab",
            durationSeconds = 220,
            albumDrawableRes = R.drawable.img_album_synthwave,
            genre = "Deep Focus Techno"
        )
    )

    private val _tracks = MutableStateFlow(defaultTracks)
    val tracks: StateFlow<List<MusicTrack>> = _tracks.asStateFlow()

    private val _currentTrackIndex = MutableStateFlow(0)
    val currentTrackIndex: StateFlow<Int> = _currentTrackIndex.asStateFlow()

    private val _isPlaying = MutableStateFlow(true)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _trackPosition = MutableStateFlow(42)
    val trackPosition: StateFlow<Int> = _trackPosition.asStateFlow()

    private val _isMediaExpanded = MutableStateFlow(false)
    val isMediaExpanded: StateFlow<Boolean> = _isMediaExpanded.asStateFlow()

    private val _volume = MutableStateFlow(0.72f)
    val volume: StateFlow<Float> = _volume.asStateFlow()

    // Notification Center
    private val initialNotifications = listOf(
        NotificationItem(
            id = "notif_1",
            title = "Superficie Qi Activa",
            message = "Dispositivo móvil detectado en cuadrante izquierdo. Carga inductiva rápida a 15W.",
            timestamp = "Hace 2 min",
            category = NotificationCategory.QI_CHARGING
        ),
        NotificationItem(
            id = "notif_2",
            title = "Alerta Ergonómica",
            message = "Llevas 50 min en modo sentado. ¿Deseas elevar la mesa a 105 cm?",
            timestamp = "Hace 12 min",
            category = NotificationCategory.ERGONOMICS
        ),
        NotificationItem(
            id = "notif_3",
            title = "Calibración IPS TrueTone",
            message = "Sensor ambiental registró 360 lx. Temperatura de color ajustada a 5600K.",
            timestamp = "Hace 35 min",
            category = NotificationCategory.DESK_SENSOR
        )
    )

    private val _notifications = MutableStateFlow(initialNotifications)
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    private val _isNotificationCenterOpen = MutableStateFlow(false)
    val isNotificationCenterOpen: StateFlow<Boolean> = _isNotificationCenterOpen.asStateFlow()

    // Virtual Keyboard & Text Input
    private val _isKeyboardVisible = MutableStateFlow(false)
    val isKeyboardVisible: StateFlow<Boolean> = _isKeyboardVisible.asStateFlow()

    private val _activeInputTarget = MutableStateFlow("QUICK_NOTE")
    val activeInputTarget: StateFlow<String> = _activeInputTarget.asStateFlow()

    private val _quickNoteText = MutableStateFlow("Revisar planos CAD v2.4 y calibrar respuesta háptica de la pantalla de 20x20 cm.")
    val quickNoteText: StateFlow<String> = _quickNoteText.asStateFlow()

    private val _browserUrl = MutableStateFlow("https://smartdesk.internal/blueprint")
    val browserUrl: StateFlow<String> = _browserUrl.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _keyboardGestureHint = MutableStateFlow<String?>(null)
    val keyboardGestureHint: StateFlow<String?> = _keyboardGestureHint.asStateFlow()

    // Drawing Canvas
    private val _strokes = MutableStateFlow<List<DrawStroke>>(emptyList())
    val strokes: StateFlow<List<DrawStroke>> = _strokes.asStateFlow()

    private val _currentStrokePoints = MutableStateFlow<List<Offset>>(emptyList())
    val currentStrokePoints: StateFlow<List<Offset>> = _currentStrokePoints.asStateFlow()

    private val _selectedColor = MutableStateFlow(NeonCyan)
    val selectedColor: StateFlow<Color> = _selectedColor.asStateFlow()

    private val _strokeWidth = MutableStateFlow(6f)
    val strokeWidth: StateFlow<Float> = _strokeWidth.asStateFlow()

    // Desk Files
    private val initialFiles = listOf(
        DeskFile(
            id = "file_1",
            name = "Estructura_Mesa_20x20.cad",
            extension = "CAD",
            size = "14.2 MB",
            date = "Hoy, 10:15 AM",
            type = FileType.CAD_BLUEPRINT,
            description = "Plano técnico modular para marco de titanio cepillado y montaje de display IPS LCD 20x20 cm."
        ),
        DeskFile(
            id = "file_2",
            name = "Esquema_Lienzo_Boceto_01.dwg",
            extension = "DWG",
            size = "3.8 MB",
            date = "Ayer, 18:40",
            type = FileType.DRAWING,
            description = "Boceto a mano alzada del mapa de circuitos táctiles y bobinas Qi integradas."
        ),
        DeskFile(
            id = "file_3",
            name = "Manual_Usuario_IPS_Mesa.pdf",
            extension = "PDF",
            size = "2.1 MB",
            date = "24 Oct 2026",
            type = FileType.DOCUMENT,
            description = "Especificaciones de tasa de refresco 120Hz, cobertura sRGB 99.8% y capa oleofóbica de zafiro."
        ),
        DeskFile(
            id = "file_4",
            name = "Telemetria_Sensores_Log.json",
            extension = "JSON",
            size = "512 KB",
            date = "25 Oct 2026",
            type = FileType.SYSTEM_LOG,
            description = "Registros de temperatura superficial, consumo de energía y ciclos de elevación."
        )
    )

    private val _files = MutableStateFlow(initialFiles)
    val files: StateFlow<List<DeskFile>> = _files.asStateFlow()

    private val _selectedFile = MutableStateFlow<DeskFile?>(initialFiles.first())
    val selectedFile: StateFlow<DeskFile?> = _selectedFile.asStateFlow()

    // Sensors & Desk Settings
    private val _sensorData = MutableStateFlow(SensorData())
    val sensorData: StateFlow<SensorData> = _sensorData.asStateFlow()

    private val _deskSettings = MutableStateFlow(DeskSettings())
    val deskSettings: StateFlow<DeskSettings> = _deskSettings.asStateFlow()

    // Real-time clock string
    private val _currentTimeString = MutableStateFlow("")
    val currentTimeString: StateFlow<String> = _currentTimeString.asStateFlow()

    private var playbackJob: Job? = null
    private var clockJob: Job? = null
    private var heightAdjustmentJob: Job? = null

    init {
        startClock()
        startPlaybackSimulation()
    }

    private fun startClock() {
        clockJob?.cancel()
        clockJob = viewModelScope.launch {
            val format = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            while (true) {
                _currentTimeString.value = format.format(Date())
                delay(1000)
            }
        }
    }

    private fun startPlaybackSimulation() {
        playbackJob?.cancel()
        playbackJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                if (_isPlaying.value) {
                    val currentTrack = _tracks.value[_currentTrackIndex.value]
                    _trackPosition.update { pos ->
                        if (pos + 1 >= currentTrack.durationSeconds) {
                            nextTrack()
                            0
                        } else {
                            pos + 1
                        }
                    }
                }
            }
        }
    }

    // Navigation
    fun navigateTo(destination: NavDestination) {
        if (_currentDestination.value != destination) {
            navigationBackStack.add(_currentDestination.value)
            _currentDestination.value = destination
        }
        // Auto-close notifications if changing screens
        _isNotificationCenterOpen.value = false
    }

    fun navigateBack(): Boolean {
        return if (navigationBackStack.isNotEmpty()) {
            _currentDestination.value = navigationBackStack.removeAt(navigationBackStack.size - 1)
            true
        } else if (_currentDestination.value != NavDestination.HOME) {
            _currentDestination.value = NavDestination.HOME
            true
        } else {
            false
        }
    }

    // Media Controls
    fun togglePlayPause() {
        _isPlaying.update { !it }
    }

    fun nextTrack() {
        _currentTrackIndex.update { (it + 1) % _tracks.value.size }
        _trackPosition.value = 0
    }

    fun previousTrack() {
        _currentTrackIndex.update {
            if (it - 1 < 0) _tracks.value.size - 1 else it - 1
        }
        _trackPosition.value = 0
    }

    fun selectTrack(index: Int) {
        if (index in _tracks.value.indices) {
            _currentTrackIndex.value = index
            _trackPosition.value = 0
            _isPlaying.value = true
        }
    }

    fun toggleMediaExpanded() {
        _isMediaExpanded.update { !it }
    }

    fun setVolume(vol: Float) {
        _volume.value = vol.coerceIn(0f, 1f)
    }

    fun seekTo(seconds: Int) {
        _trackPosition.value = seconds.coerceIn(0, _tracks.value[_currentTrackIndex.value].durationSeconds)
    }

    // Notification Center
    fun toggleNotificationCenter() {
        _isNotificationCenterOpen.update { !it }
    }

    fun closeNotificationCenter() {
        _isNotificationCenterOpen.value = false
    }

    fun dismissNotification(id: String) {
        _notifications.update { list -> list.filter { it.id != id } }
    }

    fun clearAllNotifications() {
        _notifications.value = emptyList()
    }

    fun triggerDemoNotification() {
        val newNotif = NotificationItem(
            id = "notif_${System.currentTimeMillis()}",
            title = "Ajuste de Luz Ambiental",
            message = "Brillo de pantalla recalculado dinámicamente a ${(70..95).random()}% con cobertura antirreflejo.",
            timestamp = "Ahora",
            category = NotificationCategory.DESK_SENSOR
        )
        _notifications.update { listOf(newNotif) + it }
    }

    // Keyboard & Gestures
    fun showKeyboard(targetInput: String = "QUICK_NOTE") {
        _activeInputTarget.value = targetInput
        _isKeyboardVisible.value = true
    }

    fun hideKeyboard() {
        _isKeyboardVisible.value = false
    }

    fun toggleKeyboard() {
        _isKeyboardVisible.update { !it }
    }

    fun triggerThreeFingerGesture() {
        _isKeyboardVisible.value = true
        _keyboardGestureHint.value = "🖐️ Gesto de 3 dedos detectado: Teclado táctil desplegado"
        viewModelScope.launch {
            delay(3500)
            _keyboardGestureHint.value = null
        }
    }

    fun onKeyPress(key: String) {
        when (_activeInputTarget.value) {
            "QUICK_NOTE" -> {
                _quickNoteText.update { it + key }
            }
            "BROWSER_URL" -> {
                _browserUrl.update { it + key }
            }
            "SEARCH" -> {
                _searchQuery.update { it + key }
            }
        }
    }

    fun onBackspace() {
        when (_activeInputTarget.value) {
            "QUICK_NOTE" -> {
                _quickNoteText.update { if (it.isNotEmpty()) it.dropLast(1) else "" }
            }
            "BROWSER_URL" -> {
                _browserUrl.update { if (it.isNotEmpty()) it.dropLast(1) else "" }
            }
            "SEARCH" -> {
                _searchQuery.update { if (it.isNotEmpty()) it.dropLast(1) else "" }
            }
        }
    }

    fun onEnter() {
        if (_activeInputTarget.value == "QUICK_NOTE") {
            _quickNoteText.update { "$it\n" }
        } else {
            hideKeyboard()
        }
    }

    fun clearActiveInput() {
        when (_activeInputTarget.value) {
            "QUICK_NOTE" -> _quickNoteText.value = ""
            "BROWSER_URL" -> _browserUrl.value = ""
            "SEARCH" -> _searchQuery.value = ""
        }
    }

    fun updateQuickNote(text: String) {
        _quickNoteText.value = text
    }

    fun updateBrowserUrl(url: String) {
        _browserUrl.value = url
    }

    // Drawing Canvas
    fun startStroke(offset: Offset) {
        _currentStrokePoints.value = listOf(offset)
    }

    fun addPoint(offset: Offset) {
        _currentStrokePoints.update { it + offset }
    }

    fun endStroke() {
        val points = _currentStrokePoints.value
        if (points.isNotEmpty()) {
            val stroke = DrawStroke(
                points = points,
                color = _selectedColor.value,
                strokeWidth = _strokeWidth.value
            )
            _strokes.update { it + stroke }
            _currentStrokePoints.value = emptyList()
        }
    }

    fun setDrawColor(color: Color) {
        _selectedColor.value = color
    }

    fun setStrokeWidth(width: Float) {
        _strokeWidth.value = width
    }

    fun undoStroke() {
        _strokes.update { if (it.isNotEmpty()) it.dropLast(1) else it }
    }

    fun clearCanvas() {
        _strokes.value = emptyList()
        _currentStrokePoints.value = emptyList()
    }

    fun saveDrawingToFiles(customName: String? = null) {
        val name = customName ?: "Boceto_Mesa_${System.currentTimeMillis() % 1000}.dwg"
        val newFile = DeskFile(
            id = "file_${System.currentTimeMillis()}",
            name = name,
            extension = "DWG",
            size = "${(1..5).random()}.${(1..9).random()} MB",
            date = "Hoy, " + SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
            type = FileType.DRAWING,
            description = "Dibujo interactivo exportado desde el lienzo táctil IPS de la mesa inteligente."
        )
        _files.update { listOf(newFile) + it }
        _selectedFile.value = newFile
        _keyboardGestureHint.value = "💾 Archivo guardado con éxito en Archivos"
        viewModelScope.launch {
            delay(3000)
            if (_keyboardGestureHint.value?.startsWith("💾") == true) {
                _keyboardGestureHint.value = null
            }
        }
    }

    // File Manager
    fun selectFile(file: DeskFile) {
        _selectedFile.value = file
    }

    // Desk Controls & Ergonomics
    fun setDeskHeightTarget(targetCm: Int) {
        val bounded = targetCm.coerceIn(68, 120)
        _sensorData.update { it.copy(targetHeightCm = bounded, isStandingMode = bounded >= 95) }

        heightAdjustmentJob?.cancel()
        heightAdjustmentJob = viewModelScope.launch {
            while (_sensorData.value.deskHeightCm != bounded) {
                delay(80)
                _sensorData.update {
                    val current = it.deskHeightCm
                    val step = if (current < bounded) 1 else -1
                    it.copy(deskHeightCm = current + step)
                }
            }
        }
    }

    fun toggleQiCharging() {
        _sensorData.update {
            val nextState = !it.qiChargerActive
            it.copy(
                qiChargerActive = nextState,
                qiPowerWatts = if (nextState) 15.0f else 0.0f
            )
        }
    }

    fun toggleSquareAspectMode() {
        _deskSettings.update { it.copy(squareAspectLocked = !it.squareAspectLocked) }
    }

    fun setIpsBrightness(brightness: Float) {
        _deskSettings.update { it.copy(ipsBrightness = brightness.coerceIn(0.1f, 1f)) }
    }

    fun toggleTrueTone() {
        _deskSettings.update { it.copy(trueToneAuto = !it.trueToneAuto) }
    }

    fun toggleHaptic() {
        _deskSettings.update { it.copy(hapticFeedbackEnabled = !it.hapticFeedbackEnabled) }
    }
}
