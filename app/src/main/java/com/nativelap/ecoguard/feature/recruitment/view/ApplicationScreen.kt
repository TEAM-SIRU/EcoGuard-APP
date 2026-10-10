package com.nativelap.ecoguard.feature.recruitment.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.recruitment.viewmodel.ApplicationUiState
import com.nativelap.ecoguard.feature.recruitment.viewmodel.RecruitmentScreenEvent
import com.nativelap.ecoguard.ui.component.BottomCtaBar
import com.nativelap.ecoguard.ui.component.EcoBackTopBar
import com.nativelap.ecoguard.ui.component.EcoPrimaryButton
import com.nativelap.ecoguard.ui.component.PageTitle
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

@Composable
fun ApplicationScreen(
    uiState: ApplicationUiState,
    onEvent: (RecruitmentScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { EcoBackTopBar(onBackClick = { onEvent(RecruitmentScreenEvent.BackClick) }) },
        bottomBar = {
            BottomCtaBar {
                EcoPrimaryButton(
                    text = stringResource(R.string.recruitment_apply),
                    onClick = { onEvent(RecruitmentScreenEvent.ApplyClick) },
                    enabled = uiState.canApply,
                    isLoading = uiState.isApplying,
                )
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding).consumeWindowInsets(innerPadding),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier =
                    Modifier
                        .widthIn(max = AppComponentSize.contentMaxWidth)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = AppSpacing.screenHorizontal, vertical = AppSpacing.xs),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.xl),
            ) {
                PageTitle(
                    title = stringResource(R.string.application_title),
                    subtitle = stringResource(R.string.application_description, uiState.maxApplicantsPerClass),
                )
                ApplicationFormContent(uiState = uiState, onEvent = onEvent)
            }
        }
    }
}

@Preview(name = "Application · empty · 823:2880", showBackground = true, widthDp = 390, heightDp = 844)
@Preview(name = "Application · large text", showBackground = true, widthDp = 320, heightDp = 844, fontScale = 1.5f)
@Composable
private fun ApplicationScreenPreview() {
    EcoGuardTheme { ApplicationScreen(ApplicationUiState(2, 3, 5, "최민준"), {}) }
}

@Preview(name = "Application · filled · 823:4643", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun ApplicationFilledPreview() {
    EcoGuardTheme {
        ApplicationScreen(ApplicationUiState(2, 3, 5, "최민준", "우리 반 교실과 복도를 깨끗하게 지키고 싶어요.\n아침마다 일찍 와서 꾸준히 해 볼게요."), {})
    }
}

@Preview(name = "Application · applying", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun ApplicationApplyingPreview() {
    EcoGuardTheme { ApplicationScreen(ApplicationUiState(2, 3, 5, "최민준", "깨끗한 학교를 만들고 싶어요", isApplying = true), {}) }
}
