package com.nativelap.ecoguard.feature.appeal.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.appeal.viewmodel.AppealFormUiState
import com.nativelap.ecoguard.feature.appeal.viewmodel.AppealScreenEvent
import com.nativelap.ecoguard.ui.component.BottomCtaBar
import com.nativelap.ecoguard.ui.component.EcoBackTopBar
import com.nativelap.ecoguard.ui.component.EcoPrimaryButton
import com.nativelap.ecoguard.ui.component.EcoTextArea
import com.nativelap.ecoguard.ui.component.PageTitle
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppRadius
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.extraColors
import com.nativelap.ecoguard.ui.theme.extraTypography

private val reasonBoxVerticalPadding = 14.dp

/** 09 이의신청 작성. 반려 사유 확인, 내용 입력, 선택적 사진 촬영 후 보낸다. */
@Composable
fun AppealFormScreen(
    uiState: AppealFormUiState,
    onEvent: (AppealScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier =
            modifier
                .fillMaxSize()
                .imePadding(),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            EcoBackTopBar(onBackClick = { onEvent(AppealScreenEvent.BackClick) })
        },
        bottomBar = {
            BottomCtaBar {
                EcoPrimaryButton(
                    text = stringResource(R.string.appeal_submit),
                    onClick = { onEvent(AppealScreenEvent.SubmitClick) },
                    enabled = uiState.canSubmit,
                )
            }
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier =
                    Modifier
                        .widthIn(max = AppComponentSize.contentMaxWidth)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(
                            start = AppSpacing.screenHorizontal,
                            end = AppSpacing.screenHorizontal,
                            bottom = AppSpacing.xl,
                        ),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.xl),
            ) {
                PageTitle(
                    title = stringResource(R.string.appeal_title),
                    subtitle = stringResource(R.string.appeal_eligible_reviews),
                    modifier = Modifier.padding(top = AppSpacing.xs),
                )

                AppealFormField(label = stringResource(R.string.appeal_rejection_reason)) {
                    Text(
                        text = uiState.rejectionReason,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(AppRadius.tile))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(
                                    horizontal = AppSpacing.md,
                                    vertical = reasonBoxVerticalPadding,
                                ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                AppealFormField(label = stringResource(R.string.appeal_content)) {
                    EcoTextArea(
                        value = uiState.appealContent,
                        onValueChange = { changedContent -> onEvent(AppealScreenEvent.ContentChange(changedContent)) },
                        placeholder = stringResource(R.string.appeal_content_hint),
                        maxLength = uiState.maxContentLength,
                    )
                }

                AppealFormField(label = stringResource(R.string.appeal_retake_photo_optional)) {
                    AppealPhotoAddTile(
                        attachedPhotoCount = uiState.attachedPhotoCount,
                        maxPhotoCount = uiState.maxPhotoCount,
                        onClick = { onEvent(AppealScreenEvent.AddPhotoClick) },
                    )

                    Text(
                        text =
                            stringResource(
                                R.string.appeal_photo_after_deadline,
                                uiState.verificationDeadlineTime,
                            ),
                        style = MaterialTheme.extraTypography.captionRegular,
                        color = MaterialTheme.extraColors.captionTextColor,
                    )
                }
            }
        }
    }
}

@Composable
private fun AppealFormField(
    label: String,
    fieldContent: @Composable ColumnScope.() -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.extraColors.captionTextColor,
        )

        fieldContent()
    }
}

@Preview(name = "Appeal form · empty", showBackground = true, widthDp = 390, heightDp = 844)
@Preview(
    name = "Appeal form · compact larger text",
    showBackground = true,
    widthDp = 320,
    heightDp = 596,
    fontScale = 1.5f,
)
@Composable
private fun AppealFormScreenPreview() {
    EcoGuardTheme {
        AppealFormScreen(
            uiState = AppealPreviewFixtures.emptyForm,
            onEvent = {},
        )
    }
}

@Preview(name = "Appeal form · filled", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun AppealFormScreenFilledPreview() {
    EcoGuardTheme {
        AppealFormScreen(
            uiState = AppealPreviewFixtures.emptyForm.copy(appealContent = "복도 끝도 청소했는데 사진에서 잘렸어요"),
            onEvent = {},
        )
    }
}
