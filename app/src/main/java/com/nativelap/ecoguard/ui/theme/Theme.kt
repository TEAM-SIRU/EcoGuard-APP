package com.nativelap.ecoguard.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val lightPrimaryColor = Color(0xFF55B580)
private val lightOnPrimaryColor = Color.White
private val lightPrimaryContainerColor = Color(0xFFEAF6EF)
private val lightOnPrimaryContainerColor = Color(0xFF2B7A52)
private val lightSecondaryColor = Color(0xFF30875B)
private val lightOnSecondaryColor = Color.White
private val lightSecondaryContainerColor = Color(0xFFF1F3F2)
private val lightOnSecondaryContainerColor = Color(0xFF4A544E)
private val lightTertiaryColor = Color(0xFF55B580)
private val lightOnTertiaryColor = Color.White
private val lightTertiaryContainerColor = Color(0xFFEAF6EF)
private val lightOnTertiaryContainerColor = Color(0xFF2B7A52)
private val lightBackgroundColor = Color(0xFFFAFBFA)
private val lightOnBackgroundColor = Color(0xFF1A211D)
private val lightSurfaceColor = Color.White
private val lightOnSurfaceColor = Color(0xFF1A211D)
private val lightSurfaceVariantColor = Color(0xFFF1F3F2)
private val lightOnSurfaceVariantColor = Color(0xFF4A544E)
private val lightSurfaceContainerLowColor = Color(0xFFFAFBFA)
private val lightSurfaceContainerHighestColor = Color(0xFFF1F3F2)
private val lightErrorColor = Color(0xFFB85551)
private val lightOnErrorColor = Color.White
private val lightOutlineColor = Color(0xFFAEB7B2)
private val lightOutlineVariantColor = Color(0xFFDCE3DF)
private val lightScrimColor = Color(0xFF1A211D)

private val LightColorScheme =
    lightColorScheme(
        primary = lightPrimaryColor,
        onPrimary = lightOnPrimaryColor,
        primaryContainer = lightPrimaryContainerColor,
        onPrimaryContainer = lightOnPrimaryContainerColor,
        inversePrimary = lightPrimaryColor,
        secondary = lightSecondaryColor,
        onSecondary = lightOnSecondaryColor,
        secondaryContainer = lightSecondaryContainerColor,
        onSecondaryContainer = lightOnSecondaryContainerColor,
        tertiary = lightTertiaryColor,
        onTertiary = lightOnTertiaryColor,
        tertiaryContainer = lightTertiaryContainerColor,
        onTertiaryContainer = lightOnTertiaryContainerColor,
        background = lightBackgroundColor,
        onBackground = lightOnBackgroundColor,
        surface = lightSurfaceColor,
        onSurface = lightOnSurfaceColor,
        surfaceVariant = lightSurfaceVariantColor,
        onSurfaceVariant = lightOnSurfaceVariantColor,
        surfaceTint = lightPrimaryColor,
        inverseSurface = lightOnSurfaceColor,
        inverseOnSurface = lightSurfaceColor,
        error = lightErrorColor,
        onError = lightOnErrorColor,
        errorContainer = lightSurfaceColor,
        onErrorContainer = lightErrorColor,
        outline = lightOutlineColor,
        outlineVariant = lightOutlineVariantColor,
        scrim = lightScrimColor,
        surfaceBright = lightSurfaceColor,
        surfaceDim = lightSurfaceVariantColor,
        surfaceContainerLowest = lightSurfaceColor,
        surfaceContainerLow = lightSurfaceContainerLowColor,
        surfaceContainer = lightSurfaceColor,
        surfaceContainerHigh = lightSurfaceColor,
        surfaceContainerHighest = lightSurfaceContainerHighestColor,
    )

// Figma에 다크 디자인이 없어 확정 전까지 다크 모드도 라이트 값을 사용한다.
private val DarkColorScheme = LightColorScheme

private val LightExtraColors =
    AppExtraColors(
        captionTextColor = Color(0xFF65706A),
        disabledContentColor = Color(0xFFAEB7B2),
        warningTextColor = Color(0xFF9A6430),
        warningAccentColor = Color(0xFFCF9859),
        cardShadowColor = Color(0xFF1A211D),
        cameraBackgroundColor = Color(0xFF111613),
        cameraSurfaceColor = Color(0xFF2A312D),
        cameraSecondaryTextColor = Color(0xFFC4CAD1),
        cameraOverlayColor = Color.Black,
    )

private val DarkExtraColors = LightExtraColors

@Composable
fun EcoGuardTheme(
    themeMode: ThemeMode = ThemeMode.LIGHT,
    content: @Composable () -> Unit,
) {
    val useDarkTheme =
        when (themeMode) {
            ThemeMode.SYSTEM -> isSystemInDarkTheme()
            ThemeMode.LIGHT -> false
            ThemeMode.DARK -> true
        }
    val colorScheme =
        if (useDarkTheme) {
            DarkColorScheme
        } else {
            LightColorScheme
        }
    val extraColors =
        if (useDarkTheme) {
            DarkExtraColors
        } else {
            LightExtraColors
        }

    CompositionLocalProvider(
        LocalAppExtraColors provides extraColors,
        LocalAppExtraTypography provides EcoGuardExtraTypography,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = EcoGuardTypography,
            content = content,
        )
    }
}
