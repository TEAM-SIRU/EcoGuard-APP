package com.nativelap.ecoguard.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.nativelap.ecoguard.R

private val PretendardFontFamily = FontFamily(
    Font(R.font.pretendard_regular, FontWeight.Normal),
    Font(R.font.pretendard_medium, FontWeight.Medium),
    Font(R.font.pretendard_bold, FontWeight.Bold),
)

private val CenteredLineHeightStyle = LineHeightStyle(
    alignment = LineHeightStyle.Alignment.Center,
    trim = LineHeightStyle.Trim.None,
)

// Figma 텍스트 스타일은 행간 안에서 글자를 세로 중앙에 둔다.
private fun pretendardTextStyle(
    fontWeight: FontWeight,
    fontSize: TextUnit,
    lineHeight: TextUnit,
    letterSpacing: TextUnit,
) = TextStyle(
    fontFamily = PretendardFontFamily,
    fontWeight = fontWeight,
    fontSize = fontSize,
    lineHeight = lineHeight,
    letterSpacing = letterSpacing,
    lineHeightStyle = CenteredLineHeightStyle,
)

private val defaultTypography = Typography()

private fun TextStyle.withPretendard() = copy(fontFamily = PretendardFontFamily)

val EcoGuardTypography = defaultTypography.copy(
    displayLarge = defaultTypography.displayLarge.withPretendard(),
    displayMedium = defaultTypography.displayMedium.withPretendard(),
    displaySmall = defaultTypography.displaySmall.withPretendard(),
    // Title 1 · 26/36 Bold
    headlineLarge = pretendardTextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.52f).sp,
    ),
    // Title 2 · 22/31 Bold
    headlineMedium = pretendardTextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 31.sp,
        letterSpacing = (-0.44f).sp,
    ),
    // Title 3 · 20/29 Bold
    headlineSmall = pretendardTextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 29.sp,
        letterSpacing = (-0.4f).sp,
    ),
    titleLarge = pretendardTextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = (-0.18f).sp,
    ),
    titleMedium = pretendardTextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,
        lineHeight = 25.sp,
        letterSpacing = (-0.17f).sp,
    ),
    titleSmall = pretendardTextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 23.sp,
        letterSpacing = (-0.16f).sp,
    ),
    // Body 1 · 17/25 Medium
    bodyLarge = pretendardTextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 17.sp,
        lineHeight = 25.sp,
        letterSpacing = (-0.17f).sp,
    ),
    // Body 2 · 15/22 Regular
    bodyMedium = pretendardTextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.15f).sp,
    ),
    // Sub · 14/20 Regular
    bodySmall = pretendardTextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = (-0.14f).sp,
    ),
    // Primary 버튼 · 19/26 Bold
    labelLarge = pretendardTextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 19.sp,
        lineHeight = 26.sp,
        letterSpacing = (-0.19f).sp,
    ),
    // Secondary 버튼 · 17/24 Bold
    labelMedium = pretendardTextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.17f).sp,
    ),
    // Caption · 13/18 Bold
    labelSmall = pretendardTextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = (-0.13f).sp,
    ),
)

val EcoGuardExtraTypography = AppExtraTypography(
    statusTitle = pretendardTextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        lineHeight = 34.sp,
        letterSpacing = 0.sp,
    ),
    statusBody = pretendardTextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
    ),
    captionRegular = pretendardTextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = (-0.13f).sp,
    ),
    loadingStatus = pretendardTextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
    ),
    tabLabel = pretendardTextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp,
    ),
)
