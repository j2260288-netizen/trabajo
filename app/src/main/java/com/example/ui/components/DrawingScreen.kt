package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DrawStroke
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.ElectricEmerald
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.ObsidianCard
import com.example.ui.theme.ObsidianDark
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.Silver100
import com.example.ui.theme.Silver300
import com.example.ui.theme.Silver400
import com.example.ui.theme.SolarAmber

@Composable
fun DrawingScreen(
    strokes: List<DrawStroke>,
    currentStrokePoints: List<Offset>,
    selectedColor: Color,
    strokeWidth: Float,
    onStartStroke: (Offset) -> Unit,
    onAddPoint: (Offset) -> Unit,
    onEndStroke: () -> Unit,
    onSelectColor: (Color) -> Unit,
    onSelectStrokeWidth: (Float) -> Unit,
    onUndo: () -> Unit,
    onClear: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    val palette = listOf(
        Pair(NeonCyan, "Cyan Neón"),
        Pair(Silver100, "Platino"),
        Pair(ElectricEmerald, "Verde"),
        Pair(SolarAmber, "Ámbar"),
        Pair(CyberPurple, "Púrpura"),
        Pair(ObsidianDark, "Borrador")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianDark)
            .padding(12.dp)
            .testTag("drawing_screen_container")
    ) {
        // Drawing Top Toolbar
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, GlassBorder, RoundedCornerShape(12.dp)),
            color = Color(0xF2121722),
            tonalElevation = 6.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Color Picker Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "COLOR",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Silver400,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )

                    palette.forEach { (color, label) ->
                        val isSelected = selectedColor == color
                        val isEraser = color == ObsidianDark

                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(if (isEraser) Color(0xFF1E2638) else color)
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) NeonCyan else Color(0x66FFFFFF),
                                    shape = CircleShape
                                )
                                .clickable { onSelectColor(color) }
                                .testTag("color_btn_${label.replace(" ", "_")}"),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isEraser) {
                                Text(
                                    text = "✕",
                                    fontSize = 11.sp,
                                    color = Silver300,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Stroke width selector pills
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "GROSOR",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Silver400,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )

                    listOf(3f to "Fino", 7f to "Med", 14f to "Grueso").forEach { (width, label) ->
                        val isSelected = strokeWidth == width
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) NeonCyan.copy(alpha = 0.2f) else Color(0xFF1E2638))
                            .border(1.dp, if (isSelected) NeonCyan else Color.Transparent, RoundedCornerShape(6.dp))
                            .clickable { onSelectStrokeWidth(width) }
                            .padding(horizontal = 7.dp, vertical = 4.dp)
                            .testTag("stroke_btn_$label")
                        ) {
                            Text(
                                text = label,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) NeonCyan else Silver300
                            )
                        }
                    }
                }

                // Actions: Undo, Clear, Save to Files
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = onUndo,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("drawing_btn_undo")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Undo,
                            contentDescription = "Deshacer trazo",
                            tint = Silver300,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onClear,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("drawing_btn_clear")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Limpiar lienzo",
                            tint = Silver400,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Button(
                        onClick = onSave,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeonCyan,
                            contentColor = ObsidianDark
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .height(36.dp)
                            .testTag("drawing_btn_save")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Guardar",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Interactive Canvas Surface
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF07090D))
                .border(1.dp, GlassBorder, RoundedCornerShape(14.dp))
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            onStartStroke(offset)
                        },
                        onDrag = { change, _ ->
                            change.consume()
                            onAddPoint(change.position)
                        },
                        onDragEnd = {
                            onEndStroke()
                        },
                        onDragCancel = {
                            onEndStroke()
                        }
                    )
                }
                .testTag("drawing_canvas_interactive")
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Background subtle technical grid for smart desk feel
                val step = 32.dp.toPx()
                val gridColor = Color(0x1094A3B8)
                var x = 0f
                while (x < size.width) {
                    drawLine(
                        color = gridColor,
                        start = Offset(x, 0f),
                        end = Offset(x, size.height),
                        strokeWidth = 1f
                    )
                    x += step
                }
                var y = 0f
                while (y < size.height) {
                    drawLine(
                        color = gridColor,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 1f
                    )
                    y += step
                }

                // Render completed strokes
                strokes.forEach { stroke ->
                    if (stroke.points.size > 1) {
                        val path = Path().apply {
                            moveTo(stroke.points[0].x, stroke.points[0].y)
                            for (i in 1 until stroke.points.size) {
                                lineTo(stroke.points[i].x, stroke.points[i].y)
                            }
                        }
                        drawPath(
                            path = path,
                            color = stroke.color,
                            style = Stroke(
                                width = stroke.strokeWidth,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    } else if (stroke.points.size == 1) {
                        drawCircle(
                            color = stroke.color,
                            radius = stroke.strokeWidth / 2f,
                            center = stroke.points[0]
                        )
                    }
                }

                // Render in-progress stroke
                if (currentStrokePoints.size > 1) {
                    val currentPath = Path().apply {
                        moveTo(currentStrokePoints[0].x, currentStrokePoints[0].y)
                        for (i in 1 until currentStrokePoints.size) {
                            lineTo(currentStrokePoints[i].x, currentStrokePoints[i].y)
                        }
                    }
                    drawPath(
                        path = currentPath,
                        color = selectedColor,
                        style = Stroke(
                            width = strokeWidth,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                } else if (currentStrokePoints.size == 1) {
                    drawCircle(
                        color = selectedColor,
                        radius = strokeWidth / 2f,
                        center = currentStrokePoints[0]
                    )
                }
            }

            // Hint in canvas corner
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0x990A0D14))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Lienzo IPS LCD 20×20 cm • Táctil Multicapa",
                    fontSize = 9.sp,
                    color = Silver400,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
