package com.nativelap.ecoguard.feature.home.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.home.viewmodel.HomeScreenEvent
import com.nativelap.ecoguard.ui.component.CenteredScrollContent
import com.nativelap.ecoguard.ui.component.EcoBottomTab
import com.nativelap.ecoguard.ui.component.EcoBottomTabBar
import com.nativelap.ecoguard.ui.component.EcoPrimaryButton
import com.nativelap.ecoguard.ui.component.EcoTopBar
import com.nativelap.ecoguard.ui.component.StatusMessage
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

@Composable
fun HomeLoadFailedScreen(
    onEvent: (HomeScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            EcoTopBar(stringResource(R.string.brand_name), onActionClick = { onEvent(HomeScreenEvent.NoticeIconClick) })
        },
        bottomBar = {
            EcoBottomTabBar(
                selectedTab = EcoBottomTab.HOME,
                onTabSelected = { onEvent(HomeScreenEvent.TabSelect(it)) },
                onCameraClick = { onEvent(HomeScreenEvent.CleaningVerificationClick) },
            )
        },
    ) { innerPadding ->
        CenteredScrollContent(innerPadding = innerPadding) {
            HomeLoadFailedContent(onRetryClick = { onEvent(HomeScreenEvent.RetryClick) })
        }
    }
}

@Composable
private fun HomeLoadFailedContent(onRetryClick: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(AppSpacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        StatusMessage(
            title = stringResource(R.string.home_load_failed),
            description = stringResource(R.string.home_load_failed_description),
        )
        EcoPrimaryButton(
            text = stringResource(R.string.action_retry),
            onClick = onRetryClick,
            modifier = Modifier.widthIn(max = 200.dp),
        )
    }
}

@Preview(name = "Home · failed · 823:4255", showBackground = true, widthDp = 390, heightDp = 844)
@Preview(name = "Home · failed large text", showBackground = true, widthDp = 320, heightDp = 596, fontScale = 1.5f)
@Composable
private fun HomeLoadFailedScreenPreview() {
    EcoGuardTheme { HomeLoadFailedScreen({}) }
}
