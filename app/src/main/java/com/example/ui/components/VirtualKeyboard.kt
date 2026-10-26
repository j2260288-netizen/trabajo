package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardReturn
import androidx.compose.material.icons.filled.SpaceBar
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.ObsidianDark
import com.example.ui.theme.Silver100
import com.example.ui.theme.Silver300
import com.example.ui.theme.Silver400
import com.example.ui.theme.Silver500

@Composable
fun VirtualKeyboardOverlay(
    isVisible: Boolean,
    activeTargetLabel: String,
    onKeyPress: (String) -> Unit,
    onBackspace: () -> Unit,
    onEnter: () -> Unit,
    onHide: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isShifted by remember { mutableStateOf(false) }
    var isNumbersMode by remember { mutableStateOf(false) }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("virtual_keyboard_panel"),
            color = Color(0xF70C0F16),
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder),
            tonalElevation = 16.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Keyboard Handle Bar & Active Input Indicator
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.TouchApp,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "TECLADO TÁCTIL IPS (GESTO 3 DEDOS)",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "• Escribiendo en: $activeTargetLabel",
                            fontSize = 10.sp,
                            color = Silver400
                        )
                    }

                    // Hide keyboard pill button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF1E2638))
                            .border(1.dp, GlassBorder, RoundedCornerShape(8.dp))
                            .clickable(onClick = onHide)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                            .testTag("btn_hide_keyboard"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Ocultar",
                                fontSize = 11.sp,
                                color = Silver100,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Ocultar teclado",
                                tint = NeonCyan,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                val row1 = if (isNumbersMode) {
                    listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0")
                } else if (isShifted) {
                    listOf("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P")
                } else {
                    listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p")
                }

                val row2 = if (isNumbersMode) {
                    listOf("@", "#", "$", "%", "&", "-", "+", "(", ")", "/")
                } else if (isShifted) {
                    listOf("A", "S", "D", "F", "G", "H", "J", "K", "L", "Ñ")
                } else {
                    listOf("a", "s", "d", "f", "g", "h", "j", "k", "l", "ñ")
                }

                val row3Letters = if (isNumbersMode) {
                    listOf("*", "\"", "'", ":", ";", "!", "?")
                } else if (isShifted) {
                    listOf("Z", "X", "C", "V", "B", "N", "M")
                } else {
                    listOf("z", "x", "c", "v", "b", "n", "m")
                }

                // Row 1
                KeyboardRow(
                    keys = row1,
                    onKeyPress = onKeyPress,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Row 2
                KeyboardRow(
                    keys = row2,
                    onKeyPress = onKeyPress,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Row 3 with Shift & Backspace
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Shift Key
                    SpecialKey(
                        label = if (isShifted) "⇪" else "⇧",
                        isActive = isShifted,
                        modifier = Modifier.weight(1.3f).testTag("key_shift"),
                        onClick = { isShifted = !isShifted }
                    )

                    row3Letters.forEach { letter ->
                        KeyButton(
                            text = letter,
                            onClick = { onKeyPress(letter) },
                            modifier = Modifier.weight(1f).testTag("key_$letter")
                        )
                    }

                    // Backspace Key
                    SpecialKeyWithIcon(
                        icon = Icons.Default.Backspace,
                        contentDescription = "Borrar",
                        modifier = Modifier.weight(1.3f).testTag("key_backspace"),
                        onClick = onBackspace
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Row 4: Mode switch, Spacebar, Enter, Dot, Comma
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 123 / ABC switch
                    SpecialKey(
                        label = if (isNumbersMode) "ABC" else "?123",
                        isActive = isNumbersMode,
                        modifier = Modifier.weight(1.5f).testTag("key_mode_switch"),
                        onClick = { isNumbersMode = !isNumbersMode }
                    )

                    KeyButton(
                        text = ",",
                        onClick = { onKeyPress(",") },
                        modifier = Modifier.weight(0.9f).testTag("key_comma")
                    )

                    // Spacebar
                    Box(
                        modifier = Modifier
                            .weight(4.5f)
                            .height(46.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF1E2536))
                            .border(1.dp, Color(0xFF2C374D), RoundedCornerShape(8.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(color = NeonCyan),
                                onClick = { onKeyPress(" ") }
                            )
                            .testTag("key_space"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.SpaceBar,
                                contentDescription = "Espacio",
                                tint = Silver400,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "espacio",
                                fontSize = 11.sp,
                                color = Silver400
                            )
                        }
                    }

                    KeyButton(
                        text = ".",
                        onClick = { onKeyPress(".") },
                        modifier = Modifier.weight(0.9f).testTag("key_dot")
                    )

                    // Enter Key
                    Box(
                        modifier = Modifier
                            .weight(1.8f)
                            .height(46.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(NeonCyan.copy(alpha = 0.2f))
                            .border(1.dp, NeonCyan.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(color = NeonCyan),
                                onClick = onEnter
                            )
                            .testTag("key_enter"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Listo",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonCyan
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Icon(
                                imageVector = Icons.Default.KeyboardReturn,
                                contentDescription = "Enter",
                                tint = NeonCyan,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun KeyboardRow(
    keys: List<String>,
    onKeyPress: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        keys.forEach { key ->
            KeyButton(
                text = key,
                onClick = { onKeyPress(key) },
                modifier = Modifier.weight(1f).testTag("key_$key")
            )
        }
    }
}

@Composable
private fun KeyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(46.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF19202E))
            .border(1.dp, Color(0xFF2B364C), RoundedCornerShape(8.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = NeonCyan),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
            color = Silver100
        )
    }
}

@Composable
private fun SpecialKey(
    label: String,
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(46.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isActive) NeonCyan.copy(alpha = 0.25f) else Color(0xFF141924))
            .border(
                1.dp,
                if (isActive) NeonCyan.copy(alpha = 0.8f) else Color(0xFF253043),
                RoundedCornerShape(8.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = NeonCyan),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = if (isActive) NeonCyan else Silver300
        )
    }
}

@Composable
private fun SpecialKeyWithIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(46.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF141924))
            .border(1.dp, Color(0xFF253043), RoundedCornerShape(8.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = NeonCyan),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Silver300,
            modifier = Modifier.size(18.dp)
        )
    }
}
