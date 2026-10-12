package com.nativelap.ecoguard.feature.recruitment.viewmodel

sealed interface RecruitmentScreenEvent {
    data class MotivationChange(
        val motivation: String,
    ) : RecruitmentScreenEvent

    data object ApplyClick : RecruitmentScreenEvent

    data object RetryClick : RecruitmentScreenEvent

    data object HomeClick : RecruitmentScreenEvent

    data object BackClick : RecruitmentScreenEvent
}
