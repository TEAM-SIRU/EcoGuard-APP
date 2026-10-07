package com.nativelap.ecoguard.feature.verification.view

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
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationResultScreenEvent
import com.nativelap.ecoguard.ui.component.EcoBackTopBar
import com.nativelap.ecoguard.ui.component.InfoRow
import com.nativelap.ecoguard.ui.component.InfoTableBox
import com.nativelap.ecoguard.ui.component.PageTitle
import com.nativelap.ecoguard.ui.component.PhotoPlaceholder
import com.nativelap.ecoguard.ui.component.StatusChipType
import com.nativelap.ecoguard.ui.component.contentColor
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

private val detailPhotoHeight = 224.dp

/** 08 인증 상세(활동 기록에서 열기). 제출 사진과 상태·구역·제출 시각을 보여준다. */
@Composable
fun VerificationDetailScreen(
    verificationDate: String,
    statusDescription: String,
    status: StatusChipType,
    areaName: String,
    submittedDateTime: String,
    onEvent: (VerificationResultScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            EcoBackTopBar(onBackClick = { onEvent(VerificationResultScreenEvent.BackClick) })
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
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
                PageTitle(
                    title = stringResource(R.string.verification_detail_title, verificationDate),
                    subtitle = statusDescription,
                    modifier = Modifier.padding(
                        top = AppSpacing.xs,
                        bottom = AppSpacing.lg,
                    ),
                )

                PhotoPlaceholder(
                    label = stringResource(R.string.photo_submitted_label),
                    height = detailPhotoHeight,
                    modifier = Modifier.padding(bottom = AppSpacing.xl),
                )

                InfoTableBox {
                    InfoRow(
                        label = stringResource(R.string.photo_status),
                        value = stringResource(status.labelRes),
                        valueColor = status.contentColor(),
                    )

                    InfoRow(
                        label = stringResource(R.string.common_assigned_area),
                        value = areaName,
                    )

                    InfoRow(
                        label = stringResource(R.string.verification_submitted_at),
                        value = submittedDateTime,
                    )
                }
            }
        }
    }
}

@Preview(name = "Verification detail", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun VerificationDetailScreenPreview() {
    EcoGuardTheme {
        VerificationDetailScreen(
            verificationDate = "9월 29일(화)",
            statusDescription = "AI가 사진을 확인하고 있어요",
            status = StatusChipType.REVIEWING,
            areaName = "본관 2층 복도 A",
            submittedDateTime = "9월 29일(화) 08:04",
            onEvent = {},
        )
    }
}
