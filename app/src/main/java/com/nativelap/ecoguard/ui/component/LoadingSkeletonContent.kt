package com.nativelap.ecoguard.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.ThemeMode

private val skeletonBarHeight = 22.dp
private val skeletonTitleSkeletonWidth = 160.dp
private val skeletonMainSkeletonHeight = 136.dp
private val skeletonRowSkeletonHeight = 62.dp
private val skeletonFooterSkeletonWidth = 220.dp

/** 로딩 중 카드 자리를 보여주는 스켈레톤 5개와 로딩 안내 문구. */
@Composable
fun LoadingSkeletonContent(
    modifier: Modifier = Modifier,
    itemSpacing: Dp = AppSpacing.xl,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(itemSpacing),
    ) {
        SkeletonPlaceholder(
            modifier = Modifier
                .widthIn(max = skeletonTitleSkeletonWidth)
                .fillMaxWidth()
                .height(skeletonBarHeight),
        )

        SkeletonPlaceholder(
            modifier = Modifier
                .fillMaxWidth()
                .height(skeletonMainSkeletonHeight),
        )

        SkeletonPlaceholder(
            modifier = Modifier
                .fillMaxWidth()
                .height(skeletonRowSkeletonHeight),
        )

        SkeletonPlaceholder(
            modifier = Modifier
                .fillMaxWidth()
                .height(skeletonRowSkeletonHeight),
        )

        SkeletonPlaceholder(
            modifier = Modifier
                .widthIn(max = skeletonFooterSkeletonWidth)
                .fillMaxWidth()
                .height(skeletonBarHeight),
        )

        LoadingStatusText()
    }
}

@Preview(
    name = "Loading skeleton · 390dp content",
    showBackground = true,
    widthDp = 390,
    heightDp = 800,
)
@Preview(
    name = "Loading skeleton · compact content",
    showBackground = true,
    widthDp = 320,
    heightDp = 596,
)
@Preview(
    name = "Loading skeleton · tablet content",
    showBackground = true,
    widthDp = 840,
    heightDp = 856,
)
@Preview(
    name = "Loading skeleton · larger text content",
    showBackground = true,
    widthDp = 390,
    heightDp = 800,
    fontScale = 1.5f,
)
@Preview(
    name = "Loading skeleton · compact larger text",
    showBackground = true,
    widthDp = 320,
    heightDp = 596,
    fontScale = 1.5f,
)
@Preview(
    name = "Loading skeleton · RTL",
    showBackground = true,
    widthDp = 390,
    heightDp = 800,
    locale = "ar",
)
@Composable
private fun LoadingSkeletonContentPreview() {
    EcoGuardTheme(themeMode = ThemeMode.LIGHT) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface,
        ) {
            LoadingSkeletonContent()
        }
    }
}
