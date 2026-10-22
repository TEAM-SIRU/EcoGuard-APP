package com.nativelap.ecoguard.feature.demo.view

import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.ui.component.CenteredScrollContent
import com.nativelap.ecoguard.ui.component.EcoBackTopBar
import com.nativelap.ecoguard.ui.component.InlineEmptyState

@Composable
fun DemoMissingScreen(
    @StringRes titleRes: Int,
    onBack: () -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { EcoBackTopBar(onBackClick = onBack) },
    ) { innerPadding ->
        CenteredScrollContent(innerPadding = innerPadding) {
            InlineEmptyState(
                R.drawable.ic_map_30,
                stringResource(titleRes),
                stringResource(R.string.common_try_again_later),
                actionText = stringResource(R.string.action_back),
                onActionClick = onBack,
            )
        }
    }
}
