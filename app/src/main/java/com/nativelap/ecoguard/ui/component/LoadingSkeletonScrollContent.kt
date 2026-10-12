package com.nativelap.ecoguard.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.ThemeMode

private val skeletonCompactHeightThreshold = 640.dp
private const val SKELETON_LARGE_FONT_SCALE_THRESHOLD = 1.3f

/** 홈·구역·기록·모집 화면의 로딩 상태 공통 본문. 회색 제목과 스켈레톤을 표시하며 Scaffold는 각 Screen이 배치한다. */
@Composable
fun LoadingSkeletonScrollContent(
    innerPadding: PaddingValues,
    title: String,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier =
            modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
        contentAlignment = Alignment.TopCenter,
    ) {
        val isCompactLargeText =
            maxHeight < skeletonCompactHeightThreshold &&
                LocalDensity.current.fontScale >= SKELETON_LARGE_FONT_SCALE_THRESHOLD
        val itemSpacing =
            if (isCompactLargeText) {
                AppSpacing.lg
            } else {
                AppSpacing.xl
            }

        Column(
            modifier =
                Modifier
                    .widthIn(max = AppComponentSize.contentMaxWidth)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        horizontal = AppSpacing.screenHorizontal,
                        vertical = AppSpacing.xl,
                    ),
            verticalArrangement = Arrangement.spacedBy(itemSpacing),
        ) {
            LoadingTitle(title = title)

            LoadingSkeletonContent(itemSpacing = itemSpacing)
        }
    }
}

@Preview(
    name = "Loading skeleton · Figma",
    showBackground = true,
    showSystemUi = true,
    widthDp = 390,
    heightDp = 844,
)
@Preview(
    name = "Loading skeleton · compact",
    showBackground = true,
    widthDp = 320,
    heightDp = 596,
)
@Preview(
    name = "Loading skeleton · tablet",
    showBackground = true,
    widthDp = 840,
    heightDp = 856,
)
@Preview(
    name = "Loading skeleton · larger text",
    showBackground = true,
    widthDp = 390,
    heightDp = 844,
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
    heightDp = 844,
    locale = "ar",
)
@Composable
private fun LoadingSkeletonScrollContentPreview() {
    EcoGuardTheme(themeMode = ThemeMode.LIGHT) {
        Scaffold(containerColor = MaterialTheme.colorScheme.surface) { innerPadding ->
            LoadingSkeletonScrollContent(
                innerPadding = innerPadding,
                title = "환경지킴이",
            )
        }
    }
}
