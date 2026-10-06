package com.nativelap.ecoguard.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val lightPrimaryColor = Color(0xFF57C144)
private val lightOnPrimaryColor = Color.White
private val lightPrimaryContainerColor = Color(0xFFEDF8EB)
private val lightOnPrimaryContainerColor = Color(0xFF57C144)
private val lightSecondaryColor = Color(0xFF57C144)
private val lightOnSecondaryColor = Color.White
private val lightSecondaryContainerColor = Color(0xFFF2F4F3)
private val lightOnSecondaryContainerColor = Color(0xFF4E5753)
private val lightTertiaryColor = Color(0xFF57C144)
private val lightOnTertiaryColor = Color.White
private val lightTertiaryContainerColor = Color(0xFFEDF8EB)
private val lightOnTertiaryContainerColor = Color(0xFF57C144)
private val lightBackgroundColor = Color(0xFFF8FAF9)
private val lightOnBackgroundColor = Color(0xFF1A1F1D)
private val lightSurfaceColor = Color.White
private val lightOnSurfaceColor = Color(0xFF1A1F1D)
private val lightSurfaceVariantColor = Color(0xFFF2F4F3)
private val lightOnSurfaceVariantColor = Color(0xFF4E5753)
private val lightSurfaceContainerLowColor = Color(0xFFF8FAF9)
private val lightSurfaceContainerHighestColor = Color(0xFFF2F4F3)
private val lightErrorColor = Color(0xFFD83B3B)
private val lightOnErrorColor = Color.White
private val lightOutlineColor = Color(0xFFB3BAB6)
private val lightOutlineVariantColor = Color(0xFFE5E9E7)
private val lightScrimColor = Color(0xFF1A1F1D)

private val LightColorScheme = lightColorScheme(
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

private val LightExtraColors = AppExtraColors(
    captionTextColor = Color(0xFF6B7470),
    disabledContentColor = Color(0xFFB3BAB6),
    warningTextColor = Color(0xFFB35F00),
    warningAccentColor = Color(0xFFF08C00),
    cardShadowColor = Color(0xFF1A211F),
)

private val DarkExtraColors = LightExtraColors

@Composable
fun EcoGuardTheme(
    themeMode: ThemeMode = ThemeMode.LIGHT,
    content: @Composable () -> Unit,
) {
    val useDarkTheme = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }
    val colorScheme = if (useDarkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }
    val extraColors = if (useDarkTheme) {
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
