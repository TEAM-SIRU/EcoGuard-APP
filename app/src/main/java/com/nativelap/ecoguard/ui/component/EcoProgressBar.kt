package com.nativelap.ecoguard.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppRadius
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

/** 8dp 높이의 둥근 진행 막대(예: 반별 신청 현황). */
@Composable
fun EcoProgressBar(
    currentCount: Int,
    maxCount: Int,
    modifier: Modifier = Modifier,
    progressColor: Color = MaterialTheme.colorScheme.primary,
) {
    val progressFraction = if (maxCount > 0) {
        (currentCount.toFloat() / maxCount).coerceIn(0f, 1f)
    } else {
        0f
    }
    val barShape = RoundedCornerShape(AppRadius.progressBar)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(AppComponentSize.progressBarHeight)
            .clip(barShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .semantics {
                progressBarRangeInfo = ProgressBarRangeInfo(
                    current = progressFraction,
                    range = 0f..1f,
                )
            },
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progressFraction)
                .fillMaxHeight()
                .clip(barShape)
                .background(progressColor),
        )
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun EcoProgressBarPreview() {
    EcoGuardTheme {
        EcoProgressBar(
            currentCount = 4,
            maxCount = 6,
            modifier = Modifier.padding(AppSpacing.xl),
        )
    }
}
