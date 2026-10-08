package com.nativelap.ecoguard.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.extraColors

/** 리스트 위 섹션 제목과 선택적 오른쪽 텍스트 액션(예: 전체보기). */
@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onActionClick: () -> Unit = {},
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.xxs),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            modifier =
                Modifier
                    .weight(1f)
                    .semantics { heading() },
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )

        if (actionText != null) {
            Text(
                text = actionText,
                modifier =
                    Modifier
                        .heightIn(min = AppComponentSize.minTouchTarget)
                        .clickable(
                            role = Role.Button,
                            onClick = onActionClick,
                        ).padding(
                            start = AppSpacing.xs,
                            top = AppSpacing.sm,
                            bottom = AppSpacing.sm,
                        ),
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.extraColors.captionTextColor,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun SectionHeaderPreview() {
    EcoGuardTheme {
        SectionHeader(
            title = "최근 청소 기록",
            actionText = "전체보기",
            modifier = Modifier.padding(AppSpacing.xl),
        )
    }
}
