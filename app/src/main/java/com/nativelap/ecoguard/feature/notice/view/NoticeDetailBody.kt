package com.nativelap.ecoguard.feature.notice.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import com.nativelap.ecoguard.feature.notice.viewmodel.NoticeUiModel
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.extraColors

@Composable
fun NoticeDetailBody(
    notice: NoticeUiModel,
    modifier: Modifier = Modifier,
) {
    SelectionContainer {
        Column(modifier, verticalArrangement = Arrangement.spacedBy(AppSpacing.md)) {
            Text(
                notice.title,
                Modifier.semantics { heading() },
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                notice.publishedDate,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.extraColors.captionTextColor,
            )
            Text(notice.body, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}
