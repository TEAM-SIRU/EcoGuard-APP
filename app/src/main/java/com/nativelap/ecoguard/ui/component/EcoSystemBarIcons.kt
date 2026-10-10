package com.nativelap.ecoguard.ui.component

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/** 어두운 촬영 화면의 시스템 아이콘을 밝게 바꾸고 화면을 떠나면 호스트 설정을 복원한다. */
@Composable
fun EcoSystemBarIcons(useDarkIcons: Boolean) {
    val view = LocalView.current
    if (view.isInEditMode) return
    DisposableEffect(view, useDarkIcons) {
        val activity = systemBarHostActivity(view.context)
        val controller = activity?.let { WindowCompat.getInsetsController(it.window, view) }
        val previousStatus = controller?.isAppearanceLightStatusBars
        val previousNavigation = controller?.isAppearanceLightNavigationBars
        controller?.isAppearanceLightStatusBars = useDarkIcons
        controller?.isAppearanceLightNavigationBars = useDarkIcons
        onDispose {
            if (previousStatus != null) controller.isAppearanceLightStatusBars = previousStatus
            if (previousNavigation != null) controller.isAppearanceLightNavigationBars = previousNavigation
        }
    }
}

private tailrec fun systemBarHostActivity(context: Context): Activity? =
    when (context) {
        is Activity -> context
        is ContextWrapper -> if (context.baseContext === context) null else systemBarHostActivity(context.baseContext)
        else -> null
    }
