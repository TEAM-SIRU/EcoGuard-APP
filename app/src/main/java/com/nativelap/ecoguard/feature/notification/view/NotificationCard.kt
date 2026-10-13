package com.nativelap.ecoguard.feature.notification.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.notification.viewmodel.NotificationUiModel
import com.nativelap.ecoguard.ui.component.EcoCard
import com.nativelap.ecoguard.ui.theme.AppRadius
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.extraColors

@Composable
fun NotificationCard(
    notification: NotificationUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val readState =
        stringResource(if (notification.isRead) R.string.notification_read else R.string.notification_unread)
    EcoCard(
        modifier = modifier.semantics { stateDescription = readState },
        cornerRadius = AppRadius.listCard,
        containerColor =
            if (notification.isRead) {
                MaterialTheme.colorScheme.surfaceContainerHighest
            } else {
                MaterialTheme.colorScheme.surface
            },
        contentPadding = PaddingValues(horizontal = AppSpacing.md, vertical = AppSpacing.md),
        onClick = onClick,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.xxs / 2)) {
            Text(
                notification.title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                notification.sentDate,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.extraColors.captionTextColor,
            )
        }
    }
}
