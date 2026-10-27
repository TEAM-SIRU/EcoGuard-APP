package com.nativelap.ecoguard.feature.home.viewmodel

import com.nativelap.ecoguard.ui.component.EcoBottomTab

sealed interface HomeScreenEvent {
    data object NotificationClick : HomeScreenEvent

    data object NoticeCloseClick : HomeScreenEvent

    data object NoticeDetailClick : HomeScreenEvent

    data object CleaningVerificationClick : HomeScreenEvent

    data object SubmittedPhotoClick : HomeScreenEvent

    data object ActivityRecordClick : HomeScreenEvent

    data class AppealClick(
        val verificationId: Long?,
        val rejectionReason: String,
        val verificationDeadlineTime: String,
    ) : HomeScreenEvent

    data object RecruitmentClick : HomeScreenEvent

    data object ApplicationResultClick : HomeScreenEvent

    data object AllRecordsClick : HomeScreenEvent

    data class RecordClick(
        val recordId: Long,
    ) : HomeScreenEvent

    data class TabSelect(
        val tab: EcoBottomTab,
    ) : HomeScreenEvent

    data object RetryClick : HomeScreenEvent

    data object BackClick : HomeScreenEvent

    data object HomeClick : HomeScreenEvent

    data object NoticeListClick : HomeScreenEvent
}
