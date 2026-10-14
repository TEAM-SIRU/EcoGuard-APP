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

    @Serializable data object Application : EcoGuardDestination

    @Serializable data object ApplicationResult : EcoGuardDestination

    @Serializable data object CameraGuide : EcoGuardDestination

    @Serializable data class CameraCapture(
        val appealRecordId: Long? = null,
    ) : EcoGuardDestination

    @Serializable data object PhotoConfirm : EcoGuardDestination

    @Serializable data object SubmissionCompleted : EcoGuardDestination

    @Serializable data object UploadFailed : EcoGuardDestination

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
