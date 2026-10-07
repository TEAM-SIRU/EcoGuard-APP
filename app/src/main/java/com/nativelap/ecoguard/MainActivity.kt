package com.nativelap.ecoguard

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.nativelap.ecoguard.feature.home.view.HomeRoute
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.ThemeMode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lockPhoneToPortrait()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                scrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT,
            ),
            navigationBarStyle = SystemBarStyle.light(
                scrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT,
            ),
        )
        setContent {
            EcoGuardTheme(themeMode = ThemeMode.LIGHT) {
                HomeRoute()
            }
        }
    }

    // 휴대폰(최소 폭 600dp 미만)은 세로로 고정하고, 태블릿·폴더블은 회전을 허용한다.
    private fun lockPhoneToPortrait() {
        if (resources.configuration.smallestScreenWidthDp < LARGE_SCREEN_MIN_WIDTH_DP) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    private companion object {
        const val LARGE_SCREEN_MIN_WIDTH_DP = 600
    }
}
