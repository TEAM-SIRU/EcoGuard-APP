package com.nativelap.ecoguard.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.extraColors
import com.nativelap.ecoguard.ui.theme.extraTypography

private val rejectionHeroTopPadding = 32.dp
private val rejectionHeroBottomPadding = 24.dp
private val rejectionPhotoHeight = 112.dp

/**
 * 인증 반려·이의신청 반려 결과 화면의 공통 본문. Scaffold는 각 Screen이 직접 배치한다.
 * 반려 아이콘과 요약, 피드백 박스, 제출 사진, 이의신청 버튼·안내·홈 버튼을 위에서부터 배치한다.
 */
@Composable
fun RejectionResultContent(
    innerPadding: PaddingValues,
    title: String,
    summary: String,
    feedbackLabel: String,
    feedbackHeadline: String,
    feedbackBody: String,
    primaryActionText: String,
    actionCaption: String,
    onPrimaryActionClick: () -> Unit,
    onHomeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)
            .consumeWindowInsets(innerPadding),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = AppComponentSize.contentMaxWidth)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(
                    start = AppSpacing.screenHorizontal,
                    end = AppSpacing.screenHorizontal,
                    bottom = AppSpacing.xl,
                ),
        ) {
            CenteredIconMessage(
                iconRes = R.drawable.ic_alert_64,
                title = title,
                description = summary,
                iconStyle = CenteredIconStyle.HERO,
                modifier = Modifier.padding(
                    top = rejectionHeroTopPadding,
                    bottom = rejectionHeroBottomPadding,
                ),
            )

            ReviewFeedbackCard(
                label = feedbackLabel,
                headline = feedbackHeadline,
                body = feedbackBody,
            )

            PhotoPlaceholder(
                label = stringResource(R.string.photo_submitted_label),
                height = rejectionPhotoHeight,
                modifier = Modifier.padding(vertical = AppSpacing.md),
            )

            Column(
                modifier = Modifier.padding(top = AppSpacing.lg),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(AppSpacing.xs),
            ) {
                EcoPrimaryButton(
                    text = primaryActionText,
                    onClick = onPrimaryActionClick,
                )

                Text(
                    text = actionCaption,
                    style = MaterialTheme.extraTypography.captionRegular,
                    color = MaterialTheme.extraColors.captionTextColor,
                    textAlign = TextAlign.Center,
                )

                EcoSecondaryButton(
                    text = stringResource(R.string.action_home),
                    onClick = onHomeClick,
                    minHeight = AppComponentSize.minTouchTarget,
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun RejectionResultContentPreview() {
    EcoGuardTheme {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface,
            topBar = {
                EcoBackTopBar(onBackClick = {})
            },
        ) { innerPadding ->
            RejectionResultContent(
                innerPadding = innerPadding,
                title = "인증이 반려됐어요",
                summary = "본관 2층 복도 A · 오늘 08:04 제출",
                feedbackLabel = "AI 검수 결과",
                feedbackHeadline = "사진에 청소 구역이 잘 보이지 않아요",
                feedbackBody = "복도 끝까지 보이도록 조금 뒤에서 찍으면 돼요",
                primaryActionText = "이의신청하기",
                actionCaption = "판정이 맞지 않다면 선생님이 직접 확인해요",
                onPrimaryActionClick = {},
                onHomeClick = {},
            )
        }
    }
}
