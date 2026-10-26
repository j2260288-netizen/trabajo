package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MusicTrack
import com.example.model.NavDestination
import com.example.model.SensorData
import com.example.ui.theme.ElectricEmerald
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.ObsidianDark
import com.example.ui.theme.Silver100
import com.example.ui.theme.Silver300
import com.example.ui.theme.Silver400
import com.example.ui.theme.Silver500
import com.example.ui.theme.SolarAmber

@Composable
fun DeskSurfaceHome(
    currentTrack: MusicTrack,
    allTracks: List<MusicTrack>,
    currentTrackIndex: Int,
    isPlaying: Boolean,
    trackPositionSeconds: Int,
    isMediaExpanded: Boolean,
    volume: Float,
    sensorData: SensorData,
    quickNoteText: String,
    onTogglePlay: () -> Unit,
    onNextTrack: () -> Unit,
    onPreviousTrack: () -> Unit,
    onToggleMediaExpand: () -> Unit,
    onSelectTrack: (Int) -> Unit,
    onVolumeChange: (Float) -> Unit,
    onSeekMedia: (Int) -> Unit,
    onNavigate: (NavDestination) -> Unit,
    onOpenNoteKeyboard: () -> Unit,
    onClearNote: () -> Unit,
    onTriggerThreeFingerGesture: () -> Unit,
    onSetDeskHeight: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianDark)
            .padding(14.dp)
            .verticalScroll(rememberScrollState())
            .testTag("desk_surface_home"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Expandable Multimedia Widget
        ExpandableMediaWidget(
            currentTrack = currentTrack,
            allTracks = allTracks,
            currentTrackIndex = currentTrackIndex,
            isPlaying = isPlaying,
            currentPositionSeconds = trackPositionSeconds,
            isExpanded = isMediaExpanded,
            volume = volume,
            onTogglePlay = onTogglePlay,
            onNext = onNextTrack,
            onPrevious = onPreviousTrack,
            onToggleExpand = onToggleMediaExpand,
            onSelectTrack = onSelectTrack,
            onVolumeChange = onVolumeChange,
            onSeek = onSeekMedia,
            modifier = Modifier.fillMaxWidth()
        )

        // Middle Row: Quick Note Scratchpad (Linked to Virtual Keyboard) & Desk Telemetry
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Scratchpad Note Card
            Surface(
                modifier = Modifier
                    .weight(1.2f)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
                    .clickable { onOpenNoteKeyboard() }
                    .testTag("quick_note_widget"),
                color = Color(0xF2101522),
                tonalElevation = 6.dp
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "BLOC TÁCTIL DE MESA",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Silver300,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 1.sp
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = onClearNote,
                                modifier = Modifier.size(28.dp).testTag("btn_clear_quick_note")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Limpiar nota",
                                    tint = Silver500,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                            IconButton(
                                onClick = onOpenNoteKeyboard,
                                modifier = Modifier.size(28.dp).testTag("btn_open_keyboard_note")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Keyboard,
                                    contentDescription = "Escribir con teclado",
                                    tint = NeonCyan,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0A0E17))
                            .border(1.dp, Color(0xFF1B2332), RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = quickNoteText.ifEmpty { "Toca aquí o usa el gesto de 3 dedos para escribir..." },
                            fontSize = 12.sp,
                            color = if (quickNoteText.isEmpty()) Silver500 else Silver100,
                            lineHeight = 17.sp,
                            minLines = 3,
                            maxLines = 4
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "⌨️ Pulsa para abrir teclado virtual QWERTY",
                        fontSize = 10.sp,
                        color = NeonCyan,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Desk Telemetry & Physical Status
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, GlassBorder, RoundedCornerShape(16.dp)),
                color = Color(0xF2101522),
                tonalElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "TELEMETRÍA IPS",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Silver300,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )

                    // Height item with quick buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Height, null, tint = NeonCyan, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text("Altura Mesa", fontSize = 11.sp, color = Silver400)
                                Text("${sensorData.deskHeightCm} cm", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Silver100)
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (sensorData.deskHeightCm <= 75) NeonCyan.copy(alpha = 0.25f) else Color(0xFF1E283A))
                                    .clickable { onSetDeskHeight(72) }
                                    .padding(horizontal = 6.dp, vertical = 3.dp)
                            ) {
                                Text("72", fontSize = 9.sp, color = Silver100, fontWeight = FontWeight.Bold)
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (sensorData.deskHeightCm >= 100) NeonCyan.copy(alpha = 0.25f) else Color(0xFF1E283A))
                                    .clickable { onSetDeskHeight(115) }
                                    .padding(horizontal = 6.dp, vertical = 3.dp)
                            ) {
                                Text("115", fontSize = 9.sp, color = Silver100, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Surface Temp
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Thermostat, null, tint = SolarAmber, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text("Temp. Superficie", fontSize = 11.sp, color = Silver400)
                            Text("${sensorData.surfaceTemp}°C (Óptimo)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Silver100)
                        }
                    }

                    // Ambient Lux
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LightMode, null, tint = Silver100, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text("Luz Ambiente", fontSize = 11.sp, color = Silver400)
                            Text("${sensorData.ambientLux} lx • TrueTone", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Silver100)
                        }
                    }
                }
            }
        }

        // Quick Launch Grid to Primary Functions
        Text(
            text = "ACCESOS RÁPIDOS DE LA SUPERFICIE",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Silver400,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 1.sp
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickLaunchTile(
                title = "Navegador",
                subtitle = "Web & CAD",
                icon = Icons.Default.Language,
                accentColor = NeonCyan,
                onClick = { onNavigate(NavDestination.BROWSER) },
                modifier = Modifier.weight(1f).testTag("quick_tile_browser")
            )

            QuickLaunchTile(
                title = "Archivos",
                subtitle = "Almacenamiento",
                icon = Icons.Default.Folder,
                accentColor = SolarAmber,
                onClick = { onNavigate(NavDestination.FILES) },
                modifier = Modifier.weight(1f).testTag("quick_tile_files")
            )

            QuickLaunchTile(
                title = "Lienzo",
                subtitle = "Dibujo Táctil",
                icon = Icons.Default.Brush,
                accentColor = ElectricEmerald,
                onClick = { onNavigate(NavDestination.DRAWING) },
                modifier = Modifier.weight(1f).testTag("quick_tile_drawing")
            )

            QuickLaunchTile(
                title = "Ajustes",
                subtitle = "Configuración",
                icon = Icons.Default.Settings,
                accentColor = Silver300,
                onClick = { onNavigate(NavDestination.SETTINGS) },
                modifier = Modifier.weight(1f).testTag("quick_tile_settings")
            )
        }

        // 3-Finger Gesture Simulator Floating Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF141F32), Color(0xFF111722))
                    )
                )
                .border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                .clickable(onClick = onTriggerThreeFingerGesture)
                .padding(14.dp)
                .testTag("banner_three_finger_gesture")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(NeonCyan.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.TouchApp,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Simulador de Gesto: 3 Dedos Hacia Arriba",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Silver100
                        )
                        Text(
                            text = "Toca aquí para simular el gesto y desplegar el teclado táctil QWERTY.",
                            fontSize = 11.sp,
                            color = Silver400
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(NeonCyan)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "PROBAR GESTO",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = ObsidianDark,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickLaunchTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, GlassBorder, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        color = Color(0xF2121724),
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Silver100
            )
            Text(
                text = subtitle,
                fontSize = 9.5.sp,
                color = Silver500
            )
        }
    }
}
