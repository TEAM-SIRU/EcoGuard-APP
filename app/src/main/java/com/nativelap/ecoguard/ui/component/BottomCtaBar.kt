package com.nativelap.ecoguard.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

private val bottomCtaDefaultSpacing = 8.dp

/** 화면 하단에 고정되는 CTA 영역. 시스템 내비게이션 바 inset을 직접 처리한다. */
@Composable
fun BottomCtaBar(
    modifier: Modifier = Modifier,
    itemSpacing: Dp = bottomCtaDefaultSpacing,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(containerColor)
            .windowInsetsPadding(WindowInsets.navigationBars),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = AppComponentSize.contentMaxWidth)
                .fillMaxWidth()
                .padding(
                    start = AppSpacing.screenHorizontal,
                    end = AppSpacing.screenHorizontal,
                    top = AppSpacing.sm,
                    bottom = AppSpacing.md,
                ),
            verticalArrangement = Arrangement.spacedBy(itemSpacing),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = content,
        )
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun BottomCtaBarPreview() {
    EcoGuardTheme {
        BottomCtaBar {
            EcoPrimaryButton(
                text = "홈으로",
                onClick = {},
            )

            EcoSecondaryButton(
                text = "공지 보기",
                onClick = {},
            )
        }
    }
}
