package com.nativelap.ecoguard.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class AppExtraColors(
    val captionTextColor: Color,
    val disabledContentColor: Color,
    val warningTextColor: Color,
    val warningAccentColor: Color,
    val cardShadowColor: Color,
    val cameraBackgroundColor: Color,
    val cameraSurfaceColor: Color,
    val cameraSecondaryTextColor: Color,
    val cameraOverlayColor: Color,
)

val LocalAppExtraColors = staticCompositionLocalOf<AppExtraColors> {
    error("AppExtraColors was not provided.")
}

val MaterialTheme.extraColors: AppExtraColors
    @Composable
    @ReadOnlyComposable
    get() = LocalAppExtraColors.current
