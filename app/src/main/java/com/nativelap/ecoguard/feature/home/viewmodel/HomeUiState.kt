package com.nativelap.ecoguard.feature.home.viewmodel

import androidx.compose.runtime.Immutable

@Immutable
sealed interface HomeUiState {
    data object Loading : HomeUiState

    data object LoadFailed : HomeUiState

    data class RemovedFromActivity(
        val removalReason: String,
    ) : HomeUiState

    /** 하단 탭과 함께 카드 목록을 보여주는 홈 상태. */
    data class Content(
        val notice: HomeNoticeUiModel?,
        val section: HomeSectionUiModel,
    ) : HomeUiState {
        /** 청소 구역이 배정된 환경지킴이만 하단 카메라로 인증할 수 있다. */
        val isCameraVerificationAvailable: Boolean
            get() = section is HomeSectionUiModel.Cleaning
    }
}

@Immutable
data class HomeNoticeUiModel(
    val publishedDate: String,
    val headline: String,
    val body: String,
    val isNew: Boolean,
    val emphasizedPhrases: List<String> = emptyList(),
)

@Immutable
sealed interface HomeSectionUiModel {
    /** 아직 환경지킴이가 아닌 학생에게 보여주는 모집 카드. */
    data class Recruiting(
        val semesterName: String,
        val maxApplicantsPerClass: Int,
        val grade: Int,
        val classNumber: Int,
        val appliedCount: Int,
    ) : HomeSectionUiModel

    data object WaitingAssignment : HomeSectionUiModel

    data class Cleaning(
        val todayCleaning: TodayCleaningUiModel,
        val weekCleaning: WeekCleaningUiModel,
        val recentRecords: List<CleaningRecordUiModel>,
    ) : HomeSectionUiModel
}
