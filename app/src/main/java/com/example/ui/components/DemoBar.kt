package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Icon
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
import com.example.ui.theme.ElectricEmerald
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.ObsidianDark
import com.example.ui.theme.Silver100
import com.example.ui.theme.Silver300
import com.example.ui.theme.Silver400

@Composable
fun DemoControlBar(
    isSquareMode: Boolean,
    onTriggerThreeFingerGesture: () -> Unit,
    onTriggerNotification: () -> Unit,
    onToggleSquareMode: () -> Unit,
    onNextTrack: () -> Unit,
    onToggleDeskHeight: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("demo_control_bar"),
        color = Color(0xF20B0E16),
        shape = RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder),
        tonalElevation = 10.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Label
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(NeonCyan)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "DEMO TÁCTIL",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonCyan,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp
                )
            }

            // Interactive test triggers
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                DemoChipButton(
                    label = "🖐️ Gesto 3 Dedos",
                    icon = Icons.Default.TouchApp,
                    accentColor = NeonCyan,
                    onClick = onTriggerThreeFingerGesture,
                    testTag = "demo_btn_three_finger"
                )

                DemoChipButton(
                    label = "🔔 +Notif",
                    icon = Icons.Default.NotificationsActive,
                    accentColor = ElectricEmerald,
                    onClick = onTriggerNotification,
                    testTag = "demo_btn_notif"
                )

                DemoChipButton(
                    label = if (isSquareMode) "⏹️ 20×20 cm" else "↔️ Fluido",
                    icon = Icons.Default.AspectRatio,
                    accentColor = Silver300,
                    onClick = onToggleSquareMode,
                    testTag = "demo_btn_square_toggle"
                )

                DemoChipButton(
                    label = "⬆️ Altura",
                    icon = Icons.Default.Height,
                    accentColor = Silver300,
                    onClick = onToggleDeskHeight,
                    testTag = "demo_btn_height"
                )
            }
        }
    }
}

@Composable
private fun DemoChipButton(
    label: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF161C28))
            .border(1.dp, accentColor.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 7.dp, vertical = 5.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = accentColor,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = Silver100
            )
        }
    }
}
