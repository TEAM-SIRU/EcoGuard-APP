package com.nativelap.ecoguard.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppIconSize
import com.nativelap.ecoguard.ui.theme.extraColors

private val cameraActionShadowRadius = 14.dp
private val cameraActionShadowOffsetY = 6.dp
private const val CAMERA_ACTION_SHADOW_ALPHA = 0.32f

@Composable
internal fun EcoCameraAction(
    onClick: () -> Unit,
    isEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val fabContainerColor =
        if (isEnabled) {
            MaterialTheme.colorScheme.secondary
        } else {
            MaterialTheme.extraColors.disabledContentColor
        }
    val fabShadowAlpha = if (isEnabled) CAMERA_ACTION_SHADOW_ALPHA else 0f

    Surface(
        onClick = onClick,
        modifier =
            modifier
                .size(AppComponentSize.cameraFab)
                .dropShadow(
                    shape = CircleShape,
                    shadow =
                        Shadow(
                            radius = cameraActionShadowRadius,
                            color = MaterialTheme.colorScheme.secondary,
                            offset = DpOffset(x = 0.dp, y = cameraActionShadowOffsetY),
                            alpha = fabShadowAlpha,
                        ),
                ),
        enabled = isEnabled,
        shape = CircleShape,
        color = fabContainerColor,
        contentColor = MaterialTheme.colorScheme.onSecondary,
        border =
            BorderStroke(
                width = AppComponentSize.cameraFabBorder,
                color = MaterialTheme.colorScheme.surface,
            ),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(R.drawable.ic_cam_26),
                contentDescription = stringResource(R.string.cd_camera_verification),
                modifier = Modifier.size(AppIconSize.fab),
            )
        }
    }
}
