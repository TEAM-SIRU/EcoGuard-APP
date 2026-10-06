package com.nativelap.ecoguard.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.ui.theme.AppIconSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.extraColors

/** 색맹 대응을 위해 색·아이콘·텍스트를 함께 표시하는 인증 상태 칩. */
@Composable
fun StatusChip(
    type: StatusChipType,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.labelSmall,
) {
    val contentColor = statusChipContentColor(type)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.xxs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(type.iconRes),
            contentDescription = null,
            modifier = Modifier.size(AppIconSize.chip),
            tint = contentColor,
        )

        Text(
            text = stringResource(type.labelRes),
            style = textStyle,
            color = contentColor,
        )
    }
}

@Composable
private fun statusChipContentColor(type: StatusChipType): Color {
    return when (type) {
        StatusChipType.APPROVED -> MaterialTheme.colorScheme.primary
        StatusChipType.REVIEWING -> MaterialTheme.extraColors.warningTextColor
        StatusChipType.REJECTED -> MaterialTheme.colorScheme.error
        StatusChipType.NOT_SUBMITTED -> MaterialTheme.colorScheme.onSurfaceVariant
    }
}

@Preview(showBackground = true)
@Composable
private fun StatusChipPreview() {
    EcoGuardTheme {
        Column(
            modifier = Modifier.padding(AppSpacing.md),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.xs),
        ) {
            StatusChipType.entries.forEach { chipType ->
                StatusChip(type = chipType)
            }
        }
    }
}
