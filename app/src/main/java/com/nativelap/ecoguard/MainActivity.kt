package com.nativelap.ecoguard

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.nativelap.ecoguard.feature.auth.presentation.AuthViewModel
import com.nativelap.ecoguard.navigation.EcoGuardNavHost
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.ThemeMode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lockPhoneToPortrait()
        enableEdgeToEdge(
            statusBarStyle =
                SystemBarStyle.light(
                    scrim = Color.TRANSPARENT,
                    darkScrim = Color.TRANSPARENT,
                ),
            navigationBarStyle =
                SystemBarStyle.light(
                    scrim = Color.TRANSPARENT,
                    darkScrim = Color.TRANSPARENT,
                ),
        )
        setContent {
            val authUiState by authViewModel.uiState.collectAsState()
            LaunchedEffect(authUiState.authorizationUrl) {
                val authorizationUrl = authUiState.authorizationUrl ?: return@LaunchedEffect
                val opened =
                    runCatching {
                        CustomTabsIntent
                            .Builder()
                            .build()
                            .launchUrl(this@MainActivity, android.net.Uri.parse(authorizationUrl))
                    }.isSuccess
                authViewModel.onAuthorizationUrlHandled(opened)
            }
            EcoGuardTheme(themeMode = ThemeMode.LIGHT) {
                EcoGuardNavHost(
                    authUiState = authUiState,
                    onLoginClick = authViewModel::startLogin,
                    onSignOut = authViewModel::signOut,
                )
            }
        }
        handleHandoffIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleHandoffIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        authViewModel.onExternalAuthReturned()
    }

    private fun handleHandoffIntent(intent: Intent?) {
        val uri = intent?.data ?: return
        if (authViewModel.handleHandoffLink(uri)) intent.data = null
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
