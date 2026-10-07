package com.nativelap.ecoguard.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.extraColors

private val compactInfoSpacing = 2.dp

/** 카드 안 정보 표의 한 줄. 왼쪽 라벨, 오른쪽 값. 좁거나 글자가 크면 라벨 아래에 값을 둔다. */
@Composable
fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        if (isCompactForText(maxWidth, AdaptiveWidth.infoRow)) {
            Column(verticalArrangement = Arrangement.spacedBy(compactInfoSpacing)) {
                InfoLabelText(label = label)

                InfoValueText(
                    value = value,
                    valueColor = valueColor,
                    textAlign = TextAlign.Start,
                )
            }
        } else {
            Row(
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.md),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                InfoLabelText(label = label)

                InfoValueText(
                    value = value,
                    valueColor = valueColor,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun InfoLabelText(label: String) {
    Text(
        text = label,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.extraColors.captionTextColor,
    )
}

@Composable
private fun InfoValueText(
    value: String,
    valueColor: Color,
    textAlign: TextAlign,
    modifier: Modifier = Modifier,
) {
    Text(
        text = value,
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
        color = valueColor,
        textAlign = textAlign,
    )
}

@Preview(showBackground = true, widthDp = 390)
@Preview(name = "Compact larger text", showBackground = true, widthDp = 320, fontScale = 2f)
@Composable
private fun InfoRowPreview() {
    EcoGuardTheme {
        InfoRow(
            label = "청소 시간",
            value = "08:00 – 08:10",
            modifier = Modifier.padding(AppSpacing.xl),
        )
    }
}
