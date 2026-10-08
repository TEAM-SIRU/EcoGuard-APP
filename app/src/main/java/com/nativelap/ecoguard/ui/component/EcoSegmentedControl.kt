package com.nativelap.ecoguard.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppRadius
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.extraColors

private val segmentItemRadius = 10.dp

/** 회색 트랙 안에서 하나를 고르는 세그먼트 컨트롤(예: 층 선택). */
@Composable
fun EcoSegmentedControl(
    segmentLabels: List<String>,
    selectedIndex: Int,
    onSegmentSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(AppRadius.tile))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(AppSpacing.xxs)
                .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.xxs),
    ) {
        segmentLabels.forEachIndexed { segmentIndex, segmentLabel ->
            val isSelected = segmentIndex == selectedIndex

            Box(
                modifier =
                    Modifier
                        .weight(1f)
                        .heightIn(min = AppComponentSize.minTouchTarget)
                        .clip(RoundedCornerShape(segmentItemRadius))
                        .background(
                            if (isSelected) {
                                MaterialTheme.colorScheme.surface
                            } else {
                                Color.Transparent
                            },
                        ).selectable(
                            selected = isSelected,
                            onClick = { onSegmentSelect(segmentIndex) },
                            role = Role.Tab,
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = segmentLabel,
                    style =
                        MaterialTheme.typography.bodyMedium.copy(
                            fontWeight =
                                if (isSelected) {
                                    FontWeight.Bold
                                } else {
                                    FontWeight.Medium
                                },
                        ),
                    color =
                        if (isSelected) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            MaterialTheme.extraColors.captionTextColor
                        },
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun EcoSegmentedControlPreview() {
    EcoGuardTheme {
        EcoSegmentedControl(
            segmentLabels = listOf("1층", "2층", "3층", "4층"),
            selectedIndex = 1,
            onSegmentSelect = {},
            modifier = Modifier.padding(AppSpacing.xl),
        )
    }
}
