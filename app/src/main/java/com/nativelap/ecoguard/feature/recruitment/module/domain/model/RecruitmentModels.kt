package com.nativelap.ecoguard.feature.recruitment.module.domain.model

enum class RecruitmentPeriodStatus {
    UPCOMING,
    OPEN,
    CLOSED,
    UNKNOWN,
}

data class RecruitmentPeriodStatusValue(
    val value: RecruitmentPeriodStatus,
    val rawValue: String?,
)

enum class ApplicationStatus {
    PENDING,
    APPROVED,
    REJECTED,
    UNKNOWN,
}

data class ApplicationStatusValue(
    val value: ApplicationStatus,
    val rawValue: String?,
)

data class RecruitmentPeriod(
    val start: String?,
    val end: String?,
)

data class ActivityTime(
    val start: String?,
    val end: String?,
)

data class RecruitmentSummary(
    val recruitmentId: Long?,
    val semester: String?,
    val grade: Int?,
    val classNo: Int?,
    val period: RecruitmentPeriod?,
    val activityTime: ActivityTime?,
    val periodStatus: RecruitmentPeriodStatusValue,
    val maxCount: Int?,
    val applicantCount: Long?,
    val full: Boolean?,
)

data class CreateRecruitmentResult(
    val recruitmentId: Long?,
)

data class Applicant(
    val applicationId: Long?,
    val order: Long?,
    val studentNumber: String?,
    val name: String?,
    val motivation: String?,
    val status: ApplicationStatusValue,
    val appliedAt: String?,
)

data class ApplyResult(
    val applicationId: Long?,
    val status: ApplicationStatusValue,
    val order: Long?,
    val studentNumber: String?,
    val name: String?,
    val appliedAt: String?,
)

data class CurrentRecruitment(
    val recruitmentId: Long?,
    val semester: String?,
    val grade: Int?,
    val classNo: Int?,
    val period: RecruitmentPeriod?,
    val activityTime: ActivityTime?,
    val periodStatus: RecruitmentPeriodStatusValue,
    val maxCount: Int?,
    val currentApplicants: Long?,
    val alreadyApplied: Boolean?,
    val full: Boolean?,
)

data class MyApplicationStatus(
    val recruitmentId: Long?,
    val status: ApplicationStatusValue,
    val order: Long?,
    val appliedAt: String?,
    val waitingForAssignment: Boolean?,
)

data class CreateRecruitmentCommand(
    val semester: String,
    val grade: Int,
    val classNo: Int,
    val maxCount: Int,
    val startDate: String,
    val endDate: String,
    val activityStartTime: String?,
    val activityEndTime: String?,
)

data class ApplyCommand(
    val recruitmentId: Long,
    val motivation: String,
)

data class UpdateRecruitmentCommand(
    val recruitmentId: Long,
    val maxCount: Int?,
    val startDate: String?,
    val endDate: String?,
    val activityStartTime: String?,
    val activityEndTime: String?,
)
