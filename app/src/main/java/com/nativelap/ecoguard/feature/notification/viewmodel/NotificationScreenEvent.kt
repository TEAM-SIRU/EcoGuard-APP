package com.nativelap.ecoguard.feature.notification.viewmodel

import com.nativelap.ecoguard.ui.component.EcoBottomTab

sealed interface NotificationScreenEvent {
    data object BackClick : NotificationScreenEvent

    data object RetryClick : NotificationScreenEvent

    data object CameraClick : NotificationScreenEvent

    data class TabSelect(
        val tab: EcoBottomTab,
    ) : NotificationScreenEvent

    data class NotificationClick(
        val notificationId: Long,
    ) : NotificationScreenEvent
}
