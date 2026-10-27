package com.nativelap.ecoguard.feature.activity.module.data.mapper

import com.nativelap.ecoguard.feature.activity.module.data.dto.ActivityRecordDto
import com.nativelap.ecoguard.feature.activity.module.data.dto.ActivitySummaryDto
import com.nativelap.ecoguard.feature.activity.module.data.dto.MyActivityResponseDto
import com.nativelap.ecoguard.feature.activity.module.data.dto.StudentActivityResponseDto
import com.nativelap.ecoguard.feature.activity.module.data.dto.WeeklyActivityResponseDto
import com.nativelap.ecoguard.feature.activity.module.data.dto.WeeklyDayDto
import com.nativelap.ecoguard.feature.activity.module.domain.model.ActivityRecord
import com.nativelap.ecoguard.feature.activity.module.domain.model.ActivityResultStatus
import com.nativelap.ecoguard.feature.activity.module.domain.model.ActivitySummary
import com.nativelap.ecoguard.feature.activity.module.domain.model.MyActivity
import com.nativelap.ecoguard.feature.activity.module.domain.model.StudentActivity
import com.nativelap.ecoguard.feature.activity.module.domain.model.WeeklyActivity
import com.nativelap.ecoguard.feature.activity.module.domain.model.WeeklyDay

fun MyActivityResponseDto.toDomain(): MyActivity =
    MyActivity(
        totalMinutes = totalMinutes,
        year = year,
        month = month,
        monthlyMinutes = monthlyMinutes,
        summary = summary.toDomain(),
        records = records.map(ActivityRecordDto::toDomain),
    )

fun StudentActivityResponseDto.toDomain(): StudentActivity =
    StudentActivity(
        studentId = studentId,
        studentNumber = studentNumber,
        name = name,
        area = area,
        photoUrl = photoUrl,
        attendanceDays = attendanceDays,
        totalMinutes = totalMinutes,
    )

fun WeeklyActivityResponseDto.toDomain(): WeeklyActivity =
    WeeklyActivity(
        weekStart = weekStart,
        weekEnd = weekEnd,
        completedDays = completedDays,
        requiredDays = requiredDays,
        days = days.map(WeeklyDayDto::toDomain),
    )

private fun ActivityRecordDto.toDomain(): ActivityRecord =
    ActivityRecord(
        date = date,
        area = area,
        result = result.toActivityResultStatus(),
        verificationId = verificationId,
        photoUrl = photoUrl,
        minutes = minutes,
    )

private fun ActivitySummaryDto.toDomain(): ActivitySummary =
    ActivitySummary(
        completedDays = completedDays,
        requiredDays = requiredDays,
        approvedCount = approvedCount,
        rejectedCount = rejectedCount,
        notSubmittedCount = notSubmittedCount,
    )

private fun WeeklyDayDto.toDomain(): WeeklyDay =
    WeeklyDay(
        date = date,
        result = result.toActivityResultStatus(),
    )

private fun String.toActivityResultStatus(): ActivityResultStatus =
    when (this) {
        "APPROVED" -> ActivityResultStatus.APPROVED
        "REJECTED" -> ActivityResultStatus.REJECTED
        "PROCESSING" -> ActivityResultStatus.PROCESSING
        "MANUAL_REVIEW" -> ActivityResultStatus.MANUAL_REVIEW
        "NOT_SUBMITTED" -> ActivityResultStatus.NOT_SUBMITTED
        "UPCOMING" -> ActivityResultStatus.UPCOMING
        else -> ActivityResultStatus.UNKNOWN
    }
