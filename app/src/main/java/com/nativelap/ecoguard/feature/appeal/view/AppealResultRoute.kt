package com.nativelap.ecoguard.feature.appeal.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.core.presentation.findActivity
import com.nativelap.ecoguard.core.presentation.rememberActivityViewModel
import com.nativelap.ecoguard.feature.appeal.viewmodel.AppealHistoryUiState
import com.nativelap.ecoguard.feature.appeal.viewmodel.AppealHistoryViewModel
import com.nativelap.ecoguard.ui.component.BottomCtaBar
import com.nativelap.ecoguard.ui.component.CenteredScrollContent
import com.nativelap.ecoguard.ui.component.EcoBackTopBar
import com.nativelap.ecoguard.ui.component.EcoPrimaryButton
import com.nativelap.ecoguard.ui.component.StatusChip
import com.nativelap.ecoguard.ui.component.StatusChipType
import com.nativelap.ecoguard.ui.theme.AppSpacing
import kotlinx.coroutines.flow.collect

@Composable
fun AppealResultRoute(
    appealId: Long,
    onBack: () -> Unit,
    viewModel: AppealHistoryViewModel? = null,
) {
    val screenViewModel = viewModel ?: rememberActivityViewModel<AppealHistoryViewModel>()
    val lifecycleOwner = LocalContext.current.findActivity()
    val uiState by
        produceState(
            initialValue = AppealHistoryUiState.Loading as AppealHistoryUiState,
            key1 = screenViewModel,
            key2 = lifecycleOwner,
        ) {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                screenViewModel.uiState.collect { state -> value = state }
            }
        }
    LaunchedEffect(screenViewModel, appealId) { screenViewModel.loadHistory() }
    val item = (uiState as? AppealHistoryUiState.Content)?.items?.firstOrNull { it.appealId == appealId }
    val failed = uiState is AppealHistoryUiState.LoadFailed
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { EcoBackTopBar(onBackClick = onBack) },
        bottomBar = {
            if (failed) {
                BottomCtaBar {
                    EcoPrimaryButton(
                        text = stringResource(R.string.action_retry),
                        onClick = screenViewModel::loadHistory,
                    )
                }
            }
        },
    ) { padding ->
        CenteredScrollContent(innerPadding = padding) {
            when {
                uiState is AppealHistoryUiState.Loading -> {
                    Text(stringResource(R.string.common_loading))
                }

                failed -> {
                    Text(stringResource(R.string.appeal_history_load_failed))
                }

                item == null -> {
                    Text(stringResource(R.string.appeal_result_missing))
                }

                else -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(AppSpacing.md),
                        modifier = Modifier.padding(AppSpacing.md),
                    ) {
                        Text(stringResource(R.string.appeal_attempt_summary, item.verificationDate, item.attemptNumber))
                        StatusChip(type = item.status)
                        item.earnedMinutes?.let { Text(stringResource(R.string.format_bonus_minutes, it)) }
                        if (item.status == StatusChipType.REJECTED) {
                            Text(stringResource(R.string.appeal_teacher_response))
                            item.replyTitle?.let { Text(it) }
                            Text(item.reply ?: stringResource(R.string.appeal_reply_missing))
                        }
                    }
                }
            }
        }
    }
}
