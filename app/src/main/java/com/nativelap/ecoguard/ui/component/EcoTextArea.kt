package com.nativelap.ecoguard.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.ui.theme.AppRadius
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.extraColors
import com.nativelap.ecoguard.ui.theme.extraTypography

private val textAreaMinHeight = 140.dp

// 전체 140 − 상하 여백 32 − 글자 수 줄 18 − 간격 8
private val textAreaInputMinHeight = 82.dp
private val textAreaBorderWidth = 1.dp

/** 테두리 있는 여러 줄 입력칸. 아래쪽에 글자 수(입력/최대)를 표시하고 최대 길이를 넘기지 않는다. */
@Composable
fun EcoTextArea(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    maxLength: Int,
    modifier: Modifier = Modifier,
) {
    val textAreaShape = RoundedCornerShape(AppRadius.tile)

    BasicTextField(
        value = value,
        onValueChange = { changedText ->
            onValueChange(changedText.take(maxLength))
        },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = textAreaMinHeight)
            .border(
                border = BorderStroke(
                    width = textAreaBorderWidth,
                    color = MaterialTheme.colorScheme.outlineVariant,
                ),
                shape = textAreaShape,
            ),
        textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        decorationBox = { innerTextField ->
            Column(modifier = Modifier.padding(AppSpacing.md)) {
                // 입력 길이에 따라 늘어나되, 비어 있을 때도 Figma 높이(140)를 유지한다.
                Box(modifier = Modifier.heightIn(min = textAreaInputMinHeight)) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.extraColors.captionTextColor,
                        )
                    }

                    innerTextField()
                }

                Text(
                    text = stringResource(R.string.format_text_count, value.length, maxLength),
                    modifier = Modifier.padding(top = AppSpacing.xs),
                    style = MaterialTheme.extraTypography.captionRegular,
                    color = MaterialTheme.extraColors.captionTextColor,
                )
            }
        },
    )
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun EcoTextAreaPreview() {
    EcoGuardTheme {
        EcoTextArea(
            value = "",
            onValueChange = {},
            placeholder = "예) 복도 끝도 청소했는데 사진에서 잘렸어요",
            maxLength = 300,
            modifier = Modifier.padding(AppSpacing.xl),
        )
    }
}
