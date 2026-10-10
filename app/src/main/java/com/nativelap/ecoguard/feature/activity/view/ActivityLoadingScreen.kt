package com.nativelap.ecoguard.feature.activity.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.activity.viewmodel.ActivityScreenEvent
import com.nativelap.ecoguard.ui.component.EcoBottomTab
import com.nativelap.ecoguard.ui.component.EcoBottomTabBar
import com.nativelap.ecoguard.ui.component.LoadingSkeletonScrollContent
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

/** 07 활동 기록 로딩. 회색 제목과 스켈레톤을 표시한다. */
@Composable
fun ActivityLoadingScreen(
    modifier: Modifier = Modifier,
    onEvent: (ActivityScreenEvent) -> Unit = {},
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            EcoBottomTabBar(selectedTab = EcoBottomTab.ACTIVITY, onTabSelected = {
                onEvent(ActivityScreenEvent.TabSelect(it))
            }, onCameraClick = { onEvent(ActivityScreenEvent.CameraClick) })
        },
    ) { innerPadding ->
        LoadingSkeletonScrollContent(
            innerPadding = innerPadding,
            title = stringResource(R.string.activity_title),
        )
    }
}

@Preview(name = "Activity · loading", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun ActivityLoadingScreenPreview() {
    EcoGuardTheme {
        ActivityLoadingScreen()
    }
}
