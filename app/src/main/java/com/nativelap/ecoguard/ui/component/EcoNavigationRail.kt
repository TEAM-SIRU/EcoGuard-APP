package com.nativelap.ecoguard.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.extraColors

@Composable
internal fun EcoNavigationRail(
    selectedTab: EcoBottomTab,
    onTabSelected: (EcoBottomTab) -> Unit,
    onCameraClick: () -> Unit,
    modifier: Modifier = Modifier,
    isCameraEnabled: Boolean = true,
    selectedIconResOverride: Int? = null,
) {
    NavigationRail(
        modifier = modifier.testTag("eco-navigation-rail"),
        header = {
            EcoCameraAction(
                onClick = onCameraClick,
                isEnabled = isCameraEnabled,
                modifier = Modifier.padding(bottom = AppSpacing.sm),
            )
        },
    ) {
        EcoBottomTab.entries.forEach { tab ->
            val isSelected = selectedTab == tab
            val iconRes =
                if (isSelected) {
                    selectedIconResOverride ?: tab.selectedIconRes
                } else {
                    tab.unselectedIconRes
                }
            NavigationRailItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        painter = painterResource(iconRes),
                        contentDescription = null,
                    )
                },
                label = { Text(stringResource(tab.labelRes)) },
                alwaysShowLabel = true,
                colors =
                    NavigationRailItemDefaults.colors(
                        selectedIconColor = MaterialTheme.extraColors.activeTabLabelColor,
                        selectedTextColor = MaterialTheme.extraColors.activeTabLabelColor,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        unselectedIconColor = MaterialTheme.extraColors.captionTextColor,
                        unselectedTextColor = MaterialTheme.extraColors.captionTextColor,
                    ),
            )
        }
    }
}
