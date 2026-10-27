package com.nativelap.ecoguard.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface EcoGuardDestination : NavKey {
    @Serializable data object Login : EcoGuardDestination

    @Serializable data object TeacherAccountGuide : EcoGuardDestination

    @Serializable data object Home : EcoGuardDestination

    @Serializable data object Area : EcoGuardDestination

    @Serializable data object Activity : EcoGuardDestination

    @Serializable data object Menu : EcoGuardDestination

    @Serializable data object Notifications : EcoGuardDestination

    @Serializable data object Notices : EcoGuardDestination

    @Serializable data class NoticeDetail(
        val noticeId: Long,
    ) : EcoGuardDestination

    @Serializable data object Recruitment : EcoGuardDestination

    @Serializable data class Application(
        val recruitmentId: Long,
        val grade: Int,
        val classNo: Int,
    ) : EcoGuardDestination

    @Serializable data class ApplicationResult(
        val applicationOrder: Int,
        val appliedDateTime: String,
    ) : EcoGuardDestination

    @Serializable data class CameraGuide(
        val areaId: Long,
        val areaName: String,
        val cleaningStartTime: String,
        val cleaningEndTime: String,
        val timeRemaining: String,
    ) : EcoGuardDestination

    @Serializable data class CameraCapture(
        val areaId: Long,
        val areaName: String,
        val cleaningStartTime: String,
        val cleaningEndTime: String,
        val timeRemaining: String,
        val deadlineTime: String,
    ) : EcoGuardDestination

    @Serializable data class PhotoConfirm(
        val areaId: Long,
        val areaName: String,
        val cleaningStartTime: String,
        val cleaningEndTime: String,
        val capturedTime: String,
        val deadlineTime: String,
        val photoPath: String,
    ) : EcoGuardDestination

    @Serializable data class SubmissionCompleted(
        val areaName: String,
        val submittedAt: String,
        val verificationId: Long,
        val photoPath: String,
    ) : EcoGuardDestination

    @Serializable data class UploadFailed(
        val areaId: Long,
        val areaName: String,
        val photoPath: String,
        val deadlineTime: String,
    ) : EcoGuardDestination

    @Serializable data object VerificationTimeEnded : EcoGuardDestination

    @Serializable data class VerificationResult(
        val recordId: Long,
    ) : EcoGuardDestination

    @Serializable data class VerificationDetail(
        val recordId: Long,
    ) : EcoGuardDestination

    @Serializable data class VerificationDayOff(
        val isVacation: Boolean,
    ) : EcoGuardDestination

    @Serializable data class VerificationBlocked(
        val reason: BlockReason,
    ) : EcoGuardDestination

    @Serializable data class AppealForm(
        val recordId: Long,
        val rejectionReason: String = "",
        val verificationDeadlineTime: String = "",
    ) : EcoGuardDestination

    @Serializable data class AppealSubmitted(
        val recordId: Long,
    ) : EcoGuardDestination

    @Serializable data object AppealHistory : EcoGuardDestination

    @Serializable data class AppealResult(
        val appealId: Long,
    ) : EcoGuardDestination

    @Serializable data class AppealSendFailed(
        val recordId: Long,
    ) : EcoGuardDestination

    @Serializable data object MonthPicker : EcoGuardDestination

    @Serializable data object LogoutConfirm : EcoGuardDestination

    @Serializable data object WithdrawConfirm : EcoGuardDestination

    @Serializable
    enum class BlockReason { OUTSIDE_TIME, ALREADY_SUBMITTED, CAMERA_PERMISSION }
}
