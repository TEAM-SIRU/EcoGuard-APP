package com.nativelap.ecoguard.feature.recruitment.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.ui.theme.AppRadius
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.extraColors
import com.nativelap.ecoguard.ui.theme.extraTypography

@Composable
fun ApplicationIdentityField(
    label: String,
    text: String,
    helper: String? = null,
) {
    Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.extraColors.captionTextColor)
        Text(
            text = text,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(AppRadius.input))
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(AppRadius.input))
                    .padding(horizontal = AppSpacing.md, vertical = 14.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (helper != null) {
            Text(
                helper,
                style = MaterialTheme.extraTypography.captionRegular,
                color = MaterialTheme.extraColors.captionTextColor,
            )
        }
    }
}
