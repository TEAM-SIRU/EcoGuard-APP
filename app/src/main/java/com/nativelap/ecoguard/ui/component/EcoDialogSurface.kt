package com.nativelap.ecoguard.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowCompat
import com.nativelap.ecoguard.ui.theme.AppRadius
import com.nativelap.ecoguard.ui.theme.AppSpacing

private val dialogMaxWidth = 400.dp

/** 화면 좌우 24dp 여백을 둔 흰색 둥근 다이얼로그 컨테이너. */
@Composable
fun EcoDialogSurface(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false),
    ) {
        val dialogWindow = (LocalView.current.parent as? DialogWindowProvider)?.window
        DisposableEffect(dialogWindow) {
            val previousDimAmount = dialogWindow?.attributes?.dimAmount
            val insetsController = dialogWindow?.let { WindowCompat.getInsetsController(it, it.decorView) }
            val previousStatusIcons = insetsController?.isAppearanceLightStatusBars
            val previousNavigationIcons = insetsController?.isAppearanceLightNavigationBars
            dialogWindow?.setDimAmount(0f)
            insetsController?.isAppearanceLightStatusBars = true
            insetsController?.isAppearanceLightNavigationBars = true
            onDispose {
                if (previousDimAmount != null) dialogWindow.setDimAmount(previousDimAmount)
                if (previousStatusIcons != null) insetsController.isAppearanceLightStatusBars = previousStatusIcons
                if (previousNavigationIcons !=
                    null
                ) {
                    insetsController.isAppearanceLightNavigationBars = previousNavigationIcons
                }
            }
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
                        .pointerInput(onDismissRequest) { detectTapGestures { onDismissRequest() } },
            )
            Box(
                modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing),
                contentAlignment = Alignment.Center,
            ) {
                Surface(
                    modifier =
                        modifier
                            .padding(horizontal = AppSpacing.screenHorizontal)
                            .widthIn(max = dialogMaxWidth)
                            .fillMaxWidth()
                            .pointerInput(Unit) { detectTapGestures { } },
                    shape = RoundedCornerShape(AppRadius.logo),
                    color = MaterialTheme.colorScheme.surface,
                ) {
                    Column(
                        modifier =
                            Modifier
                                .verticalScroll(rememberScrollState())
                                .padding(AppSpacing.xl),
                        verticalArrangement = Arrangement.spacedBy(AppSpacing.xl),
                        content = content,
                    )
                }
            }
        }
    }
}
