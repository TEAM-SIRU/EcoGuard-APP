package com.nativelap.ecoguard.feature.recruitment.module.data.mapper

import com.nativelap.ecoguard.feature.recruitment.module.data.dto.ActivityTimeDto
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.ApplicantResponseDto
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.ApplicationStatusResponseDto
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.ApplyResponseDto
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.CreateRecruitmentResponseDto
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.CurrentRecruitmentResponseDto
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.RecruitmentPeriodDto
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.RecruitmentSummaryResponseDto
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.ActivityTime
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.Applicant
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.ApplicationStatus
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.ApplicationStatusValue
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.ApplyResult
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.CreateRecruitmentResult
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.CurrentRecruitment
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.MyApplicationStatus
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.RecruitmentPeriod
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.RecruitmentPeriodStatus
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.RecruitmentPeriodStatusValue
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.RecruitmentSummary

fun RecruitmentPeriodDto.toDomain(): RecruitmentPeriod =
    RecruitmentPeriod(
        start = start,
        end = end,
    )

fun ActivityTimeDto.toDomain(): ActivityTime =
    ActivityTime(
        start = start,
        end = end,
    )

fun RecruitmentSummaryResponseDto.toDomain(): RecruitmentSummary =
    RecruitmentSummary(
        recruitmentId = recruitmentId,
        semester = semester,
        grade = grade,
        classNo = classNo,
        period = period?.toDomain(),
        activityTime = activityTime?.toDomain(),
        periodStatus = periodStatus.toRecruitmentPeriodStatusValue(),
        maxCount = maxCount,
        applicantCount = applicantCount,
        full = full,
    )

fun CreateRecruitmentResponseDto.toDomain(): CreateRecruitmentResult =
    CreateRecruitmentResult(
        recruitmentId = recruitmentId,
    )

fun ApplicantResponseDto.toDomain(): Applicant =
    Applicant(
        applicationId = applicationId,
        order = order,
        studentNumber = studentNumber,
        name = name,
        motivation = motivation,
        status = status.toApplicationStatusValue(),
        appliedAt = appliedAt,
    )

fun ApplyResponseDto.toDomain(): ApplyResult =
    ApplyResult(
        applicationId = applicationId,
        status = status.toApplicationStatusValue(),
        order = order,
        studentNumber = studentNumber,
        name = name,
        appliedAt = appliedAt,
    )

fun CurrentRecruitmentResponseDto.toDomain(): CurrentRecruitment =
    CurrentRecruitment(
        recruitmentId = recruitmentId,
        semester = semester,
        grade = grade,
        classNo = classNo,
        period = period?.toDomain(),
        activityTime = activityTime?.toDomain(),
        periodStatus = periodStatus.toRecruitmentPeriodStatusValue(),
        maxCount = maxCount,
        currentApplicants = currentApplicants,
        alreadyApplied = alreadyApplied,
        full = full,
    )

fun ApplicationStatusResponseDto.toDomain(): MyApplicationStatus =
    MyApplicationStatus(
        recruitmentId = recruitmentId,
        status = status.toApplicationStatusValue(),
        order = order,
        appliedAt = appliedAt,
        waitingForAssignment = waitingForAssignment,
    )

private fun String?.toRecruitmentPeriodStatusValue(): RecruitmentPeriodStatusValue =
    RecruitmentPeriodStatusValue(
        value =
            when (this) {
                "UPCOMING" -> RecruitmentPeriodStatus.UPCOMING
                "OPEN" -> RecruitmentPeriodStatus.OPEN
                "CLOSED" -> RecruitmentPeriodStatus.CLOSED
                else -> RecruitmentPeriodStatus.UNKNOWN
            },
        rawValue = this,
    )

private fun String?.toApplicationStatusValue(): ApplicationStatusValue =
    ApplicationStatusValue(
        value =
            when (this) {
                "PENDING" -> ApplicationStatus.PENDING
                "APPROVED" -> ApplicationStatus.APPROVED
                "REJECTED" -> ApplicationStatus.REJECTED
                else -> ApplicationStatus.UNKNOWN
            },
        rawValue = this,
    )
