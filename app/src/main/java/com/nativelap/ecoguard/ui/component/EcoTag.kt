package com.nativelap.ecoguard.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.ui.theme.AppRadius
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

private val tagHorizontalPadding = 6.dp
private val tagVerticalPadding = 2.dp

/** 연한 배경의 작은 상태 태그(예: 모집 중, 신청 완료). */
@Composable
fun EcoTag(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
) {
    Text(
        text = text,
        modifier = modifier
            .clip(RoundedCornerShape(AppRadius.tag))
            .background(containerColor)
            .padding(
                horizontal = tagHorizontalPadding,
                vertical = tagVerticalPadding,
            ),
        style = MaterialTheme.typography.labelSmall,
        color = contentColor,
    )
}

@Preview(showBackground = true)
@Composable
private fun EcoTagPreview() {
    EcoGuardTheme {
        EcoTag(
            text = "모집 중",
            modifier = Modifier.padding(AppSpacing.md),
        )
    }
}
