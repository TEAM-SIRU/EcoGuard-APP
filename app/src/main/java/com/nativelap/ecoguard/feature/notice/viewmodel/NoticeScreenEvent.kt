package com.nativelap.ecoguard.feature.notice.viewmodel

sealed interface NoticeScreenEvent {
    data object BackClick : NoticeScreenEvent

    data object RetryClick : NoticeScreenEvent

    data object ReturnToListClick : NoticeScreenEvent

    data class NoticeClick(
        val noticeId: Long,
    ) : NoticeScreenEvent
}
