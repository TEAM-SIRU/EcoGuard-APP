package com.nativelap.ecoguard.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle

@Immutable
data class AppExtraTypography(
    val statusTitle: TextStyle,
    val statusBody: TextStyle,
    val captionRegular: TextStyle,
    val loadingStatus: TextStyle,
    val tabLabel: TextStyle,
    val highlightNumber: TextStyle,
)

val LocalAppExtraTypography =
    staticCompositionLocalOf<AppExtraTypography> {
        error("AppExtraTypography was not provided.")
    }

val MaterialTheme.extraTypography: AppExtraTypography
    @Composable
    @ReadOnlyComposable
    get() = LocalAppExtraTypography.current
