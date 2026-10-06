package com.nativelap.ecoguard.feature.appeal.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.ui.theme.AppIconSize
import com.nativelap.ecoguard.ui.theme.AppRadius
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.extraColors

private val photoAddTileSize = 88.dp

/** 이의신청용 사진 촬영 타일. 현재 촬영 수/최대 수를 표시한다. */
@Composable
fun AppealPhotoAddTile(
    attachedPhotoCount: Int,
    maxPhotoCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .size(photoAddTileSize)
            .clip(RoundedCornerShape(AppRadius.tile))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable(
                role = Role.Button,
                onClick = onClick,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = AppSpacing.xxs,
            alignment = Alignment.CenterVertically,
        ),
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_cam_24),
            contentDescription = null,
            modifier = Modifier.size(AppIconSize.standard),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Text(
            text = stringResource(R.string.format_photo_progress, attachedPhotoCount, maxPhotoCount),
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.extraColors.captionTextColor,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AppealPhotoAddTilePreview() {
    EcoGuardTheme {
        AppealPhotoAddTile(
            attachedPhotoCount = 0,
            maxPhotoCount = 3,
            onClick = {},
            modifier = Modifier.padding(AppSpacing.md),
        )
    }
}
