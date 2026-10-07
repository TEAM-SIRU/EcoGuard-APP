package com.nativelap.ecoguard.ui.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.ui.theme.extraColors

enum class StatusChipType(
    @param:StringRes val labelRes: Int,
    @param:DrawableRes val iconRes: Int,
) {
    APPROVED(
        labelRes = R.string.verification_status_approved,
        iconRes = R.drawable.ic_check,
    ),
    REVIEWING(
        labelRes = R.string.verification_status_reviewing,
        iconRes = R.drawable.ic_clock,
    ),
    REJECTED(
        labelRes = R.string.verification_status_rejected,
        iconRes = R.drawable.ic_alert,
    ),
    NOT_SUBMITTED(
        labelRes = R.string.verification_status_not_submitted,
        iconRes = R.drawable.ic_circle,
    ),
}

/** 인증 상태별 강조색. 칩·정보 표 등 상태를 색으로 표현하는 곳에서 같은 기준을 쓴다. */
@Composable
@ReadOnlyComposable
fun StatusChipType.contentColor(): Color {
    return when (this) {
        StatusChipType.APPROVED -> MaterialTheme.colorScheme.primary
        StatusChipType.REVIEWING -> MaterialTheme.extraColors.warningTextColor
        StatusChipType.REJECTED -> MaterialTheme.colorScheme.error
        StatusChipType.NOT_SUBMITTED -> MaterialTheme.colorScheme.onSurfaceVariant
    }
}
