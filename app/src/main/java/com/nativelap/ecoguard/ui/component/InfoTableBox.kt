package com.nativelap.ecoguard.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.ui.theme.AppRadius
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

/** 연한 회색 배경 안에 InfoRow를 12dp 간격으로 쌓는 정보 표. */
@Composable
fun InfoTableBox(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(AppRadius.button))
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                .padding(AppSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.sm),
        content = content,
    )
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun InfoTableBoxPreview() {
    EcoGuardTheme {
        InfoTableBox(modifier = Modifier.padding(AppSpacing.xl)) {
            InfoRow(
                label = "담당 구역",
                value = "본관 2층 복도 A",
            )

            InfoRow(
                label = "제출 시각",
                value = "오늘 08:04",
            )
        }
    }
}
