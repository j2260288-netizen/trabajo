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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DeskSettings
import com.example.model.SensorData
import com.example.ui.theme.ElectricEmerald
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.ObsidianDark
import com.example.ui.theme.Silver100
import com.example.ui.theme.Silver300
import com.example.ui.theme.Silver400
import com.example.ui.theme.Silver500

@Composable
fun SettingsScreen(
    sensorData: SensorData,
    deskSettings: DeskSettings,
    onSetHeight: (Int) -> Unit,
    onToggleQi: () -> Unit,
    onToggleSquareMode: () -> Unit,
    onSetBrightness: (Float) -> Unit,
    onToggleTrueTone: () -> Unit,
    onToggleHaptic: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianDark)
            .padding(14.dp)
            .verticalScroll(rememberScrollState())
            .testTag("settings_screen_container"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Section 1: Motorized Desk Height (Ergonomics)
        SettingsCard(
            title = "ELEVACIÓN ERGONÓMICA DE LA MESA",
            icon = Icons.Default.Height,
            iconTint = NeonCyan
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Altura actual: ${sensorData.deskHeightCm} cm",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Silver100
                    )
                    Text(
                        text = if (sensorData.isStandingMode) "MODO DE PIE" else "MODO SENTADO",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = if (sensorData.isStandingMode) ElectricEmerald else NeonCyan
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Presets
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listOf(
                        Triple("Sentado", 72, "72 cm"),
                        Triple("Mixto", 90, "90 cm"),
                        Triple("De Pie", 115, "115 cm")
                    ).forEach { (label, cm, desc) ->
                        val isSelected = sensorData.targetHeightCm == cm
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) NeonCyan.copy(alpha = 0.2f) else Color(0xFF1B2332))
                                .border(1.dp, if (isSelected) NeonCyan else Color(0xFF28344A), RoundedCornerShape(8.dp))
                                .clickable { onSetHeight(cm) }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = label,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) NeonCyan else Silver100
                                )
                                Text(
                                    text = desc,
                                    fontSize = 10.sp,
                                    color = Silver400,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section 2: IPS LCD Display Calibration
        SettingsCard(
            title = "CALIBRACIÓN PANTALLA IPS LCD",
            icon = Icons.Default.BrightnessMedium,
            iconTint = Silver100
        ) {
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Brightness Slider
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Brillo de Cristal IPS", fontSize = 13.sp, color = Silver100)
                        Text(
                            text = "${(deskSettings.ipsBrightness * 100).toInt()}%",
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            color = NeonCyan
                        )
                    }
                    Slider(
                        value = deskSettings.ipsBrightness,
                        onValueChange = onSetBrightness,
                        colors = SliderDefaults.colors(
                            thumbColor = NeonCyan,
                            activeTrackColor = NeonCyan,
                            inactiveTrackColor = Color(0xFF232D3F)
                        ),
                        modifier = Modifier.testTag("slider_ips_brightness")
                    )
                }

                // TrueTone Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Sensor TrueTone Automático", fontSize = 13.sp, color = Silver100)
                        Text(
                            text = "Ajusta la temperatura Kelvin en tiempo real según los ${sensorData.ambientLux} lx medidos",
                            fontSize = 11.sp,
                            color = Silver400
                        )
                    }
                    Switch(
                        checked = deskSettings.trueToneAuto,
                        onCheckedChange = { onToggleTrueTone() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = ObsidianDark,
                            checkedTrackColor = NeonCyan,
                            uncheckedTrackColor = Color(0xFF222B3C)
                        ),
                        modifier = Modifier.testTag("switch_truetone")
                    )
                }

                // Square 1:1 Aspect Mode Toggle (20x20 cm physical simulation)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Bloquear Relación Cuadrada 20×20 cm (1:1)", fontSize = 13.sp, color = Silver100)
                        Text(
                            text = "Limita la interfaz a un viewport 1:1 exacto con biseles de titanio cepillado.",
                            fontSize = 11.sp,
                            color = Silver400
                        )
                    }
                    Switch(
                        checked = deskSettings.squareAspectLocked,
                        onCheckedChange = { onToggleSquareMode() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = ObsidianDark,
                            checkedTrackColor = NeonCyan,
                            uncheckedTrackColor = Color(0xFF222B3C)
                        ),
                        modifier = Modifier.testTag("switch_square_aspect")
                    )
                }
            }
        }

        // Section 3: Hardware Integrations (Qi Charger & Haptic)
        SettingsCard(
            title = "HARDWARE DE LA SUPERFICIE INTELIGENTE",
            icon = Icons.Default.Bolt,
            iconTint = ElectricEmerald
        ) {
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Base de Carga Qi Integrada", fontSize = 13.sp, color = Silver100)
                        Text(
                            text = "Bobinas inductivas bajo la superficie (15W Máx / Eficiencia 88%)",
                            fontSize = 11.sp,
                            color = Silver400
                        )
                    }
                    Switch(
                        checked = sensorData.qiChargerActive,
                        onCheckedChange = { onToggleQi() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = ObsidianDark,
                            checkedTrackColor = ElectricEmerald,
                            uncheckedTrackColor = Color(0xFF222B3C)
                        ),
                        modifier = Modifier.testTag("switch_qi_charger")
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Retroalimentación Háptica Táctil", fontSize = 13.sp, color = Silver100)
                        Text(
                            text = "Micro-vibración piezoeléctrica al pulsar teclas o interactuar",
                            fontSize = 11.sp,
                            color = Silver400
                        )
                    }
                    Switch(
                        checked = deskSettings.hapticFeedbackEnabled,
                        onCheckedChange = { onToggleHaptic() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = ObsidianDark,
                            checkedTrackColor = NeonCyan,
                            uncheckedTrackColor = Color(0xFF222B3C)
                        ),
                        modifier = Modifier.testTag("switch_haptic")
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsCard(
    title: String,
    icon: ImageVector,
    iconTint: Color,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, GlassBorder, RoundedCornerShape(12.dp)),
        color = Color(0xF2121724),
        tonalElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Silver300,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}
