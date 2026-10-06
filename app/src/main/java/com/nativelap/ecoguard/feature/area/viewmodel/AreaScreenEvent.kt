package com.nativelap.ecoguard.feature.area.viewmodel

import com.nativelap.ecoguard.ui.component.EcoBottomTab

sealed interface AreaScreenEvent {
    data class FloorSelect(
        val floor: Int,
    ) : AreaScreenEvent

    data object RetryClick : AreaScreenEvent

    data class TabSelect(
        val tab: EcoBottomTab,
    ) : AreaScreenEvent

    data object CameraClick : AreaScreenEvent
}
