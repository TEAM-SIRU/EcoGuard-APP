package com.nativelap.ecoguard.ui.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.nativelap.ecoguard.R

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
