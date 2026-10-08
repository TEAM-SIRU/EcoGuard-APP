package com.nativelap.ecoguard.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import com.nativelap.ecoguard.ui.theme.AppSpacing

/** 제목·설명과 취소/확인 버튼을 가진 확인 다이얼로그. isDestructive이면 확인 글자를 빨간색으로 표시한다. */
@Composable
fun EcoConfirmDialog(
    title: String,
    description: String,
    dismissText: String,
    confirmText: String,
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
    isDestructive: Boolean = false,
) {
    EcoDialogSurface(onDismissRequest = onDismissRequest) {
        Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)) {
            Text(
                text = title,
                modifier = Modifier.semantics { heading() },
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        val confirmContainerColor =
            if (isDestructive) {
                MaterialTheme.colorScheme.surfaceVariant
            } else {
                MaterialTheme.colorScheme.primary
            }
        val confirmContentColor =
            if (isDestructive) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.onPrimary
            }

        AdaptiveButtonRow(
            firstButton = { itemModifier ->
                EcoDialogButton(
                    text = dismissText,
                    onClick = onDismissRequest,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    modifier = itemModifier,
                )
            },
            secondButton = { itemModifier ->
                EcoDialogButton(
                    text = confirmText,
                    onClick = onConfirmClick,
                    containerColor = confirmContainerColor,
                    contentColor = confirmContentColor,
                    modifier = itemModifier,
                )
            },
        )
    }
}
