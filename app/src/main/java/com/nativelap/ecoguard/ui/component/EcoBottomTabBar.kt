package com.nativelap.ecoguard.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppIconSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.extraColors
import com.nativelap.ecoguard.ui.theme.extraTypography

private val tabBarShadowRadius = 8.dp
private val tabBarShadowOffsetY = (-4).dp
private const val TAB_BAR_SHADOW_ALPHA = 0.06f
private val cameraFabOffsetY = (-18).dp
private val cameraFabShadowRadius = 14.dp
private val cameraFabShadowOffsetY = 6.dp
private const val CAMERA_FAB_SHADOW_ALPHA = 0.32f
private val tabItemTopPadding = 10.dp

/** 홈·구역·카메라 FAB·기록·마이페이지로 구성된 하단 탭 바. */
@Composable
fun EcoBottomTabBar(
    selectedTab: EcoBottomTab,
    onTabSelected: (EcoBottomTab) -> Unit,
    onCameraClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .dropShadow(
                    shape = RectangleShape,
                    shadow = Shadow(
                        radius = tabBarShadowRadius,
                        color = MaterialTheme.extraColors.cardShadowColor,
                        offset = DpOffset(x = 0.dp, y = tabBarShadowOffsetY),
                        alpha = TAB_BAR_SHADOW_ALPHA,
                    ),
                )
                .background(MaterialTheme.colorScheme.surface)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .heightIn(min = AppComponentSize.tabBarHeight)
                .padding(horizontal = AppSpacing.xs)
                .selectableGroup(),
        ) {
            EcoBottomTabItem(
                tab = EcoBottomTab.HOME,
                isSelected = selectedTab == EcoBottomTab.HOME,
                onClick = { onTabSelected(EcoBottomTab.HOME) },
                modifier = Modifier.weight(1f),
            )

            EcoBottomTabItem(
                tab = EcoBottomTab.AREA,
                isSelected = selectedTab == EcoBottomTab.AREA,
                onClick = { onTabSelected(EcoBottomTab.AREA) },
                modifier = Modifier.weight(1f),
            )

            Spacer(modifier = Modifier.weight(1f))

            EcoBottomTabItem(
                tab = EcoBottomTab.ACTIVITY,
                isSelected = selectedTab == EcoBottomTab.ACTIVITY,
                onClick = { onTabSelected(EcoBottomTab.ACTIVITY) },
                modifier = Modifier.weight(1f),
            )

            EcoBottomTabItem(
                tab = EcoBottomTab.MY_PAGE,
                isSelected = selectedTab == EcoBottomTab.MY_PAGE,
                onClick = { onTabSelected(EcoBottomTab.MY_PAGE) },
                modifier = Modifier.weight(1f),
            )
        }

        CameraFab(
            onClick = onCameraClick,
            modifier = Modifier.offset(y = cameraFabOffsetY),
        )
    }
}

@Composable
private fun EcoBottomTabItem(
    tab: EcoBottomTab,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.extraColors.captionTextColor
    }
    val iconRes = if (isSelected) {
        tab.selectedIconRes
    } else {
        tab.unselectedIconRes
    }
    val labelWeight = if (isSelected) {
        FontWeight.Bold
    } else {
        FontWeight.Medium
    }

    Column(
        modifier = modifier
            .heightIn(min = AppComponentSize.tabBarHeight)
            .selectable(
                selected = isSelected,
                onClick = onClick,
                role = Role.Tab,
            )
            .padding(top = tabItemTopPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppSpacing.xxs),
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier.size(AppIconSize.standard),
            tint = contentColor,
        )

        Text(
            text = stringResource(tab.labelRes),
            style = MaterialTheme.extraTypography.tabLabel.copy(fontWeight = labelWeight),
            color = contentColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun CameraFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .size(AppComponentSize.cameraFab)
            .dropShadow(
                shape = CircleShape,
                shadow = Shadow(
                    radius = cameraFabShadowRadius,
                    color = MaterialTheme.colorScheme.primary,
                    offset = DpOffset(x = 0.dp, y = cameraFabShadowOffsetY),
                    alpha = CAMERA_FAB_SHADOW_ALPHA,
                ),
            ),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        border = BorderStroke(
            width = AppComponentSize.cameraFabBorder,
            color = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(R.drawable.ic_cam_26),
                contentDescription = stringResource(R.string.cd_camera_verification),
                modifier = Modifier.size(AppIconSize.fab),
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8FAF9, widthDp = 390, heightDp = 140)
@Composable
private fun EcoBottomTabBarPreview() {
    EcoGuardTheme {
        Box(
            modifier = Modifier.padding(top = AppSpacing.xl * 2),
        ) {
            EcoBottomTabBar(
                selectedTab = EcoBottomTab.HOME,
                onTabSelected = {},
                onCameraClick = {},
            )
        }
    }
}
