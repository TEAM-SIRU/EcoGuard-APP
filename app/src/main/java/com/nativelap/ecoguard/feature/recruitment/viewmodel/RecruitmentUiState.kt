package com.nativelap.ecoguard.feature.recruitment.viewmodel

import androidx.compose.runtime.Immutable

@Immutable
sealed interface RecruitmentUiState {
    data object LoadFailed : RecruitmentUiState

    data class Content(
        val semesterName: String,
        val maxApplicantsPerClass: Int,
        val periodStartDate: String,
        val periodEndDate: String,
        val activityStartTime: String,
        val activityEndTime: String,
        val grade: Int,
        val classNumber: Int,
        val appliedCount: Int,
        val volunteerMinutesPerVerification: Int,
        val applicationStatus: RecruitmentApplicationStatus,
    ) : RecruitmentUiState
}

@Immutable
sealed interface RecruitmentApplicationStatus {
    data class Open(
        val remainingSpots: Int,
    ) : RecruitmentApplicationStatus

    data object Closed : RecruitmentApplicationStatus

    data class AlreadyApplied(
        val appliedDateTime: String,
        val applicationOrder: Int,
    ) : RecruitmentApplicationStatus
}
