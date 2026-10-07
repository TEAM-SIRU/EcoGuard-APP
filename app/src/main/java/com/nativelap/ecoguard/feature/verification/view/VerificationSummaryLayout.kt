package com.nativelap.ecoguard.feature.verification.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.ui.component.BottomCtaBar
import com.nativelap.ecoguard.ui.component.CenteredIconMessage
import com.nativelap.ecoguard.ui.component.CenteredIconStyle
import com.nativelap.ecoguard.ui.component.EcoPrimaryButton
import com.nativelap.ecoguard.ui.component.InfoTableBox
import com.nativelap.ecoguard.ui.component.PhotoPlaceholder
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppSpacing

private val summaryPhotoHeight = 200.dp
private val summaryMessageBottomPadding = 32.dp

/** 제출 완료·인증 승인·선생님 확인 중 화면의 공통 뼈대(결과 아이콘, 제출 사진, 정보 표, 하단 버튼). */
@Composable
internal fun VerificationSummaryLayout(
    @DrawableRes resultIconRes: Int,
    title: String,
    description: String,
    actionText: String,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier,
    infoRows: @Composable ColumnScope.() -> Unit,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            BottomCtaBar {
                EcoPrimaryButton(
                    text = actionText,
                    onClick = onActionClick,
                )
            }
        },
    ) { innerPadding ->
        BoxWithConstraints(
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
                    .heightIn(min = maxHeight)
                    .padding(
                        horizontal = AppSpacing.screenHorizontal,
                        vertical = AppSpacing.xl,
                    ),
                verticalArrangement = Arrangement.Center,
            ) {
                CenteredIconMessage(
                    iconRes = resultIconRes,
                    title = title,
                    description = description,
                    iconStyle = CenteredIconStyle.HERO,
                    modifier = Modifier.padding(bottom = summaryMessageBottomPadding),
                )

                PhotoPlaceholder(
                    label = stringResource(R.string.photo_submitted_label),
                    height = summaryPhotoHeight,
                    modifier = Modifier.padding(bottom = AppSpacing.xl),
                )

                InfoTableBox(content = infoRows)
            }
        }
    }
}
