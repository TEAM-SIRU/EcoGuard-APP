package com.nativelap.ecoguard.feature.home.view

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
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
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.ThemeMode

private val homeLoadingContentMaxWidth = 600.dp
private val homeLoadingCompactHeightThreshold = 640.dp
private const val HOME_LOADING_LARGE_FONT_SCALE_THRESHOLD = 1.3f

@Composable
fun HomeLoadingScreen(
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
            contentAlignment = Alignment.TopCenter,
        ) {
            val isCompactLargeText = maxHeight < homeLoadingCompactHeightThreshold &&
                LocalDensity.current.fontScale >= HOME_LOADING_LARGE_FONT_SCALE_THRESHOLD
            val itemSpacing = if (isCompactLargeText) {
                AppSpacing.lg
            } else {
                AppSpacing.xl
            }

            Column(
                modifier = Modifier
                    .widthIn(max = homeLoadingContentMaxWidth)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        horizontal = AppSpacing.screenHorizontal,
                        vertical = AppSpacing.xl,
                    ),
                verticalArrangement = Arrangement.spacedBy(itemSpacing),
            ) {
                HomeLoadingTitle()

                HomeLoadingContent(itemSpacing = itemSpacing)
            }
        }
    }
}

@Preview(
    name = "Home loading screen · Figma",
    showBackground = true,
    showSystemUi = true,
    widthDp = 390,
    heightDp = 844,
)
@Preview(
    name = "Home loading screen · compact",
    showBackground = true,
    widthDp = 320,
    heightDp = 596,
)
@Preview(
    name = "Home loading screen · tablet",
    showBackground = true,
    widthDp = 840,
    heightDp = 856,
)
@Preview(
    name = "Home loading screen · larger text",
    showBackground = true,
    widthDp = 390,
    heightDp = 844,
    fontScale = 1.5f,
)
@Preview(
    name = "Home loading screen · compact larger text",
    showBackground = true,
    widthDp = 320,
    heightDp = 596,
    fontScale = 1.5f,
)
@Preview(
    name = "Home loading screen · light under system dark",
    showBackground = true,
    widthDp = 390,
    heightDp = 844,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    name = "Home loading screen · RTL",
    showBackground = true,
    widthDp = 390,
    heightDp = 844,
    locale = "ar",
)
@Composable
private fun HomeLoadingScreenPreview() {
    EcoGuardTheme(themeMode = ThemeMode.LIGHT) {
        HomeLoadingScreen()
    }
}
