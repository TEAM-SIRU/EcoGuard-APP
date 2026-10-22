package com.nativelap.ecoguard.feature.demo.view

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.ui.component.CenteredScrollContent
import com.nativelap.ecoguard.ui.component.EcoBackTopBar
import com.nativelap.ecoguard.ui.component.StatusMessage

@Composable
fun AppealReviewingScreen(onBack: () -> Unit) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { EcoBackTopBar(onBackClick = onBack) },
    ) { innerPadding ->
        CenteredScrollContent(innerPadding = innerPadding) {
            StatusMessage(
                iconRes = R.drawable.ic_clock_64,
                title = stringResource(R.string.appeal_reviewing_title),
                description = stringResource(R.string.appeal_reviewing_description),
            )
        }
    }
}
