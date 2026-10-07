package com.nativelap.ecoguard.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
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
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.lg),
    ) {
        Text(
            text = title,
            modifier = Modifier.semantics { heading() },
            style = MaterialTheme.extraTypography.statusTitle,
            color = MaterialTheme.colorScheme.onSurface,
        )

        if (highlightText != null) {
            Text(
                text = highlightText,
                style = MaterialTheme.extraTypography.highlightNumber,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        Text(
            text = description,
            style = MaterialTheme.extraTypography.statusBody,
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
