package com.nativelap.ecoguard.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.extraColors

private val twoLineRowVerticalPadding = 14.dp
private val twoLineRowTextSpacing = 2.dp

/** 아이콘 없이 본문 17·보조 14 두 줄로 구성된 리스트 행. */
@Composable
fun TwoLineTextRow(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = AppSpacing.screenHorizontal,
                vertical = twoLineRowVerticalPadding,
            ),
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
