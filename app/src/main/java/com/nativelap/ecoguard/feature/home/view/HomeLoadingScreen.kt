package com.nativelap.ecoguard.feature.home.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.home.viewmodel.HomeScreenEvent
import com.nativelap.ecoguard.ui.component.EcoBottomTab
import com.nativelap.ecoguard.ui.component.EcoBottomTabBar
import com.nativelap.ecoguard.ui.component.LoadingSkeletonScrollContent
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

/** 02 홈 로딩. 회색 제목과 스켈레톤을 표시한다. */
@Composable
fun HomeLoadingScreen(
    modifier: Modifier = Modifier,
    onEvent: (HomeScreenEvent) -> Unit = {},
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            EcoBottomTabBar(selectedTab = EcoBottomTab.HOME, onTabSelected = {
                onEvent(HomeScreenEvent.TabSelect(it))
            }, onCameraClick = { onEvent(HomeScreenEvent.CleaningVerificationClick) })
        },
    ) { innerPadding ->
        LoadingSkeletonScrollContent(
            innerPadding = innerPadding,
            title = stringResource(R.string.brand_name),
        )
    }
}

@Preview(name = "Home · loading", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun HomeLoadingScreenPreview() {
    EcoGuardTheme {
        HomeLoadingScreen()
    }
}
