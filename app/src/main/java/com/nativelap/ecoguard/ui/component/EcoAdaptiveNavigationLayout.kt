package com.nativelap.ecoguard.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldLayout
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun EcoAdaptiveNavigationLayout(
    selectedTab: EcoBottomTab,
    onTabSelected: (EcoBottomTab) -> Unit,
    onCameraClick: () -> Unit,
    modifier: Modifier = Modifier,
    isCameraEnabled: Boolean = true,
    selectedIconResOverride: Int? = null,
    content: @Composable (usesNavigationRail: Boolean) -> Unit,
) {
    val usesNavigationRail = isExpandedLayout()
    Box(modifier = modifier.fillMaxSize()) {
        NavigationSuiteScaffoldLayout(
            navigationSuite = {
                if (usesNavigationRail) {
                    EcoNavigationRail(
                        selectedTab = selectedTab,
                        onTabSelected = onTabSelected,
                        onCameraClick = onCameraClick,
                        isCameraEnabled = isCameraEnabled,
                        selectedIconResOverride = selectedIconResOverride,
                    )
                }
            },
            layoutType = if (usesNavigationRail) NavigationSuiteType.NavigationRail else NavigationSuiteType.None,
        ) {
            content(usesNavigationRail)
        }
    }
}
