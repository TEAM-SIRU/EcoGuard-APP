package com.nativelap.ecoguard.feature.notice.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.nativelap.ecoguard.feature.notice.viewmodel.NoticeUiModel
import com.nativelap.ecoguard.ui.component.EcoCard
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.extraColors

@Composable
fun NoticeCard(
    notice: NoticeUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    EcoCard(modifier = modifier, contentPadding = PaddingValues(AppSpacing.md), onClick = onClick) {
        Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)) {
            Text(
                notice.title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                notice.publishedDate,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.extraColors.captionTextColor,
            )
        }
    }
}
