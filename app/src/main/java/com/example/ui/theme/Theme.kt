package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val SmartDeskColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = ObsidianDark,
    primaryContainer = ObsidianSurfaceVariant,
    onPrimaryContainer = NeonCyan,
    secondary = Silver300,
    onSecondary = ObsidianDark,
    secondaryContainer = ObsidianCard,
    onSecondaryContainer = Silver100,
    tertiary = CyberPurple,
    onTertiary = ObsidianDark,
    background = ObsidianDark,
    onBackground = Silver100,
    surface = ObsidianSurface,
    onSurface = Silver100,
    surfaceVariant = ObsidianSurfaceVariant,
    onSurfaceVariant = Silver400,
    outline = Silver500,
    outlineVariant = GlassBorder
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = SmartDeskColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = ObsidianDark.toArgb()
            window.navigationBarColor = ObsidianDark.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
