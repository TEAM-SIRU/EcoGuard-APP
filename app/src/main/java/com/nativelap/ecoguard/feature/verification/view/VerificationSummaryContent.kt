package com.nativelap.ecoguard.feature.verification.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.ui.component.CenteredIconMessage
import com.nativelap.ecoguard.ui.component.CenteredIconStyle
import com.nativelap.ecoguard.ui.component.InfoTableBox
import com.nativelap.ecoguard.ui.component.PhotoPlaceholder
import com.nativelap.ecoguard.ui.theme.AppSpacing

private val summaryPhotoHeight = 176.dp
private val summaryMessageBottomPadding = 24.dp

/** 제출 완료·인증 승인·선생님 확인 중 화면의 공통 본문(결과 아이콘, 제출 사진, 정보 표). */
@Composable
internal fun VerificationSummaryContent(
    @DrawableRes resultIconRes: Int,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    infoRows: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
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
