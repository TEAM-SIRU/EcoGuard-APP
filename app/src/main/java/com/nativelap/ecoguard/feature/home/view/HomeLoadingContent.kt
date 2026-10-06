package com.nativelap.ecoguard.feature.home.view

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
import com.nativelap.ecoguard.ui.component.SkeletonPlaceholder
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.ThemeMode

private val homeLoadingBarHeight = 24.dp
private val homeLoadingTitleSkeletonWidth = 160.dp
private val homeLoadingMainSkeletonHeight = 156.dp
private val homeLoadingRowSkeletonHeight = 72.dp
private val homeLoadingFooterSkeletonWidth = 220.dp

@Composable
fun HomeLoadingContent(
    modifier: Modifier = Modifier,
    itemSpacing: Dp = AppSpacing.xl,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(itemSpacing),
    ) {
        SkeletonPlaceholder(
            modifier = Modifier
                .widthIn(max = homeLoadingTitleSkeletonWidth)
                .fillMaxWidth()
                .height(homeLoadingBarHeight),
        )

        SkeletonPlaceholder(
            modifier = Modifier
                .fillMaxWidth()
                .height(homeLoadingMainSkeletonHeight),
        )

        SkeletonPlaceholder(
            modifier = Modifier
                .fillMaxWidth()
                .height(homeLoadingRowSkeletonHeight),
        )

        SkeletonPlaceholder(
            modifier = Modifier
                .fillMaxWidth()
                .height(homeLoadingRowSkeletonHeight),
        )

        SkeletonPlaceholder(
            modifier = Modifier
                .widthIn(max = homeLoadingFooterSkeletonWidth)
                .fillMaxWidth()
                .height(homeLoadingBarHeight),
        )

        HomeLoadingStatus()
    }
}

@Preview(
    name = "Home loading · 390dp content",
    showBackground = true,
    widthDp = 390,
    heightDp = 800,
)
@Preview(
    name = "Home loading · compact content",
    showBackground = true,
    widthDp = 320,
    heightDp = 596,
)
@Preview(
    name = "Home loading · tablet content",
    showBackground = true,
    widthDp = 840,
    heightDp = 856,
)
@Preview(
    name = "Home loading · larger text content",
    showBackground = true,
    widthDp = 390,
    heightDp = 800,
    fontScale = 1.5f,
)
@Preview(
    name = "Home loading · compact larger text",
    showBackground = true,
    widthDp = 320,
    heightDp = 596,
    fontScale = 1.5f,
)
@Preview(
    name = "Home loading · RTL",
    showBackground = true,
    widthDp = 390,
    heightDp = 800,
    locale = "ar",
)
@Composable
private fun HomeLoadingContentPreview() {
    EcoGuardTheme(themeMode = ThemeMode.LIGHT) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface,
        ) {
            HomeLoadingContent()
        }
    }
}
