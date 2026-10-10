package com.nativelap.ecoguard.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppIconSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.extraColors

private val menuRowVerticalPadding = 12.dp
private val menuRowSpacing = 14.dp

// 오른쪽 화살표(20)와 간격(14)은 판정 폭 밖에 있으므로 기준에서 뺀다.
private val menuChevronAllowance = 34.dp

/** 제목, 선택적 값 텍스트, 오른쪽 화살표로 구성된 메뉴 행. trailingContent로 스위치 등을 대신 둘 수 있다. */
@Composable
fun MenuRow(
    title: String,
    modifier: Modifier = Modifier,
    valueText: String? = null,
    onClick: (() -> Unit)? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = AppComponentSize.minTouchTarget)
                .then(
                    if (onClick != null) {
                        Modifier.clickable(onClick = onClick)
                    } else {
                        Modifier
                    },
                ).padding(
                    horizontal = AppSpacing.md,
                    vertical = menuRowVerticalPadding,
                ),
        horizontalArrangement = Arrangement.spacedBy(menuRowSpacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BoxWithConstraints(modifier = Modifier.weight(1f)) {
            val isCompact =
                valueText != null && isCompactForText(maxWidth, AdaptiveWidth.trailingRow - menuChevronAllowance)

            if (isCompact) {
                Column {
                    MenuTitleText(title = title)

                    MenuValueText(valueText = valueText.orEmpty())
                }
            } else {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(menuRowSpacing),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    MenuTitleText(
                        title = title,
                        modifier = Modifier.weight(1f),
                    )

                    if (valueText != null) {
                        MenuValueText(valueText = valueText)
                    }
                }
            }
        }

        if (trailingContent != null) {
            trailingContent()
        } else {
            Icon(
                painter = painterResource(R.drawable.ic_chevron_right_20),
                contentDescription = null,
                modifier = Modifier.size(AppIconSize.button),
                tint = MaterialTheme.colorScheme.outline,
            )
        }
    }
}

@Composable
private fun MenuTitleText(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        modifier = modifier,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Composable
private fun MenuValueText(valueText: String) {
    Text(
        text = valueText,
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
        color = MaterialTheme.extraColors.captionTextColor,
    )
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun MenuRowPreview() {
    EcoGuardTheme {
        MenuRow(
            title = "내 청소 구역",
            valueText = "본관 2층 복도 A",
            onClick = {},
        )
    }
}
