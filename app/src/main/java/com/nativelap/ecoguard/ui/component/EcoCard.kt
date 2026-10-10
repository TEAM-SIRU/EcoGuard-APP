package com.nativelap.ecoguard.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.ui.theme.AppRadius
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.extraColors

private val defaultCardShadowRadius = 18.dp
private val defaultCardShadowOffsetY = 6.dp
private const val DEFAULT_CARD_SHADOW_ALPHA = 0.07f
private val emphasizedCardShadowRadius = 18.dp
private val emphasizedCardShadowOffsetY = 6.dp
private const val EMPHASIZED_CARD_SHADOW_ALPHA = 0.07f

/** 흰 배경과 Figma 카드 그림자를 가진 기본 카드. emphasized는 공지 카드의 강한 그림자. */
@Composable
fun EcoCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = AppRadius.card,
    contentPadding: PaddingValues = PaddingValues(AppSpacing.lg),
    emphasized: Boolean = false,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val cardShape = RoundedCornerShape(cornerRadius)
    val cardShadow =
        if (emphasized) {
            Shadow(
                radius = emphasizedCardShadowRadius,
                color = MaterialTheme.extraColors.cardShadowColor,
                offset = DpOffset(x = 0.dp, y = emphasizedCardShadowOffsetY),
                alpha = EMPHASIZED_CARD_SHADOW_ALPHA,
            )
        } else {
            Shadow(
                radius = defaultCardShadowRadius,
                color = MaterialTheme.extraColors.cardShadowColor,
                offset = DpOffset(x = 0.dp, y = defaultCardShadowOffsetY),
                alpha = DEFAULT_CARD_SHADOW_ALPHA,
            )
        }

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .dropShadow(
                    shape = cardShape,
                    shadow = cardShadow,
                ).dropShadow(
                    shape = cardShape,
                    shadow =
                        Shadow(
                            radius = 3.dp,
                            color = MaterialTheme.extraColors.cardShadowColor,
                            offset = DpOffset(0.dp, 1.dp),
                            alpha = 0.04f,
                        ),
                ).clip(cardShape)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, cardShape)
                .background(MaterialTheme.colorScheme.surface)
                .then(
                    if (onClick != null) {
                        Modifier.clickable(onClick = onClick)
                    } else {
                        Modifier
                    },
                ).padding(contentPadding),
        content = content,
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF8FAF9, widthDp = 390)
@Composable
private fun EcoCardPreview() {
    EcoGuardTheme {
        EcoCard(modifier = Modifier.padding(AppSpacing.xl)) {
            Text(
                text = "이번 주 청소",
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}
