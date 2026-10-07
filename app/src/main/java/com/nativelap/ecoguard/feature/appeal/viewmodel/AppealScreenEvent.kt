package com.nativelap.ecoguard.feature.appeal.viewmodel

sealed interface AppealScreenEvent {
    data class ContentChange(
        val appealContent: String,
    ) : AppealScreenEvent

    data object AddPhotoClick : AppealScreenEvent

    data object SubmitClick : AppealScreenEvent

    data object RetrySendClick : AppealScreenEvent

    data object EditContentClick : AppealScreenEvent

    data object ResubmitClick : AppealScreenEvent

    data object ActivityRecordClick : AppealScreenEvent

    data class HistoryItemClick(
        val appealId: Long,
    ) : AppealScreenEvent

    data object HomeClick : AppealScreenEvent

    data object BackClick : AppealScreenEvent
}
