package com.nativelap.ecoguard.feature.demo.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.nativelap.ecoguard.feature.activity.viewmodel.ActivityUiState
import com.nativelap.ecoguard.feature.activity.viewmodel.ActivityWeekGroupUiModel
import com.nativelap.ecoguard.feature.home.viewmodel.CleaningRecordUiModel
import com.nativelap.ecoguard.feature.home.viewmodel.HomeSectionUiModel
import com.nativelap.ecoguard.feature.home.viewmodel.HomeUiState
import com.nativelap.ecoguard.feature.home.viewmodel.TodayCleaningStatus
import com.nativelap.ecoguard.feature.recruitment.viewmodel.RecruitmentApplicationStatus
import com.nativelap.ecoguard.ui.component.StatusChipType

// 샘플 데이터만 저장한다. 실제 인증·제출 성공 판정은 서버 기능에서 교체한다.
class DemoAppState {
    var isNoticeVisible by mutableStateOf(true)
    var isApplicationCompleted by mutableStateOf(false)
    var isTodaySubmitted by mutableStateOf(false)
    var isCleaningNotificationEnabled by mutableStateOf(true)
    var selectedFloor by mutableIntStateOf(2)
    var selectedYear by mutableIntStateOf(2026)
    var selectedMonth by mutableIntStateOf(9)
    var motivation by mutableStateOf("")
    var appealDrafts by mutableStateOf(emptyMap<Long, DemoAppealDraft>())
    var appealRecordId by mutableLongStateOf(0)
    var submittedAppealRecordIds by mutableStateOf(emptyList<Long>())
    var readNotificationIds by mutableStateOf(listOf(2L))
    var isFrontCamera by mutableStateOf(false)

    fun beginAppeal(recordId: Long) {
        appealRecordId = recordId
    }

    fun appealDraft(recordId: Long): DemoAppealDraft = appealDrafts[recordId] ?: DemoAppealDraft()

    fun updateAppealContent(
        recordId: Long,
        content: String,
    ) {
        appealDrafts = appealDrafts + (recordId to appealDraft(recordId).copy(content = content))
    }

    fun addAppealPhoto(recordId: Long) {
        val currentDraft = appealDraft(recordId)
        appealDrafts =
            appealDrafts + (recordId to currentDraft.copy(photoCount = (currentDraft.photoCount + 1).coerceAtMost(3)))
    }

    fun submitAppeal(recordId: Long) {
        if (appealDraft(recordId).content.isNotBlank() && recordId !in submittedAppealRecordIds) {
            submittedAppealRecordIds = submittedAppealRecordIds + recordId
        }
    }

    val records get() =
        DemoData.records.map { record ->
            if (record.recordId == DemoData.TODAY_RECORD_ID && isTodaySubmitted) {
                record.copy(status = StatusChipType.REVIEWING, submittedTime = "08:04")
            } else {
                record
            }
        }

    val homeUiState: HomeUiState get() =
        HomeUiState.Content(
            notice = if (isNoticeVisible) DemoData.homeNotice else null,
            section =
                HomeSectionUiModel.Cleaning(
                    todayCleaning =
                        DemoData.todayCleaning.copy(
                            status =
                                if (isTodaySubmitted) {
                                    TodayCleaningStatus.AiReviewing("08:04")
                                } else {
                                    TodayCleaningStatus.NotSubmitted("05:32")
                                },
                        ),
                    weekCleaning = DemoData.weekCleaning,
                    recentRecords =
                        records.filter { it.submittedTime != null }.take(3).map {
                            CleaningRecordUiModel(
                                it.recordId,
                                "${it.dateLabel} ${it.submittedTime}",
                                it.areaName,
                                it.status,
                                it.earnedMinutes,
                            )
                        },
                ),
        )

    val activityUiState: ActivityUiState.Content get() {
        val visibleRecords = if (selectedYear == 2026 && selectedMonth == 9) records else emptyList()
        return ActivityUiState.Content(
            selectedYear,
            selectedMonth,
            visibleRecords.sumOf { it.earnedMinutes },
            visibleRecords.count { it.status == StatusChipType.APPROVED },
            visibleRecords.count { it.status == StatusChipType.REJECTED },
            visibleRecords.count { it.status == StatusChipType.NOT_SUBMITTED },
            if (visibleRecords.isEmpty()) {
                emptyList()
            } else {
                listOf(
                    ActivityWeekGroupUiModel(0, emptyList(), visibleRecords),
                )
            },
        )
    }

    val recruitmentUiState get() =
        DemoData.recruitment.copy(
            applicationStatus =
                if (isApplicationCompleted) {
                    RecruitmentApplicationStatus.AlreadyApplied("9월 29일(화) 12:34", 5)
                } else {
                    RecruitmentApplicationStatus.Open(2)
                },
        )

    companion object {
        val Saver =
            listSaver<DemoAppState, Any>(
                save = { snapshot ->
                    listOf(
                        snapshot.isNoticeVisible,
                        snapshot.isApplicationCompleted,
                        snapshot.isTodaySubmitted,
                        snapshot.isCleaningNotificationEnabled,
                        snapshot.selectedFloor,
                        snapshot.selectedYear,
                        snapshot.selectedMonth,
                        snapshot.motivation,
                        snapshot.appealRecordId,
                        ArrayList(snapshot.submittedAppealRecordIds),
                        ArrayList(snapshot.readNotificationIds),
                        snapshot.isFrontCamera,
                        ArrayList(
                            snapshot.appealDrafts.flatMap { (recordId, draft) ->
                                listOf(recordId.toString(), draft.content, draft.photoCount.toString())
                            },
                        ),
                    )
                },
                restore = { saved ->
                    DemoAppState().apply {
                        isNoticeVisible = saved[0] as Boolean
                        isApplicationCompleted = saved[1] as Boolean
                        isTodaySubmitted = saved[2] as Boolean
                        isCleaningNotificationEnabled = saved[3] as Boolean
                        selectedFloor = saved[4] as Int
                        selectedYear = saved[5] as Int
                        selectedMonth = saved[6] as Int
                        motivation = saved[7] as String
                        appealRecordId = saved[8] as Long
                        submittedAppealRecordIds = (saved[9] as List<*>).filterIsInstance<Long>()
                        readNotificationIds = (saved[10] as List<*>).filterIsInstance<Long>()
                        isFrontCamera = saved[11] as Boolean
                        appealDrafts =
                            (saved[12] as List<*>).filterIsInstance<String>().chunked(3).associate { draft ->
                                draft[0].toLong() to DemoAppealDraft(draft[1], draft[2].toInt())
                            }
                    }
                },
            )
    }
}

@Composable
fun rememberDemoAppState(): DemoAppState = rememberSaveable(saver = DemoAppState.Saver) { DemoAppState() }
