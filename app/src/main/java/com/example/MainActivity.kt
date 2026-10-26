package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.NavDestination
import com.example.ui.components.BrowserScreen
import com.example.ui.components.DemoControlBar
import com.example.ui.components.DeskSurfaceHome
import com.example.ui.components.DrawingScreen
import com.example.ui.components.FilesScreen
import com.example.ui.components.NotificationCenterOverlay
import com.example.ui.components.SettingsScreen
import com.example.ui.components.SmartDeskTopBar
import com.example.ui.components.VirtualKeyboardOverlay
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.ObsidianDark
import com.example.ui.theme.Silver100
import com.example.ui.theme.Silver400
import com.example.viewmodel.SmartDeskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                SmartDeskAppRoot()
            }
        }
    }
}

@Composable
fun SmartDeskAppRoot(
    viewModel: SmartDeskViewModel = viewModel()
) {
    val currentDestination by viewModel.currentDestination.collectAsState()
    val currentTimeString by viewModel.currentTimeString.collectAsState()
    val sensorData by viewModel.sensorData.collectAsState()
    val deskSettings by viewModel.deskSettings.collectAsState()

    val tracks by viewModel.tracks.collectAsState()
    val currentTrackIndex by viewModel.currentTrackIndex.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val trackPosition by viewModel.trackPosition.collectAsState()
    val isMediaExpanded by viewModel.isMediaExpanded.collectAsState()
    val volume by viewModel.volume.collectAsState()

    val notifications by viewModel.notifications.collectAsState()
    val isNotificationCenterOpen by viewModel.isNotificationCenterOpen.collectAsState()

    val isKeyboardVisible by viewModel.isKeyboardVisible.collectAsState()
    val activeInputTarget by viewModel.activeInputTarget.collectAsState()
    val quickNoteText by viewModel.quickNoteText.collectAsState()
    val browserUrl by viewModel.browserUrl.collectAsState()
    val keyboardGestureHint by viewModel.keyboardGestureHint.collectAsState()

    val strokes by viewModel.strokes.collectAsState()
    val currentStrokePoints by viewModel.currentStrokePoints.collectAsState()
    val selectedDrawColor by viewModel.selectedColor.collectAsState()
    val drawStrokeWidth by viewModel.strokeWidth.collectAsState()

    val files by viewModel.files.collectAsState()
    val selectedFile by viewModel.selectedFile.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    // Handle back button
    BackHandler(enabled = isKeyboardVisible || isNotificationCenterOpen || currentDestination != NavDestination.HOME) {
        if (isKeyboardVisible) {
            viewModel.hideKeyboard()
        } else if (isNotificationCenterOpen) {
            viewModel.closeNotificationCenter()
        } else {
            viewModel.navigateBack()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianDark)
            .testTag("smart_desk_scaffold"),
        containerColor = ObsidianDark,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { _ ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(ObsidianDark)
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            val isSquareLocked = deskSettings.squareAspectLocked

            // Main desk surface container
            Box(
                modifier = if (isSquareLocked) {
                    Modifier
                        .size(minOf(maxWidth, maxHeight))
                        .aspectRatio(1f)
                        .align(Alignment.Center)
                        .clip(RoundedCornerShape(16.dp))
                        .border(2.dp, Brush.linearGradient(listOf(Color(0xFF334155), Color(0xFF1E293B))), RoundedCornerShape(16.dp))
                } else {
                    Modifier.fillMaxSize()
                }
                // Multi-touch 3-finger upward swipe detector
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent(PointerEventPass.Initial)
                            val pointers = event.changes
                            if (pointers.size >= 3) {
                                val upwardMovements = pointers.count { change ->
                                    val dy = change.positionChange().y
                                    dy < -8f // Moving upward
                                }
                                if (upwardMovements >= 3) {
                                    pointers.forEach { it.consume() }
                                    viewModel.triggerThreeFingerGesture()
                                }
                            }
                        }
                    }
                }
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Persistent Top Bar
                    SmartDeskTopBar(
                        currentDestination = currentDestination,
                        onNavigate = { viewModel.navigateTo(it) },
                        currentTimeString = currentTimeString,
                        sensorData = sensorData,
                        notificationCount = notifications.size,
                        onToggleNotifications = { viewModel.toggleNotificationCenter() }
                    )

                    // Main Content Screen Area
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        when (currentDestination) {
                            NavDestination.HOME -> {
                                DeskSurfaceHome(
                                    currentTrack = tracks[currentTrackIndex],
                                    allTracks = tracks,
                                    currentTrackIndex = currentTrackIndex,
                                    isPlaying = isPlaying,
                                    trackPositionSeconds = trackPosition,
                                    isMediaExpanded = isMediaExpanded,
                                    volume = volume,
                                    sensorData = sensorData,
                                    quickNoteText = quickNoteText,
                                    onTogglePlay = { viewModel.togglePlayPause() },
                                    onNextTrack = { viewModel.nextTrack() },
                                    onPreviousTrack = { viewModel.previousTrack() },
                                    onToggleMediaExpand = { viewModel.toggleMediaExpanded() },
                                    onSelectTrack = { viewModel.selectTrack(it) },
                                    onVolumeChange = { viewModel.setVolume(it) },
                                    onSeekMedia = { viewModel.seekTo(it) },
                                    onNavigate = { viewModel.navigateTo(it) },
                                    onOpenNoteKeyboard = { viewModel.showKeyboard("QUICK_NOTE") },
                                    onClearNote = { viewModel.clearActiveInput() },
                                    onTriggerThreeFingerGesture = { viewModel.triggerThreeFingerGesture() },
                                    onSetDeskHeight = { viewModel.setDeskHeightTarget(it) }
                                )
                            }
                            NavDestination.BROWSER -> {
                                BrowserScreen(
                                    currentUrl = browserUrl,
                                    onUrlClick = { viewModel.showKeyboard("BROWSER_URL") },
                                    onSelectBookmark = { viewModel.updateBrowserUrl(it) }
                                )
                            }
                            NavDestination.FILES -> {
                                FilesScreen(
                                    files = files,
                                    selectedFile = selectedFile,
                                    onSelectFile = { viewModel.selectFile(it) }
                                )
                            }
                            NavDestination.DRAWING -> {
                                DrawingScreen(
                                    strokes = strokes,
                                    currentStrokePoints = currentStrokePoints,
                                    selectedColor = selectedDrawColor,
                                    strokeWidth = drawStrokeWidth,
                                    onStartStroke = { viewModel.startStroke(it) },
                                    onAddPoint = { viewModel.addPoint(it) },
                                    onEndStroke = { viewModel.endStroke() },
                                    onSelectColor = { viewModel.setDrawColor(it) },
                                    onSelectStrokeWidth = { viewModel.setStrokeWidth(it) },
                                    onUndo = { viewModel.undoStroke() },
                                    onClear = { viewModel.clearCanvas() },
                                    onSave = { viewModel.saveDrawingToFiles() }
                                )
                            }
                            NavDestination.SETTINGS -> {
                                SettingsScreen(
                                    sensorData = sensorData,
                                    deskSettings = deskSettings,
                                    onSetHeight = { viewModel.setDeskHeightTarget(it) },
                                    onToggleQi = { viewModel.toggleQiCharging() },
                                    onToggleSquareMode = { viewModel.toggleSquareAspectMode() },
                                    onSetBrightness = { viewModel.setIpsBrightness(it) },
                                    onToggleTrueTone = { viewModel.toggleTrueTone() },
                                    onToggleHaptic = { viewModel.toggleHaptic() }
                                )
                            }
                        }

                        // Floating Notification Center Dropdown
                        NotificationCenterOverlay(
                            isOpen = isNotificationCenterOpen,
                            notifications = notifications,
                            onClose = { viewModel.closeNotificationCenter() },
                            onDismissItem = { viewModel.dismissNotification(it) },
                            onClearAll = { viewModel.clearAllNotifications() },
                            onTriggerDemoNotif = { viewModel.triggerDemoNotification() },
                            modifier = Modifier.align(Alignment.TopCenter)
                        )
                    }

                    // Demo Control Toolbar at Bottom
                    DemoControlBar(
                        isSquareMode = isSquareLocked,
                        onTriggerThreeFingerGesture = { viewModel.triggerThreeFingerGesture() },
                        onTriggerNotification = { viewModel.triggerDemoNotification() },
                        onToggleSquareMode = { viewModel.toggleSquareAspectMode() },
                        onNextTrack = { viewModel.nextTrack() },
                        onToggleDeskHeight = {
                            val nextHeight = if (sensorData.deskHeightCm <= 80) 115 else 72
                            viewModel.setDeskHeightTarget(nextHeight)
                        }
                    )
                }

                // Temporary Banner for 3-finger gesture feedback
                AnimatedVisibility(
                    visible = keyboardGestureHint != null,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 64.dp)
                ) {
                    keyboardGestureHint?.let { hint ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xE60A101C))
                                .border(1.dp, NeonCyan, RoundedCornerShape(20.dp))
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = hint,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = NeonCyan
                            )
                        }
                    }
                }

                // Virtual QWERTY Keyboard Sliding Sheet
                VirtualKeyboardOverlay(
                    isVisible = isKeyboardVisible,
                    activeTargetLabel = when (activeInputTarget) {
                        "BROWSER_URL" -> "URL del Navegador"
                        "SEARCH" -> "Búsqueda"
                        else -> "Bloc de Notas de Mesa"
                    },
                    onKeyPress = { viewModel.onKeyPress(it) },
                    onBackspace = { viewModel.onBackspace() },
                    onEnter = { viewModel.onEnter() },
                    onHide = { viewModel.hideKeyboard() },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}
