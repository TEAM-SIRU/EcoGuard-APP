package com.nativelap.ecoguard.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.ui.theme.AppRadius
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.extraColors

/** 연한 회색 카드에 작은 라벨과 큰 수치를 보여주는 통계 카드(예: 승인 7회). */
@Composable
fun StatSummaryCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    EcoCard(
        modifier = modifier,
        cornerRadius = AppRadius.recordCard,
        contentPadding =
            androidx.compose.foundation.layout
                .PaddingValues(AppSpacing.md),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.extraColors.captionTextColor,
        )

        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StatSummaryCardPreview() {
    EcoGuardTheme {
        StatSummaryCard(
            label = "승인",
            value = "7회",
            modifier = Modifier.padding(AppSpacing.md),
        )
    }
}
