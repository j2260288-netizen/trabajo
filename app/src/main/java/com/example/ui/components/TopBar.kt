package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
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
import com.example.model.NavDestination
import com.example.model.SensorData
import com.example.ui.theme.ElectricEmerald
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.GlassHighlight
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.ObsidianCard
import com.example.ui.theme.ObsidianDark
import com.example.ui.theme.Silver100
import com.example.ui.theme.Silver300
import com.example.ui.theme.Silver400

@Composable
fun SmartDeskTopBar(
    currentDestination: NavDestination,
    onNavigate: (NavDestination) -> Unit,
    currentTimeString: String,
    sensorData: SensorData,
    notificationCount: Int,
    onToggleNotifications: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("smart_desk_top_bar"),
        color = Color(0xEB0C0F17),
        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
        tonalElevation = 6.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Logo & Direct Navigation Shortcuts
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // SmartDesk Brand badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Brush.horizontalGradient(listOf(Color(0xFF1E2638), Color(0xFF151922))))
                            .border(1.dp, GlassBorder, RoundedCornerShape(8.dp))
                            .clickable { onNavigate(NavDestination.HOME) }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("nav_btn_home_brand"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(NeonCyan)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "SMARTDESK",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Silver100,
                                letterSpacing = 1.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "20×20 IPS",
                                fontSize = 9.sp,
                                color = NeonCyan,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Persistent Navigation Shortcut Pills
                    TopBarNavButton(
                        destination = NavDestination.HOME,
                        icon = Icons.Default.Home,
                        isSelected = currentDestination == NavDestination.HOME,
                        onClick = { onNavigate(NavDestination.HOME) },
                        testTag = "nav_btn_home"
                    )

                    TopBarNavButton(
                        destination = NavDestination.BROWSER,
                        icon = Icons.Default.Language,
                        isSelected = currentDestination == NavDestination.BROWSER,
                        onClick = { onNavigate(NavDestination.BROWSER) },
                        testTag = "nav_btn_browser"
                    )

                    TopBarNavButton(
                        destination = NavDestination.FILES,
                        icon = Icons.Default.Folder,
                        isSelected = currentDestination == NavDestination.FILES,
                        onClick = { onNavigate(NavDestination.FILES) },
                        testTag = "nav_btn_files"
                    )

                    TopBarNavButton(
                        destination = NavDestination.DRAWING,
                        icon = Icons.Default.Brush,
                        isSelected = currentDestination == NavDestination.DRAWING,
                        onClick = { onNavigate(NavDestination.DRAWING) },
                        testTag = "nav_btn_drawing"
                    )

                    TopBarNavButton(
                        destination = NavDestination.SETTINGS,
                        icon = Icons.Default.Settings,
                        isSelected = currentDestination == NavDestination.SETTINGS,
                        onClick = { onNavigate(NavDestination.SETTINGS) },
                        testTag = "nav_btn_settings"
                    )
                }

                // Right: Environmental sensors, Clock & Notification Center
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Qi Charger Telemetry Pill
                    if (sensorData.qiChargerActive) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x3310B981))
                                .border(1.dp, Color(0x6610B981), RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = "Carga Qi activa",
                                tint = ElectricEmerald,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Qi ${sensorData.qiPowerWatts.toInt()}W",
                                color = ElectricEmerald,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Surface Temp Pill
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x221E293B))
                            .border(1.dp, GlassBorder, RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Thermostat,
                            contentDescription = "Temperatura de mesa",
                            tint = Silver300,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${sensorData.surfaceTemp}°C",
                            color = Silver300,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Digital Desk Clock
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = currentTimeString.ifEmpty { "12:00:00" },
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Silver100,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "MESA ACTIVA",
                            fontSize = 8.sp,
                            color = NeonCyan,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp
                        )
                    }

                    // Notification Button with Badge
                    IconButton(
                        onClick = onToggleNotifications,
                        modifier = Modifier
                            .size(38.dp)
                            .testTag("btn_toggle_notifications")
                    ) {
                        BadgedBox(
                            badge = {
                                if (notificationCount > 0) {
                                    Badge(
                                        containerColor = NeonCyan,
                                        contentColor = ObsidianDark
                                    ) {
                                        Text(
                                            text = notificationCount.toString(),
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Centro de notificaciones",
                                tint = if (notificationCount > 0) NeonCyan else Silver400,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // Subtle glowing bottom divider line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color.Transparent,
                                NeonCyan.copy(alpha = 0.4f),
                                Silver400.copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        )
                    )
            )
        }
    }
}

@Composable
private fun TopBarNavButton(
    destination: NavDestination,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val bg = if (isSelected) {
        Brush.horizontalGradient(listOf(Color(0xFF00E5FF).copy(alpha = 0.18f), Color(0xFF1E293B)))
    } else {
        Brush.horizontalGradient(listOf(Color.Transparent, Color.Transparent))
    }
    val borderCol = if (isSelected) NeonCyan.copy(alpha = 0.7f) else Color.Transparent

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bg)
            .border(1.dp, borderCol, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 9.dp, vertical = 6.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = destination.label,
                tint = if (isSelected) NeonCyan else Silver400,
                modifier = Modifier.size(17.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = destination.label,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Silver100 else Silver400
            )
        }
    }
}
