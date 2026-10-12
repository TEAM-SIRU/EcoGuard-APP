package com.nativelap.ecoguard.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.ui.theme.AppRadius
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.extraColors

private val feedbackTextSpacing = 6.dp

/** AI 검수 결과·선생님 답변처럼 라벨, 핵심 사유, 안내로 구성된 흰색 카드. */
@Composable
fun ReviewFeedbackCard(
    label: String,
    headline: String,
    body: String,
    modifier: Modifier = Modifier,
) {
    EcoCard(
        modifier = modifier,
        cornerRadius = AppRadius.recordCard,
        contentPadding =
            androidx.compose.foundation.layout
                .PaddingValues(AppSpacing.md),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(feedbackTextSpacing)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.extraColors.captionTextColor,
            )

            Text(
                text = headline,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun ReviewFeedbackCardPreview() {
    EcoGuardTheme {
        ReviewFeedbackCard(
            label = "AI 검수 결과",
            headline = "사진에 청소 구역이 잘 보이지 않아요",
            body = "복도 끝까지 보이도록 조금 뒤에서 찍으면 돼요",
            modifier = Modifier.padding(AppSpacing.xl),
        )
    }
}
