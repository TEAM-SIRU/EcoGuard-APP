package com.nativelap.ecoguard.feature.home.viewmodel

import com.nativelap.ecoguard.feature.activity.module.domain.model.ActivityRecord
import com.nativelap.ecoguard.feature.activity.module.domain.model.ActivityResultStatus
import com.nativelap.ecoguard.feature.activity.module.domain.model.ActivitySummary
import com.nativelap.ecoguard.feature.activity.module.domain.model.MyActivity
import com.nativelap.ecoguard.feature.activity.module.domain.model.WeeklyActivity
import com.nativelap.ecoguard.feature.activity.module.domain.model.WeeklyDay
import com.nativelap.ecoguard.feature.cleaningarea.module.domain.model.MyCleaningAssignment
import com.nativelap.ecoguard.feature.notice.module.domain.model.NoticeListItem
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.ApplicationStatus
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.ApplicationStatusValue
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.CurrentRecruitment
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.MyApplicationStatus
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.RecruitmentPeriodStatus
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.RecruitmentPeriodStatusValue
import com.nativelap.ecoguard.feature.verification.module.domain.model.MyVerification
import com.nativelap.ecoguard.feature.verification.module.domain.model.TodayVerification
import com.nativelap.ecoguard.feature.verification.module.domain.model.VerificationStatus
import com.nativelap.ecoguard.feature.verification.module.domain.model.VerificationStatusValue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate

class HomeUiMapperTest {
    @Test
    fun recruitingCardUsesSwaggerRecruitmentCountsAndClass() {
        val currentRecruitment = currentRecruitment()

        val section = currentRecruitment.toHomeRecruitingSection()

        assertEquals(
            HomeSectionUiModel.Recruiting(
                semesterName = "2026-2",
                maxApplicantsPerClass = 5,
                grade = 2,
                classNumber = 3,
                appliedCount = 4,
            ),
            section,
        )
    }

    @Test
    fun recruitmentWithoutRequiredServerFieldsDoesNotInventDefaults() {
        val incompleteRecruitment = currentRecruitment().copy(currentApplicants = null)

        assertNull(incompleteRecruitment.toHomeRecruitingSection())
    }

    @Test
    fun appliedStudentWithNoAssignmentIsWaitingOnlyWhenServerSaysSo() {
        val appliedRecruitment = currentRecruitment().copy(alreadyApplied = true)

        assertEquals(
            HomeSectionUiModel.WaitingAssignment,
            appliedRecruitment.toHomeSection(applicationStatus(waitingForAssignment = true)),
        )
        assertNull(appliedRecruitment.toHomeSection(applicationStatus = null))
    }

    @Test
    fun closedRecruitmentIsNotShownAsOpenRecruiting() {
        val closedRecruitment =
            currentRecruitment().copy(
                periodStatus = RecruitmentPeriodStatusValue(RecruitmentPeriodStatus.CLOSED, "CLOSED"),
            )

        assertEquals(HomeSectionUiModel.NoRecruitment, closedRecruitment.toHomeSection(applicationStatus = null))
    }

    @Test
    fun noticeUsesServerTitlePreviewDateAndReadState() {
        val notice =
            listOf(
                NoticeListItem(
                    noticeId = 31,
                    title = "오늘 활동 안내",
                    preview = "인증 마감 시간을 확인해 주세요.",
                    createdAt = "2026-10-25T08:00:00",
                    isRead = false,
                ),
            ).toHomeNoticeUiModel()

        assertEquals("10월 25일", notice?.publishedDate)
        assertEquals("오늘 활동 안내", notice?.headline)
        assertEquals("인증 마감 시간을 확인해 주세요.", notice?.body)
        assertTrue(notice?.isNew == true)
    }

    @Test
    fun cleaningCardMapsServerVerificationAndWeeklyActivity() {
        val today = LocalDate.parse("2026-10-25")
        val assignment = assignment()
        val cleaning =
            assignment.toHomeCleaningSection(
                todayVerification = todayVerification(canSubmit = true, submitted = false),
                myVerifications = emptyList(),
                weeklyActivity = weeklyActivity(today),
                monthlyActivity = monthlyActivity(today),
                today = today,
            )

        assertEquals("08:00", cleaning?.todayCleaning?.cleaningStartTime)
        assertEquals("08:10", cleaning?.todayCleaning?.cleaningEndTime)
        assertEquals("05:00", (cleaning?.todayCleaning?.status as? TodayCleaningStatus.NotSubmitted)?.timeUntilDeadline)
        assertEquals(2, cleaning?.weekCleaning?.completedDayCount)
        assertTrue(cleaning?.weekCleaning?.days?.any { it.dayOfWeek == DayOfWeek.SATURDAY && it.isCompleted } == true)
    }

    @Test
    fun approvedVerificationUsesMatchingActivityMinutesAndRecentRecordsRequireIds() {
        val today = LocalDate.parse("2026-10-25")
        val activity =
            monthlyActivity(today).copy(
                records =
                    listOf(
                        ActivityRecord(
                            date = today.toString(),
                            area = "본관 복도",
                            result = ActivityResultStatus.APPROVED,
                            verificationId = 100L,
                            photoUrl = null,
                            minutes = 10,
                        ),
                        ActivityRecord(
                            date = today.minusDays(1).toString(),
                            area = "운동장",
                            result = ActivityResultStatus.REJECTED,
                            verificationId = null,
                            photoUrl = null,
                            minutes = 0,
                        ),
                    ),
            )
        val cleaning =
            assignment().toHomeCleaningSection(
                todayVerification =
                    todayVerification(
                        canSubmit = false,
                        submitted = true,
                        verificationStatus = VerificationStatus.APPROVED,
                        verificationId = 100L,
                    ),
                myVerifications = emptyList(),
                weeklyActivity = weeklyActivity(today),
                monthlyActivity = activity,
                today = today,
            )

        assertEquals(10, (cleaning?.todayCleaning?.status as? TodayCleaningStatus.Approved)?.earnedMinutes)
        assertEquals(1, cleaning?.recentRecords?.size)
    }

    @Test
    fun rejectedVerificationUsesServerFailureReasonAndUnknownStatusFailsMapping() {
        val today = LocalDate.parse("2026-10-25")
        val rejected =
            assignment().toHomeCleaningSection(
                todayVerification =
                    todayVerification(
                        canSubmit = false,
                        submitted = true,
                        verificationStatus = VerificationStatus.REJECTED,
                        verificationId = 101L,
                    ),
                myVerifications =
                    listOf(
                        MyVerification(
                            verificationId = 101L,
                            photoUrl = null,
                            date = today.toString(),
                            submittedAt = "08:05:00",
                            areaName = "본관 복도",
                            reviewStatus = VerificationStatusValue(VerificationStatus.REJECTED, "REJECTED"),
                            failReasons = listOf("청소 구역이 사진에 보이지 않아요"),
                        ),
                    ),
                weeklyActivity = weeklyActivity(today),
                monthlyActivity = monthlyActivity(today),
                today = today,
            )

        assertEquals(
            "청소 구역이 사진에 보이지 않아요",
            (rejected?.todayCleaning?.status as? TodayCleaningStatus.Rejected)?.rejectionReason,
        )

        val unknown =
            assignment().toHomeCleaningSection(
                todayVerification =
                    todayVerification(
                        canSubmit = false,
                        submitted = true,
                        verificationStatus = VerificationStatus.UNKNOWN,
                        verificationId = 102L,
                    ),
                myVerifications = emptyList(),
                weeklyActivity = weeklyActivity(today),
                monthlyActivity = monthlyActivity(today),
                today = today,
            )
        assertNull(unknown)
    }

    @Test
    fun emptyNoticeListIsDistinctFromAnUnreadNotice() {
        assertNull(emptyList<NoticeListItem>().toHomeNoticeUiModel())
        assertFalse(
            listOf(
                NoticeListItem(1, "확인한 공지", "내용", "2026-10-20", true),
            ).toHomeNoticeUiModel()?.isNew ?: true,
        )
    }
}

private fun currentRecruitment() =
    CurrentRecruitment(
        recruitmentId = 5L,
        semester = "2026-2",
        grade = 2,
        classNo = 3,
        period = null,
        activityTime = null,
        periodStatus = RecruitmentPeriodStatusValue(RecruitmentPeriodStatus.OPEN, "OPEN"),
        maxCount = 5,
        currentApplicants = 4L,
        alreadyApplied = false,
        full = false,
    )

private fun applicationStatus(waitingForAssignment: Boolean) =
    MyApplicationStatus(
        recruitmentId = 5L,
        status = ApplicationStatusValue(ApplicationStatus.PENDING, "PENDING"),
        order = 2L,
        appliedAt = "2026-10-20T10:00:00",
        waitingForAssignment = waitingForAssignment,
    )

private fun assignment() =
    MyCleaningAssignment(
        areaId = 7L,
        zoneCode = "A-1",
        areaName = "본관 복도",
        description = null,
        cleanTime = "08:00 - 08:10",
        members = emptyList(),
    )

private fun todayVerification(
    canSubmit: Boolean?,
    submitted: Boolean?,
    verificationStatus: VerificationStatus = VerificationStatus.UNKNOWN,
    verificationId: Long? = null,
) = TodayVerification(
    serverTime = "2026-10-25T08:05:00",
    areaId = 7L,
    areaName = "본관 복도",
    cleanTime = "08:00 - 08:10",
    startTime = "08:00",
    endTime = "08:10",
    canSubmit = canSubmit,
    unavailableReason = null,
    submitted = submitted,
    verificationId = verificationId,
    status = VerificationStatusValue(verificationStatus, verificationStatus.name),
    submittedAt = "2026-10-25T08:05:00",
)

private fun weeklyActivity(today: LocalDate) =
    WeeklyActivity(
        weekStart = today.minusDays(6).toString(),
        weekEnd = today.toString(),
        completedDays = 2,
        requiredDays = 5,
        days =
            listOf(
                WeeklyDay(today.minusDays(1).toString(), ActivityResultStatus.APPROVED),
                WeeklyDay(today.toString(), ActivityResultStatus.NOT_SUBMITTED),
            ),
    )

private fun monthlyActivity(today: LocalDate) =
    MyActivity(
        totalMinutes = 10,
        year = today.year,
        month = today.monthValue,
        monthlyMinutes = 10,
        summary = ActivitySummary(1, 5, 1, 0, 0),
        records = emptyList(),
    )
