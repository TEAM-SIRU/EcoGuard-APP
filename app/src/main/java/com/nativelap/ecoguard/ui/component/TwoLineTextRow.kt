package com.nativelap.ecoguard.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.ui.theme.AppIconSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.extraColors

private val twoLineRowVerticalPadding = 14.dp
private val twoLineRowTextSpacing = 2.dp
private val twoLineRowIconSpacing = 14.dp

/** 본문 17·보조 14 두 줄 리스트 행. 선택적으로 앞에 22dp 강조색 아이콘을 둔다. */
@Composable
fun TwoLineTextRow(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    @DrawableRes leadingIconRes: Int? = null,
    trailingContent: @Composable RowScope.() -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = AppSpacing.screenHorizontal,
                vertical = twoLineRowVerticalPadding,
            ),
        horizontalArrangement = Arrangement.spacedBy(twoLineRowIconSpacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (leadingIconRes != null) {
            Icon(
                painter = painterResource(leadingIconRes),
                contentDescription = null,
                modifier = Modifier.size(AppIconSize.listTile),
                tint = MaterialTheme.colorScheme.primary,
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(twoLineRowTextSpacing),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.extraColors.captionTextColor,
            )
        }

        trailingContent()
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun TwoLineTextRowPreview() {
    EcoGuardTheme {
        TwoLineTextRow(
            title = "인증 1번에 봉사시간 10분",
            subtitle = "인증이 승인되면 활동 기록에 쌓여요",
        )
    }
}
