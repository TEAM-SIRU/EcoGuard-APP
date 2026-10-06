package com.nativelap.ecoguard.feature.activity.viewmodel

import com.nativelap.ecoguard.ui.component.EcoBottomTab

sealed interface ActivityScreenEvent {
    data object MonthSelectorClick : ActivityScreenEvent

    data class MonthApply(
        val year: Int,
        val month: Int,
    ) : ActivityScreenEvent

    data object MonthPickerDismiss : ActivityScreenEvent

    data class RecordClick(
        val recordId: Long,
    ) : ActivityScreenEvent

    data object StartVerificationClick : ActivityScreenEvent

    data object RetryClick : ActivityScreenEvent

    data class TabSelect(
        val tab: EcoBottomTab,
    ) : ActivityScreenEvent

    data object CameraClick : ActivityScreenEvent
}
