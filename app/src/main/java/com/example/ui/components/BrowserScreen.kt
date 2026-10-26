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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.graphics.Color
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
import com.example.ui.theme.Silver500

@Composable
fun BrowserScreen(
    currentUrl: String,
    onUrlClick: () -> Unit,
    onSelectBookmark: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val bookmarks = listOf(
        Pair("Especificación IPS 20x20", "https://smartdesk.internal/ips-spec"),
        Pair("Ergonomía de Altura", "https://smartdesk.internal/ergonomics"),
        Pair("Protocolo Qi 15W", "https://smartdesk.internal/qi-protocol"),
        Pair("Manual de Gestos Táctiles", "https://smartdesk.internal/gestures")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianDark)
            .padding(12.dp)
            .testTag("browser_screen_container")
    ) {
        // Navigation bar
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, GlassBorder, RoundedCornerShape(12.dp)),
            color = Color(0xF2121722),
            tonalElevation = 6.dp
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    IconButton(
                        onClick = { },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás",
                            tint = Silver400,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    IconButton(
                        onClick = { },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Adelante",
                            tint = Silver400,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    IconButton(
                        onClick = { },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Recargar",
                            tint = Silver400,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Interactive Address Bar (clicking invokes virtual keyboard)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0A0D14))
                            .border(1.dp, NeonCyan.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .clickable(onClick = onUrlClick)
                            .padding(horizontal = 10.dp)
                            .testTag("browser_address_bar"),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Seguro",
                                    tint = ElectricEmerald,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = currentUrl,
                                    fontSize = 12.sp,
                                    color = Silver100,
                                    fontFamily = FontFamily.Monospace,
                                    maxLines = 1
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar URL",
                                tint = NeonCyan,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Bookmarks chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = null,
                        tint = Silver500,
                        modifier = Modifier.size(14.dp)
                    )
                    bookmarks.forEach { (title, url) ->
                        val isCurrent = currentUrl == url
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isCurrent) NeonCyan.copy(alpha = 0.2f) else Color(0xFF1B2332))
                                .border(1.dp, if (isCurrent) NeonCyan else Color.Transparent, RoundedCornerShape(6.dp))
                                .clickable { onSelectBookmark(url) }
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                                .testTag("bookmark_${title.replace(" ", "_")}")
                        ) {
                            Text(
                                text = title,
                                fontSize = 10.sp,
                                color = if (isCurrent) NeonCyan else Silver300
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Rendered Web Page Content
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF0F1420))
                .border(1.dp, GlassBorder, RoundedCornerShape(12.dp))
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .testTag("browser_content_viewport")
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Article Header
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(NeonCyan)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "SMARTDESK INTERNAL SPECIFICATION // v4.2",
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            color = NeonCyan,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Arquitectura de la Mesa Inteligente con Pantalla Táctil IPS LCD",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Silver100
                    )
                }

                // Cards with real specs
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161C2A)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "1. Pantalla Cuadrada 20 × 20 cm",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Silver100
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Matriz IPS LCD de formato simétrico 1:1, diseñada sin marcos para montaje enrasado con cristal templado de borosilicato. Ofrece ángulo de visión de 178° libre de degradación cromática.",
                            fontSize = 12.sp,
                            color = Silver300,
                            lineHeight = 18.sp
                        )
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161C2A)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "2. Gestor Gestual Multitáctil de 3 Dedos",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Silver100
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Un barrido vertical hacia arriba ejecutado simultáneamente con tres puntos de contacto despliega el teclado digital inteligente QWERTY para evitar la necesidad de periféricos físicos en el escritorio.",
                            fontSize = 12.sp,
                            color = Silver300,
                            lineHeight = 18.sp
                        )
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161C2A)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "3. Substrato de Carga Qi y Telemetría Térmica",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Silver100
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "La zona izquierda incorpora doble bobina resonante capaz de suministrar hasta 15W manteniendo la disipación pasiva de la mesa por debajo de los 23°C en régimen continuo.",
                            fontSize = 12.sp,
                            color = Silver300,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}
