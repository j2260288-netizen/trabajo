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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.example.model.DeskFile
import com.example.model.FileType
import com.example.ui.theme.CyberPurple
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
fun FilesScreen(
    files: List<DeskFile>,
    selectedFile: DeskFile?,
    onSelectFile: (DeskFile) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianDark)
            .padding(12.dp)
            .testTag("files_screen_container"),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Left Column: File Explorer List
        Surface(
            modifier = Modifier
                .weight(1.2f)
                .fillMaxSize()
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, GlassBorder, RoundedCornerShape(14.dp)),
            color = Color(0xF210141F),
            tonalElevation = 6.dp
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Folder,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ALMACENAMIENTO MESA",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Silver100,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.sp
                        )
                    }
                    Text(
                        text = "${files.size} elementos",
                        fontSize = 10.sp,
                        color = Silver400
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(files, key = { it.id }) { file ->
                        val isSelected = file.id == selectedFile?.id
                        val (icon, color) = when (file.type) {
                            FileType.CAD_BLUEPRINT -> Pair(Icons.Default.InsertDriveFile, NeonCyan)
                            FileType.DRAWING -> Pair(Icons.Default.Draw, CyberPurple)
                            FileType.DOCUMENT -> Pair(Icons.Default.Description, SolarAmber)
                            FileType.SYSTEM_LOG -> Pair(Icons.Default.Terminal, ElectricEmerald)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) Color(0x3300E5FF) else Color(0xFF161B28))
                                .border(
                                    1.dp,
                                    if (isSelected) NeonCyan else Color.Transparent,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { onSelectFile(file) }
                                .padding(10.dp)
                                .testTag("file_item_${file.id}"),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(color.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = color,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = file.name,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Silver100 else Silver300,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "${file.size} • ${file.date}",
                                        fontSize = 10.sp,
                                        color = Silver500
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFF222B3C))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = file.extension,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = color
                                )
                            }
                        }
                    }
                }
            }
        }

        // Right Column: Preview & Metadata Inspector
        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, GlassBorder, RoundedCornerShape(14.dp)),
            color = Color(0xF210141F),
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp)
            ) {
                Text(
                    text = "INSPECTOR DE ARCHIVO",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Silver400,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (selectedFile != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF161C2A)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = selectedFile.name,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Silver100
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Tipo: ${selectedFile.type.name} • ${selectedFile.size}",
                                fontSize = 11.sp,
                                color = NeonCyan,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = selectedFile.description,
                                fontSize = 11.5.sp,
                                color = Silver300,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Technical Schematics Preview Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF080B12))
                            .border(1.dp, Color(0xFF1B2332), RoundedCornerShape(10.dp))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.InsertDriveFile,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Vista previa vectorial 20×20 cm",
                                fontSize = 11.sp,
                                color = Silver400,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "Integridad de renderizado: 100% IPS",
                                fontSize = 9.sp,
                                color = ElectricEmerald
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Selecciona un archivo para inspeccionar",
                            fontSize = 12.sp,
                            color = Silver500
                        )
                    }
                }
            }
        }
    }
}
