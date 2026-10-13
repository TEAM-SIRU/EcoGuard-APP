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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.ui.theme.AppComponentSize
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
    secondaryActionText: String? = null,
    onSecondaryActionClick: () -> Unit = {},
    iconSlotSize: Dp = 48.dp,
    showSheet: Boolean = true,
) {
    if (!showSheet) {
        EcoInfoBottomSheetContent(
            iconRes,
            title,
            description,
            primaryActionText,
            onPrimaryActionClick,
            secondaryActionText,
            onSecondaryActionClick,
            iconSlotSize,
        )
        return
    }
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
        EcoInfoBottomSheetContent(
            iconRes,
            title,
            description,
            primaryActionText,
            onPrimaryActionClick,
            secondaryActionText,
            onSecondaryActionClick,
            iconSlotSize,
        )
    }
}
