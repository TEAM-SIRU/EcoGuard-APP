package com.nativelap.ecoguard.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.ui.theme.AppIconSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.extraTypography

/** 조회 실패·활동 제외 같은 상태 안내 화면의 큰 제목과 설명. */
@Composable
fun StatusMessage(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    highlightText: String? = null,
    iconRes: Int = R.drawable.ic_alert_64,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.xs),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painterResource(iconRes),
            null,
            modifier = Modifier.padding(bottom = AppSpacing.md).size(AppIconSize.hero),
            tint = Color.Unspecified,
        )
        Text(
            text = title,
            modifier = Modifier.semantics { heading() },
            style = MaterialTheme.extraTypography.statusTitle,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
        )

        if (highlightText != null) {
            Text(
                text = highlightText,
                style = MaterialTheme.extraTypography.highlightNumber,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }

        Text(
            text = description,
            style = MaterialTheme.extraTypography.statusBody,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun StatusMessagePreview() {
    EcoGuardTheme {
        StatusMessage(
            title = "홈을 불러오지 못했어요",
            description = "네트워크 연결을 확인한 뒤 다시 시도해 주세요.",
            modifier = Modifier.padding(AppSpacing.xl),
        )
    }
}
