package com.nativelap.ecoguard.ui.component

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * 어두운 배경이 시스템 바 뒤까지 그려지는 화면(카메라 촬영)에서 상태 바·내비게이션 바 아이콘을 밝게 바꾼다.
 * 화면을 벗어나면 앱 기본값(MainActivity의 SystemBarStyle.light: 어두운 아이콘)으로 되돌린다.
 * 진입 전 값을 저장해 되돌리면 전환 중 두 화면이 겹칠 때 복원 순서가 꼬일 수 있어 기본값으로 고정한다.
 */
@Composable
fun LightSystemBarIconsEffect() {
    val hostView = LocalView.current

    DisposableEffect(hostView) {
        val hostWindow = hostView.context.findActivity()?.window
        if (hostView.isInEditMode || hostWindow == null) {
            return@DisposableEffect onDispose {}
        }

        val insetsController = WindowCompat.getInsetsController(hostWindow, hostView)
        insetsController.isAppearanceLightStatusBars = false
        insetsController.isAppearanceLightNavigationBars = false

        onDispose {
            insetsController.isAppearanceLightStatusBars = true
            insetsController.isAppearanceLightNavigationBars = true
        }
    }
}

private tailrec fun Context.findActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
}
