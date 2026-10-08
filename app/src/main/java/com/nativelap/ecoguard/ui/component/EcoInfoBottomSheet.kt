package com.nativelap.ecoguard.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppIconSize
import com.nativelap.ecoguard.ui.theme.AppRadius
import com.nativelap.ecoguard.ui.theme.AppSpacing

private const val BOTTOM_SHEET_SCRIM_ALPHA = 0.4f
private val bottomSheetHandleWidth = 36.dp
private val bottomSheetHandleHeight = 4.dp
private val bottomSheetHandleTopPadding = 10.dp

/** 아이콘·제목·설명과 버튼 1~2개로 구성된 안내 바텀시트(인증 불가, 권한 요청 등). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcoInfoBottomSheet(
    @DrawableRes iconRes: Int,
    title: String,
    description: String,
    primaryActionText: String,
    onPrimaryActionClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    iconTint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    secondaryActionText: String? = null,
    onSecondaryActionClick: () -> Unit = {},
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        sheetMaxWidth = AppComponentSize.contentMaxWidth,
        shape =
            RoundedCornerShape(
                topStart = AppRadius.logo,
                topEnd = AppRadius.logo,
            ),
        containerColor = MaterialTheme.colorScheme.surface,
        scrimColor = MaterialTheme.colorScheme.scrim.copy(alpha = BOTTOM_SHEET_SCRIM_ALPHA),
        dragHandle = {
            Box(
                modifier =
                    Modifier
                        .padding(top = bottomSheetHandleTopPadding)
                        .width(bottomSheetHandleWidth)
                        .height(bottomSheetHandleHeight)
                        .clip(RoundedCornerShape(AppRadius.progressBar))
                        .background(MaterialTheme.colorScheme.outlineVariant),
            )
        },
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        start = AppSpacing.screenHorizontal,
                        end = AppSpacing.screenHorizontal,
                        top = AppSpacing.xl,
                        bottom = AppSpacing.md,
                    ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(AppComponentSize.emptyIconCircle)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(AppIconSize.emptyState),
                    tint = iconTint,
                )
            }

            Spacer(modifier = Modifier.height(AppSpacing.md))

            Text(
                text = title,
                modifier = Modifier.semantics { heading() },
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(AppSpacing.xs))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(AppSpacing.xl))

            Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)) {
                EcoPrimaryButton(
                    text = primaryActionText,
                    onClick = onPrimaryActionClick,
                )

                if (secondaryActionText != null) {
                    EcoSecondaryButton(
                        text = secondaryActionText,
                        onClick = onSecondaryActionClick,
                    )
                }
            }
        }
    }
}
