package com.nativelap.ecoguard.feature.notification.viewmodel

sealed interface NotificationTarget {
    data object Recruitment : NotificationTarget

    data object ApplicationResult : NotificationTarget

    data class Notice(
        val noticeId: Long,
    ) : NotificationTarget

    data class Verification(
        val recordId: Long,
    ) : NotificationTarget

    data class Appeal(
        val appealId: Long,
    ) : NotificationTarget
}
