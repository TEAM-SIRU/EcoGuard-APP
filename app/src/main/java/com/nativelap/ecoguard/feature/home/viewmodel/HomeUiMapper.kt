package com.nativelap.ecoguard.feature.home.viewmodel

import com.nativelap.ecoguard.feature.activity.module.domain.model.ActivityRecord
import com.nativelap.ecoguard.feature.activity.module.domain.model.ActivityResultStatus
import com.nativelap.ecoguard.feature.activity.module.domain.model.MyActivity
import com.nativelap.ecoguard.feature.activity.module.domain.model.WeeklyActivity
import com.nativelap.ecoguard.feature.cleaningarea.module.domain.model.MyCleaningAssignment
import com.nativelap.ecoguard.feature.notice.module.domain.model.NoticeListItem
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.CurrentRecruitment
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.MyApplicationStatus
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.RecruitmentPeriodStatus
import com.nativelap.ecoguard.feature.verification.module.domain.model.MyVerification
import com.nativelap.ecoguard.feature.verification.module.domain.model.TodayVerification
import com.nativelap.ecoguard.feature.verification.module.domain.model.VerificationStatus
import com.nativelap.ecoguard.ui.component.StatusChipType
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

internal fun CurrentRecruitment.toHomeRecruitingSection(): HomeSectionUiModel.Recruiting? {
    if (periodStatus.value != RecruitmentPeriodStatus.OPEN) return null
    val currentGrade = grade ?: return null
    val currentClassNumber = classNo ?: return null
    val currentSemesterName = semester?.takeIf(String::isNotBlank) ?: return null
    val applicantLimit = maxCount ?: return null
    val currentApplicantCount = currentApplicants?.toIntExactOrNull() ?: return null

    return HomeSectionUiModel.Recruiting(
        semesterName = currentSemesterName,
        maxApplicantsPerClass = applicantLimit,
        grade = currentGrade,
        classNumber = currentClassNumber,
        appliedCount = currentApplicantCount,
    )
}

internal fun MyCleaningAssignment.toHomeCleaningSection(
    todayVerification: TodayVerification,
    myVerifications: List<MyVerification>,
    weeklyActivity: WeeklyActivity,
    monthlyActivity: MyActivity,
    today: LocalDate,
): HomeSectionUiModel.Cleaning? {
    val todayModel =
        todayVerification.toTodayCleaningUiModel(
            this,
            myVerifications,
            monthlyActivity,
            today,
        ) ?: return null
    val weeklyModel = weeklyActivity.toWeekCleaningUiModel(today) ?: return null
    val records = monthlyActivity.records.toHomeRecentRecords()

    return HomeSectionUiModel.Cleaning(
        todayCleaning = todayModel,
        weekCleaning = weeklyModel,
        recentRecords = records,
    )
}

internal fun List<NoticeListItem>.toHomeNoticeUiModel(): HomeNoticeUiModel? =
    firstOrNull()?.let { notice ->
        HomeNoticeUiModel(
            publishedDate = notice.createdAt.toDisplayDate(),
            headline = notice.title,
            body = notice.preview,
            isNew = !notice.isRead,
        )
    }

internal fun CurrentRecruitment.toHomeSection(applicationStatus: MyApplicationStatus?): HomeSectionUiModel? {
    if (alreadyApplied == true && applicationStatus == null) return null
    if (applicationStatus?.waitingForAssignment == true) return HomeSectionUiModel.WaitingAssignment
    return when (periodStatus.value) {
        RecruitmentPeriodStatus.OPEN -> toHomeRecruitingSection()

        RecruitmentPeriodStatus.UPCOMING,
        RecruitmentPeriodStatus.CLOSED,
        -> HomeSectionUiModel.NoRecruitment

        RecruitmentPeriodStatus.UNKNOWN -> null
    }
}

private fun TodayVerification.toTodayCleaningUiModel(
    assignment: MyCleaningAssignment,
    myVerifications: List<MyVerification>,
    activity: MyActivity,
    today: LocalDate,
): TodayCleaningUiModel? {
    val isSubmitted = submitted ?: return null
    val canSubmitNow = canSubmit ?: return null
    val cleaningRange =
        parseTimeRange(assignment.cleanTime ?: cleanTime)
            ?: parseTimeRange("${startTime.orEmpty()} ${endTime.orEmpty()}")
            ?: TimeRange(UNKNOWN_VALUE, UNKNOWN_VALUE)
    val statusModel =
        if (!isSubmitted) {
            if (canSubmitNow) {
                TodayCleaningStatus.NotSubmitted(
                    timeUntilDeadline = remainingTime(serverTime, endTime),
                )
            } else {
                TodayCleaningStatus.OutsideVerificationTime(
                    availableFromTime = startTime.toDisplayTimeOrUnknown(),
                )
            }
        } else {
            when (status?.value) {
                VerificationStatus.PROCESSING -> {
                    TodayCleaningStatus.AiReviewing(submittedAt.toDisplayTimeOrUnknown())
                }

                VerificationStatus.MANUAL_REVIEW -> {
                    TodayCleaningStatus.TeacherReviewing(submittedAt.toDisplayTimeOrUnknown())
                }

                VerificationStatus.APPROVED -> {
                    val approvedRecord =
                        activity.records.firstOrNull { record ->
                            (verificationId != null && record.verificationId == verificationId) ||
                                (record.date == today.toString() && record.result == ActivityResultStatus.APPROVED)
                        } ?: return null
                    TodayCleaningStatus.Approved(earnedMinutes = approvedRecord.minutes)
                }

                VerificationStatus.REJECTED -> {
                    val rejectedVerification =
                        verificationId?.let { currentVerificationId ->
                            myVerifications.firstOrNull { verification ->
                                verification.verificationId == currentVerificationId
                            }
                        }
                    TodayCleaningStatus.Rejected(
                        rejectionReason =
                            rejectedVerification
                                ?.failReasons
                                ?.filter(String::isNotBlank)
                                ?.joinToString("\n")
                                ?.takeIf(String::isNotBlank) ?: return null,
                    )
                }

                VerificationStatus.UNKNOWN,
                null,
                -> {
                    return null
                }
            }
        }

    return TodayCleaningUiModel(
        areaId = assignment.areaId,
        cleaningStartTime = cleaningRange.start,
        cleaningEndTime = cleaningRange.end,
        assignedAreaName = assignment.areaName,
        status = statusModel,
    )
}

private fun WeeklyActivity.toWeekCleaningUiModel(today: LocalDate): WeekCleaningUiModel? {
    val mappedDays =
        days.mapNotNull { activityDay ->
            val date = parseDate(activityDay.date) ?: return@mapNotNull null
            val dayOfWeek = date.dayOfWeek
            if (activityDay.result == ActivityResultStatus.UNKNOWN) return@mapNotNull null
            WeekDayUiModel(
                dayOfWeek = dayOfWeek,
                isToday = date == today,
                isCompleted = activityDay.result == ActivityResultStatus.APPROVED,
            )
        }
    if (mappedDays.size != days.size) return null

    return WeekCleaningUiModel(
        completedDayCount = completedDays,
        totalDayCount = requiredDays,
        days = mappedDays.sortedBy { it.dayOfWeek.value },
    )
}

private fun List<ActivityRecord>.toHomeRecentRecords(): List<CleaningRecordUiModel> =
    sortedByDescending { record -> parseDate(record.date) ?: LocalDate.MIN }
        .mapNotNull { record ->
            val recordId = record.verificationId ?: return@mapNotNull null
            val recordDate = parseDate(record.date) ?: return@mapNotNull null
            val statusChip = record.result.toStatusChip() ?: return@mapNotNull null
            CleaningRecordUiModel(
                recordId = recordId,
                submittedDateTime = record.date.toDisplayDate(),
                areaName = record.area ?: UNKNOWN_VALUE,
                status = statusChip,
                earnedMinutes = record.minutes,
            )
        }.take(HOME_RECENT_RECORD_LIMIT)

private fun ActivityResultStatus.toStatusChip(): StatusChipType? =
    when (this) {
        ActivityResultStatus.APPROVED -> StatusChipType.APPROVED

        ActivityResultStatus.REJECTED -> StatusChipType.REJECTED

        ActivityResultStatus.PROCESSING,
        ActivityResultStatus.MANUAL_REVIEW,
        -> StatusChipType.REVIEWING

        ActivityResultStatus.NOT_SUBMITTED,
        ActivityResultStatus.UPCOMING,
        -> StatusChipType.NOT_SUBMITTED

        ActivityResultStatus.UNKNOWN -> null
    }

private fun String?.toDisplayTimeOrUnknown(): String = this?.let(::parseTime)?.format(timeFormatter) ?: UNKNOWN_VALUE

private fun String.toDisplayDate(): String = parseDate(this)?.format(dateFormatter) ?: this

private fun parseDate(dateValue: String): LocalDate? = runCatching { LocalDate.parse(dateValue.take(10)) }.getOrNull()

private fun parseTime(timeValue: String): LocalTime? {
    if ('T' in timeValue) {
        return runCatching { LocalTime.parse(timeValue.substringAfter('T').take(5), timeFormatter) }.getOrNull()
    }
    val match = TIME_PATTERN.find(timeValue) ?: return null
    return runCatching { LocalTime.parse(match.value, timeFormatter) }.getOrNull()
}

private fun parseTimeRange(timeRange: String?): TimeRange? {
    if (timeRange.isNullOrBlank()) return null
    val times =
        TIME_PATTERN
            .findAll(timeRange)
            .mapNotNull { match ->
                runCatching { LocalTime.parse(match.value, timeFormatter) }.getOrNull()
            }.toList()
    if (times.size < 2) return null
    return TimeRange(times[0].format(timeFormatter), times[1].format(timeFormatter))
}

private fun remainingTime(
    serverTime: String?,
    endTime: String?,
): String {
    val currentTime = serverTime?.let(::parseTime) ?: return UNKNOWN_REMAINING_TIME
    val deadlineTime = endTime?.let(::parseTime) ?: return UNKNOWN_REMAINING_TIME
    val remainingSeconds = Duration.between(currentTime, deadlineTime).seconds.coerceAtLeast(0)
    val minutes = remainingSeconds / SECONDS_PER_MINUTE
    val seconds = remainingSeconds % SECONDS_PER_MINUTE
    return "%02d:%02d".format(Locale.ROOT, minutes, seconds)
}

private fun Long.toIntExactOrNull(): Int? = if (this in Int.MIN_VALUE..Int.MAX_VALUE) toInt() else null

private data class TimeRange(
    val start: String,
    val end: String,
)

private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
private val dateFormatter = DateTimeFormatter.ofPattern("M월 d일", Locale.KOREAN)
private val TIME_PATTERN = Regex("\\b\\d{1,2}:\\d{2}\\b")
private const val HOME_RECENT_RECORD_LIMIT = 3
private const val SECONDS_PER_MINUTE = 60
private const val UNKNOWN_VALUE = "—"
private const val UNKNOWN_REMAINING_TIME = "--:--"
