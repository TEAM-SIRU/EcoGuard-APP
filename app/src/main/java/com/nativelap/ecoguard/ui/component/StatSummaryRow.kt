package com.nativelap.ecoguard.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

@Immutable
data class StatSummaryItem(
    val label: String,
    val value: String,
)

/** 통계 카드를 같은 폭으로 나란히 둔다. 폭이 좁거나 글자가 크면 세로로 쌓는다. */
@Composable
fun StatSummaryRow(
    statItems: List<StatSummaryItem>,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val requiredWidth = AdaptiveWidth.statItem * statItems.size

        if (isCompactForText(maxWidth, requiredWidth)) {
            Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)) {
                statItems.forEach { statItem ->
                    StatSummaryCard(
                        label = statItem.label,
                        value = statItem.value,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs)) {
                statItems.forEach { statItem ->
                    StatSummaryCard(
                        label = statItem.label,
                        value = statItem.value,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390)
@Preview(name = "Compact larger text", showBackground = true, widthDp = 320, fontScale = 2f)
@Composable
private fun StatSummaryRowPreview() {
    EcoGuardTheme {
        StatSummaryRow(
            statItems =
                listOf(
                    StatSummaryItem(label = "승인", value = "7회"),
                    StatSummaryItem(label = "반려", value = "1회"),
                    StatSummaryItem(label = "미제출", value = "1회"),
                ),
            modifier = Modifier.padding(AppSpacing.xl),
        )
    }
}
