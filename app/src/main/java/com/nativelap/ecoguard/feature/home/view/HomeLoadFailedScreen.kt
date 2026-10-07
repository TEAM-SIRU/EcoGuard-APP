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
import com.nativelap.ecoguard.ui.component.BottomCtaBar
import com.nativelap.ecoguard.ui.component.CenteredScrollContent
import com.nativelap.ecoguard.ui.component.EcoBackTopBar
import com.nativelap.ecoguard.ui.component.EcoPrimaryButton
import com.nativelap.ecoguard.ui.component.EcoSecondaryButton
import com.nativelap.ecoguard.ui.component.StatusMessage
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

@Composable
fun HomeLoadFailedScreen(
    onEvent: (HomeScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            EcoBackTopBar(onBackClick = { onEvent(HomeScreenEvent.BackClick) })
        },
        bottomBar = {
            BottomCtaBar {
                EcoPrimaryButton(
                    text = stringResource(R.string.action_retry),
                    onClick = { onEvent(HomeScreenEvent.RetryClick) },
                )

                EcoSecondaryButton(
                    text = stringResource(R.string.action_view_notices),
                    onClick = { onEvent(HomeScreenEvent.NoticeListClick) },
                )
            }
        },
    ) { innerPadding ->
        CenteredScrollContent(innerPadding = innerPadding) {
            StatusMessage(
                title = stringResource(R.string.home_load_failed),
                description = stringResource(R.string.home_load_failed_description),
            )
        }
    }
}

@Preview(name = "Home · load failed", showBackground = true, widthDp = 390, heightDp = 844)
@Preview(name = "Home · load failed compact larger text", showBackground = true, widthDp = 320, heightDp = 596, fontScale = 1.5f)
@Composable
private fun HomeLoadFailedScreenPreview() {
    EcoGuardTheme {
        HomeLoadFailedScreen(onEvent = {})
    }
}
