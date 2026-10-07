package com.nativelap.ecoguard.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

/**
 * 버튼 두 개를 같은 폭으로 나란히 둔다. 폭이 좁거나 글자가 크면 위아래로 쌓는다.
 * 각 버튼에는 전달받은 itemModifier를 그대로 적용해야 같은 폭으로 배치된다.
 */
@Composable
fun AdaptiveButtonRow(
    modifier: Modifier = Modifier,
    firstButton: @Composable (itemModifier: Modifier) -> Unit,
    secondButton: @Composable (itemModifier: Modifier) -> Unit,
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        if (isCompactForText(maxWidth, AdaptiveWidth.buttonRow)) {
            Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)) {
                firstButton(Modifier.fillMaxWidth())

                secondButton(Modifier.fillMaxWidth())
            }
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs)) {
                firstButton(Modifier.weight(1f))

                secondButton(Modifier.weight(1f))
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390)
@Preview(name = "Compact larger text", showBackground = true, widthDp = 320, fontScale = 2f)
@Composable
private fun AdaptiveButtonRowPreview() {
    EcoGuardTheme {
        AdaptiveButtonRow(
            modifier = Modifier.padding(AppSpacing.xl),
            firstButton = { itemModifier ->
                EcoSecondaryButton(
                    text = "다시 찍기",
                    onClick = {},
                    modifier = itemModifier,
                )
            },
            secondButton = { itemModifier ->
                EcoPrimaryButton(
                    text = "보내기",
                    onClick = {},
                    modifier = itemModifier,
                )
            },
        )
    }
}
