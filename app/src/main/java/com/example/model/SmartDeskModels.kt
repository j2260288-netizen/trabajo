package com.example.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

enum class NavDestination(val label: String, val iconName: String) {
    HOME("Superficie", "home"),
    BROWSER("Navegador", "language"),
    FILES("Archivos", "folder"),
    DRAWING("Lienzo Táctil", "brush"),
    SETTINGS("Configuración", "settings")
}

data class MusicTrack(
    val id: String,
    val title: String,
    val artist: String,
    val durationSeconds: Int,
    val albumDrawableRes: Int,
    val genre: String = "Synthwave / Cyber Ambient"
)

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val category: NotificationCategory,
    val isRead: Boolean = false
)

enum class NotificationCategory(val label: String) {
    DESK_SENSOR("Sensores Mesa"),
    QI_CHARGING("Carga Inalámbrica"),
    ERGONOMICS("Ergonomía"),
    SYSTEM("Sistema IPS")
}

data class DeskFile(
    val id: String,
    val name: String,
    val extension: String,
    val size: String,
    val date: String,
    val type: FileType,
    val description: String = ""
)

enum class FileType {
    DRAWING,
    CAD_BLUEPRINT,
    DOCUMENT,
    SYSTEM_LOG
}

data class SensorData(
    val surfaceTemp: Float = 21.4f,
    val ambientLux: Int = 360,
    val qiChargerActive: Boolean = true,
    val qiPowerWatts: Float = 15.0f,
    val deskHeightCm: Int = 74,
    val isStandingMode: Boolean = false,
    val targetHeightCm: Int = 74
)

data class DeskSettings(
    val ipsBrightness: Float = 0.85f,
    val trueToneAuto: Boolean = true,
    val colorTempK: Int = 5600,
    val wirelessChargingEnabled: Boolean = true,
    val hapticFeedbackEnabled: Boolean = true,
    val squareAspectLocked: Boolean = false // Toggle between 1:1 20x20cm panel mode and full screen
)

data class DrawStroke(
    val points: List<Offset>,
    val color: Color,
    val strokeWidth: Float
)
