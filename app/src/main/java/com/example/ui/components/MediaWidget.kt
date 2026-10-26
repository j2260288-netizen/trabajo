package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MusicTrack
import com.example.ui.theme.ElectricEmerald
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.ObsidianCard
import com.example.ui.theme.ObsidianDark
import com.example.ui.theme.Silver100
import com.example.ui.theme.Silver200
import com.example.ui.theme.Silver300
import com.example.ui.theme.Silver400
import com.example.ui.theme.Silver500

@Composable
fun ExpandableMediaWidget(
    currentTrack: MusicTrack,
    allTracks: List<MusicTrack>,
    currentTrackIndex: Int,
    isPlaying: Boolean,
    currentPositionSeconds: Int,
    isExpanded: Boolean,
    volume: Float,
    onTogglePlay: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onToggleExpand: () -> Unit,
    onSelectTrack: (Int) -> Unit,
    onVolumeChange: (Float) -> Unit,
    onSeek: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .animateContentSize()
            .testTag("expandable_media_widget"),
        color = Color(0xD910141E),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder),
        tonalElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .padding(14.dp)
        ) {
            // Header Row: Cover, Info, Mini Visualizer, Expand Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Album Cover Thumbnail with Glow
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .clickable { onToggleExpand() }
                    ) {
                        Image(
                            painter = painterResource(id = currentTrack.albumDrawableRes),
                            contentDescription = currentTrack.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(56.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "AUDIO IPS MASTER",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonCyan,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            if (isPlaying) {
                                MiniVisualizerPill()
                            }
                        }
                        Text(
                            text = currentTrack.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Silver100,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = currentTrack.artist,
                            fontSize = 12.sp,
                            color = Silver400,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Mini Quick Controls in Compact View
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    IconButton(
                        onClick = onPrevious,
                        modifier = Modifier
                            .size(44.dp)
                            .testTag("media_btn_prev")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipPrevious,
                            contentDescription = "Pista anterior",
                            tint = Silver300,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(NeonCyan, Color(0xFF0097A7))
                                )
                            )
                            .clickable(onClick = onTogglePlay)
                            .testTag("media_btn_play_pause"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pausar" else "Reproducir",
                            tint = ObsidianDark,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    IconButton(
                        onClick = onNext,
                        modifier = Modifier
                            .size(44.dp)
                            .testTag("media_btn_next")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = "Pista siguiente",
                            tint = Silver300,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    IconButton(
                        onClick = onToggleExpand,
                        modifier = Modifier
                            .size(40.dp)
                            .testTag("media_btn_expand")
                    ) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (isExpanded) "Colapsar widget" else "Expandir widget",
                            tint = NeonCyan,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            // Compact Progress Bar
            Spacer(modifier = Modifier.height(10.dp))
            val progress = (currentPositionSeconds.toFloat() / currentTrack.durationSeconds.toFloat()).coerceIn(0f, 1f)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFF222938))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .height(3.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(
                            Brush.horizontalGradient(listOf(NeonCyan, ElectricEmerald))
                        )
                )
            }

            // Expandable Detailed View
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp)
                ) {
                    // Audio Waveform Equalizer Display
                    FullVisualizerWave(isPlaying = isPlaying)

                    Spacer(modifier = Modifier.height(10.dp))

                    // Scrubber with Timestamps
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = formatSeconds(currentPositionSeconds),
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = NeonCyan
                        )
                        Text(
                            text = formatSeconds(currentTrack.durationSeconds),
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Silver400
                        )
                    }

                    Slider(
                        value = currentPositionSeconds.toFloat(),
                        onValueChange = { onSeek(it.toInt()) },
                        valueRange = 0f..currentTrack.durationSeconds.toFloat(),
                        colors = SliderDefaults.colors(
                            thumbColor = NeonCyan,
                            activeTrackColor = NeonCyan,
                            inactiveTrackColor = Color(0xFF273142)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("media_slider_seek")
                    )

                    // Volume Controls Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeDown,
                            contentDescription = "Volumen bajo",
                            tint = Silver400,
                            modifier = Modifier.size(18.dp)
                        )
                        Slider(
                            value = volume,
                            onValueChange = onVolumeChange,
                            valueRange = 0f..1f,
                            colors = SliderDefaults.colors(
                                thumbColor = Silver200,
                                activeTrackColor = Silver300,
                                inactiveTrackColor = Color(0xFF273142)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp)
                                .testTag("media_slider_volume")
                        )
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Volumen alto",
                            tint = Silver300,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Playlist Quick Selector
                    Text(
                        text = "COLA DE REPRODUCCIÓN EN MESA",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Silver400,
                        letterSpacing = 1.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        allTracks.forEachIndexed { index, track ->
                            val isSelected = index == currentTrackIndex
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isSelected) Color(0x3300E5FF) else Color(0x221E293B)
                                    )
                                    .border(
                                        1.dp,
                                        if (isSelected) NeonCyan.copy(alpha = 0.5f) else Color.Transparent,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { onSelectTrack(index) }
                                    .padding(horizontal = 10.dp, vertical = 7.dp)
                                    .testTag("playlist_item_$index"),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "0${index + 1}",
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace,
                                        color = if (isSelected) NeonCyan else Silver500,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = track.title,
                                            fontSize = 12.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) Silver100 else Silver300
                                        )
                                        Text(
                                            text = track.artist,
                                            fontSize = 10.sp,
                                            color = Silver400
                                        )
                                    }
                                }

                                Text(
                                    text = formatSeconds(track.durationSeconds),
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = if (isSelected) NeonCyan else Silver500
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MiniVisualizerPill() {
    val infiniteTransition = rememberInfiniteTransition(label = "mini_eq")
    val h1 by infiniteTransition.animateFloat(
        initialValue = 4f, targetValue = 14f,
        animationSpec = infiniteRepeatable(tween(350, easing = LinearEasing), RepeatMode.Reverse),
        label = "h1"
    )
    val h2 by infiniteTransition.animateFloat(
        initialValue = 12f, targetValue = 5f,
        animationSpec = infiniteRepeatable(tween(420, easing = LinearEasing), RepeatMode.Reverse),
        label = "h2"
    )
    val h3 by infiniteTransition.animateFloat(
        initialValue = 6f, targetValue = 16f,
        animationSpec = infiniteRepeatable(tween(300, easing = LinearEasing), RepeatMode.Reverse),
        label = "h3"
    )

    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier.height(16.dp)
    ) {
        Box(modifier = Modifier.width(2.5.dp).height(h1.dp).background(NeonCyan, CircleShape))
        Box(modifier = Modifier.width(2.5.dp).height(h2.dp).background(ElectricEmerald, CircleShape))
        Box(modifier = Modifier.width(2.5.dp).height(h3.dp).background(NeonCyan, CircleShape))
    }
}

@Composable
fun FullVisualizerWave(isPlaying: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "full_eq")
    val heights = (0 until 18).map { i ->
        val duration = 280 + (i * 35) % 200
        val target = if (isPlaying) 8f + ((i * 7) % 24) else 4f
        infiniteTransition.animateFloat(
            initialValue = 4f,
            targetValue = target,
            animationSpec = infiniteRepeatable(tween(duration, easing = LinearEasing), RepeatMode.Reverse),
            label = "wave_$i"
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(38.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF090D15))
            .border(1.dp, Color(0xFF1E283A), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            heights.forEach { hState ->
                val barHeight = hState.value
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(barHeight.dp)
                        .background(
                            Brush.verticalGradient(listOf(NeonCyan, Color(0xFF0284C7))),
                            RoundedCornerShape(2.dp)
                        )
                )
            }
        }
    }
}

private fun formatSeconds(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return String.format("%02d:%02d", m, s)
}
