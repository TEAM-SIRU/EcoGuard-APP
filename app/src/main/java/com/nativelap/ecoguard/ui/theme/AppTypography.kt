package com.nativelap.ecoguard.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.nativelap.ecoguard.R

private val NotoSansKrFontFamily =
    FontFamily(
        Font(R.font.noto_sans_kr_regular, FontWeight.Normal),
        Font(R.font.noto_sans_kr_medium, FontWeight.Medium),
        Font(R.font.noto_sans_kr_bold, FontWeight.Bold),
    )

private val CenteredLineHeightStyle =
    LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None,
    )

// Figma 텍스트 스타일은 행간 안에서 글자를 세로 중앙에 둔다.
private fun notoSansKrTextStyle(
    fontWeight: FontWeight,
    fontSize: TextUnit,
    lineHeight: TextUnit,
    letterSpacing: TextUnit,
) = TextStyle(
    fontFamily = NotoSansKrFontFamily,
    fontWeight = fontWeight,
    fontSize = fontSize,
    lineHeight = lineHeight,
    letterSpacing = letterSpacing,
    lineHeightStyle = CenteredLineHeightStyle,
)

private val defaultTypography = Typography()

// 제목·버튼처럼 짧은 한국어 문구는 어절 단위로, 줄 길이를 고르게 줄바꿈해 한두 글자만 다음 줄로 넘어가지 않게 한다.
// 본문은 Figma처럼 기본(음절 단위) 줄바꿈을 유지한다.
private val PhraseHeadingLineBreak = LineBreak.Heading.copy(wordBreak = LineBreak.WordBreak.Phrase)

// 어절 단위 줄바꿈은 텍스트 언어가 한국어일 때만 적용되므로, 기기 언어와 무관하게 한국어로 지정한다.
private val KoreanLocaleList = LocaleList("ko-KR")

private fun TextStyle.withPhraseLineBreak() =
    copy(
        lineBreak = PhraseHeadingLineBreak,
        localeList = KoreanLocaleList,
    )

private fun TextStyle.withNotoSansKr() = copy(fontFamily = NotoSansKrFontFamily)

private val BaseTypography =
    defaultTypography.copy(
        displayLarge = defaultTypography.displayLarge.withNotoSansKr(),
        displayMedium = defaultTypography.displayMedium.withNotoSansKr(),
        displaySmall = defaultTypography.displaySmall.withNotoSansKr(),
        headlineLarge =
            notoSansKrTextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                lineHeight = 30.sp,
                letterSpacing = (-0.44f).sp,
            ),
        headlineMedium =
            notoSansKrTextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp,
                lineHeight = 27.sp,
                letterSpacing = (-0.38f).sp,
            ),
        headlineSmall =
            notoSansKrTextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                lineHeight = 25.sp,
                letterSpacing = (-0.34f).sp,
            ),
        titleLarge =
            notoSansKrTextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                lineHeight = 25.sp,
                letterSpacing = (-0.34f).sp,
            ),
        titleMedium =
            notoSansKrTextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                lineHeight = 22.sp,
                letterSpacing = (-0.15f).sp,
            ),
        titleSmall =
            notoSansKrTextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = (-0.14f).sp,
            ),
        bodyLarge =
            notoSansKrTextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                lineHeight = 22.sp,
                letterSpacing = (-0.15f).sp,
            ),
        bodyMedium =
            notoSansKrTextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                letterSpacing = (-0.14f).sp,
            ),
        bodySmall =
            notoSansKrTextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                lineHeight = 19.sp,
                letterSpacing = (-0.13f).sp,
            ),
        labelLarge =
            notoSansKrTextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                lineHeight = 22.sp,
                letterSpacing = (-0.15f).sp,
            ),
        labelMedium =
            notoSansKrTextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = (-0.14f).sp,
            ),
        labelSmall =
            notoSansKrTextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                lineHeight = 19.sp,
                letterSpacing = (-0.13f).sp,
            ),
    )

val EcoGuardTypography =
    BaseTypography.copy(
        headlineLarge = BaseTypography.headlineLarge.withPhraseLineBreak(),
        headlineMedium = BaseTypography.headlineMedium.withPhraseLineBreak(),
        headlineSmall = BaseTypography.headlineSmall.withPhraseLineBreak(),
        titleLarge = BaseTypography.titleLarge.withPhraseLineBreak(),
        titleMedium = BaseTypography.titleMedium.withPhraseLineBreak(),
        titleSmall = BaseTypography.titleSmall.withPhraseLineBreak(),
        labelLarge = BaseTypography.labelLarge.withPhraseLineBreak(),
        labelMedium = BaseTypography.labelMedium.withPhraseLineBreak(),
    )

val EcoGuardExtraTypography =
    AppExtraTypography(
        statusTitle =
            notoSansKrTextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp,
                lineHeight = 27.sp,
                letterSpacing = (0f).sp,
            ).withPhraseLineBreak(),
        statusBody =
            notoSansKrTextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                letterSpacing = (0f).sp,
            ),
        captionRegular =
            notoSansKrTextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                letterSpacing = (-0.12f).sp,
            ),
        loadingStatus =
            notoSansKrTextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 20.sp,
                letterSpacing = (0f).sp,
            ),
        tabLabel =
            notoSansKrTextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.sp,
            ),
        highlightNumber =
            notoSansKrTextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                letterSpacing = (0f).sp,
            ),
    )
