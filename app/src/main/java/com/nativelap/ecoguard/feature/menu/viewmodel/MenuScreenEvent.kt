package com.nativelap.ecoguard.feature.menu.viewmodel

import com.nativelap.ecoguard.ui.component.EcoBottomTab

sealed interface MenuScreenEvent {
    data object AreaClick : MenuScreenEvent

    data object ApplicationResultClick : MenuScreenEvent

    data class CleaningNotificationToggle(
        val isEnabled: Boolean,
    ) : MenuScreenEvent

    data object AppealHistoryClick : MenuScreenEvent

    data object NoticeClick : MenuScreenEvent

    data object HelpClick : MenuScreenEvent

    data object LogoutClick : MenuScreenEvent

    data object LogoutConfirm : MenuScreenEvent

    data class TabSelect(
        val tab: EcoBottomTab,
    ) : MenuScreenEvent

    data object CameraClick : MenuScreenEvent
}
