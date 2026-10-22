package com.nativelap.ecoguard.feature.demo.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.activity.view.ActivityRoute
import com.nativelap.ecoguard.feature.activity.view.MonthPickerDialog
import com.nativelap.ecoguard.feature.activity.viewmodel.ActivityScreenEvent
import com.nativelap.ecoguard.feature.appeal.view.AppealFormRoute
import com.nativelap.ecoguard.feature.appeal.view.AppealHistoryScreen
import com.nativelap.ecoguard.feature.appeal.view.AppealResultScreen
import com.nativelap.ecoguard.feature.appeal.view.AppealSubmittedScreen
import com.nativelap.ecoguard.feature.appeal.viewmodel.AppealFormUiState
import com.nativelap.ecoguard.feature.appeal.viewmodel.AppealHistoryItemUiModel
import com.nativelap.ecoguard.feature.appeal.viewmodel.AppealResultUiState
import com.nativelap.ecoguard.feature.area.view.AreaRoute
import com.nativelap.ecoguard.feature.demo.viewmodel.DemoAppState
import com.nativelap.ecoguard.feature.demo.viewmodel.DemoData
import com.nativelap.ecoguard.feature.home.view.HomeRoute
import com.nativelap.ecoguard.feature.menu.view.MenuRoute
import com.nativelap.ecoguard.feature.menu.viewmodel.MenuScreenEvent
import com.nativelap.ecoguard.feature.menu.viewmodel.MenuUiState
import com.nativelap.ecoguard.feature.notice.view.NoticeDetailScreen
import com.nativelap.ecoguard.feature.notice.view.NoticeListScreen
import com.nativelap.ecoguard.feature.notice.viewmodel.NoticeDetailUiState
import com.nativelap.ecoguard.feature.notice.viewmodel.NoticeUiState
import com.nativelap.ecoguard.feature.notification.view.NotificationRoute
import com.nativelap.ecoguard.feature.notification.viewmodel.NotificationUiState
import com.nativelap.ecoguard.feature.recruitment.view.ApplicationResultScreen
import com.nativelap.ecoguard.feature.recruitment.view.ApplicationRoute
import com.nativelap.ecoguard.feature.recruitment.view.RecruitmentRoute
import com.nativelap.ecoguard.feature.recruitment.viewmodel.ApplicationResultUiState
import com.nativelap.ecoguard.feature.recruitment.viewmodel.ApplicationUiState
import com.nativelap.ecoguard.feature.verification.view.CameraCaptureScreen
import com.nativelap.ecoguard.feature.verification.view.CameraGuideScreen
import com.nativelap.ecoguard.feature.verification.view.PhotoConfirmScreen
import com.nativelap.ecoguard.feature.verification.view.SubmissionCompletedScreen
import com.nativelap.ecoguard.feature.verification.view.UploadFailedScreen
import com.nativelap.ecoguard.feature.verification.view.VerificationBlockedSheet
import com.nativelap.ecoguard.feature.verification.view.VerificationDayOffScreen
import com.nativelap.ecoguard.feature.verification.view.VerificationDetailScreen
import com.nativelap.ecoguard.feature.verification.view.VerificationResultRoute
import com.nativelap.ecoguard.feature.verification.view.VerificationTimeEndedScreen
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationBlockReason
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationDayOffReason
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationResultUiState
import com.nativelap.ecoguard.navigation.EcoGuardDestination
import com.nativelap.ecoguard.navigation.EcoGuardMainNavigator
import com.nativelap.ecoguard.ui.component.EcoBottomTab
import com.nativelap.ecoguard.ui.component.EcoConfirmDialog
import com.nativelap.ecoguard.ui.component.StatusChipType

@Composable
fun DemoDestinationRoute(
    destination: EcoGuardDestination,
    demoState: DemoAppState,
    navigator: EcoGuardMainNavigator,
    selectedTab: EcoBottomTab,
    onSignOut: () -> Unit,
) {
    val controller = remember(demoState, navigator, onSignOut) { DemoFlowController(demoState, navigator, onSignOut) }
    when (destination) {
        EcoGuardDestination.Home -> {
            HomeRoute(demoState.homeUiState, controller::onHomeEvent)
        }

        EcoGuardDestination.Area -> {
            AreaRoute(
                DemoData.area.copy(selectedFloor = demoState.selectedFloor),
                controller::onAreaEvent,
            )
        }

        EcoGuardDestination.Activity -> {
            ActivityRoute(
                demoState.activityUiState,
                controller::onActivityEvent,
                useNavigationOverlays = true,
            )
        }

        EcoGuardDestination.Menu -> {
            MenuRoute(
                MenuUiState(
                    "최민준",
                    2,
                    3,
                    demoState.activityUiState.approvedCount,
                    demoState.activityUiState.monthlyMinutes,
                    DemoData.AREA_NAME,
                    demoState.isApplicationCompleted,
                    demoState.isCleaningNotificationEnabled,
                ),
                controller::onMenuEvent,
                useNavigationOverlays = true,
            )
        }

        EcoGuardDestination.Notifications -> {
            NotificationRoute(
                NotificationUiState.Content(
                    DemoData.notifications.map { notification ->
                        notification.copy(isRead = notification.notificationId in demoState.readNotificationIds)
                    },
                ),
                selectedTab,
                navigator::goBack,
                navigator::navigateToTab,
                controller::startVerification,
                controller::openNotification,
            )
        }

        EcoGuardDestination.Notices -> {
            NoticeListScreen(
                NoticeUiState.Content(DemoData.notices),
                controller::onNoticeEvent,
            )
        }

        is EcoGuardDestination.NoticeDetail -> {
            val notice = DemoData.notices.find { it.noticeId == destination.noticeId }
            NoticeDetailScreen(
                notice?.let { NoticeDetailUiState.Content(it) } ?: NoticeDetailUiState.Missing,
                controller::onNoticeEvent,
            )
        }

        EcoGuardDestination.Recruitment -> {
            RecruitmentRoute(
                demoState.recruitmentUiState,
                controller::onRecruitmentEvent,
            )
        }

        EcoGuardDestination.Application -> {
            ApplicationRoute(
                ApplicationUiState(
                    2,
                    3,
                    15,
                    "최민준",
                    demoState.motivation,
                ),
                controller::onApplicationEvent,
            )
        }

        EcoGuardDestination.ApplicationResult -> {
            ApplicationResultScreen(
                if (demoState.isApplicationCompleted) {
                    ApplicationResultUiState.Completed(
                        5,
                        "9월 29일(화) 12:34",
                    )
                } else {
                    ApplicationResultUiState.FilledWhileApplying
                },
                controller::onRecruitmentEvent,
            )
        }

        EcoGuardDestination.CameraGuide -> {
            CameraGuideScreen(DemoData.AREA_NAME, controller::onVerificationEvent)
        }

        is EcoGuardDestination.CameraCapture -> {
            CameraCaptureScreen(
                DemoData.AREA_NAME,
                "05:32",
                DemoData.END_TIME,
                { controller.onVerificationEvent(it, destination.appealRecordId) },
            )
        }

        EcoGuardDestination.PhotoConfirm -> {
            PhotoConfirmScreen(
                DemoData.START_TIME,
                DemoData.END_TIME,
                DemoData.AREA_NAME,
                DemoData.SUBMITTED_TIME,
                controller::onVerificationEvent,
            )
        }

        EcoGuardDestination.SubmissionCompleted -> {
            SubmissionCompletedScreen(
                DemoData.AREA_NAME,
                DemoData.SUBMITTED_TIME,
                controller::onVerificationEvent,
            )
        }

        EcoGuardDestination.UploadFailed -> {
            UploadFailedScreen(DemoData.END_TIME, controller::onVerificationEvent)
        }

        EcoGuardDestination.VerificationTimeEnded -> {
            VerificationTimeEndedScreen(
                DemoData.END_TIME,
                controller::onVerificationEvent,
            )
        }

        is EcoGuardDestination.VerificationDayOff -> {
            VerificationDayOffScreen(
                if (destination.isVacation) {
                    VerificationDayOffReason.SCHOOL_VACATION
                } else {
                    VerificationDayOffReason.WEEKEND
                },
                controller::onVerificationEvent,
            )
        }

        is EcoGuardDestination.VerificationResult -> {
            DemoVerificationResultRoute(
                destination.recordId,
                demoState,
                controller,
                navigator,
            )
        }

        is EcoGuardDestination.VerificationDetail -> {
            val record = demoState.records.find { it.recordId == destination.recordId }
            if (record == null) {
                DemoMissingScreen(R.string.demo_missing_record, navigator::goBack)
            } else {
                VerificationDetailScreen(
                    record.dateLabel,
                    stringResource(record.status.labelRes),
                    record.status,
                    record.areaName,
                    "${record.dateLabel} ${record.submittedTime.orEmpty()}",
                    {
                        controller.onVerificationResultEvent(
                            record.recordId,
                            it,
                        )
                    },
                )
            }
        }

        is EcoGuardDestination.AppealForm -> {
            AppealFormRoute(
                AppealFormUiState(
                    DemoData.REJECTION_REASON,
                    demoState.appealDraft(destination.recordId).content,
                    demoState.appealDraft(destination.recordId).photoCount,
                    3,
                    300,
                    DemoData.END_TIME,
                ),
                { controller.onAppealEvent(destination.recordId, it) },
                useExternalInput = true,
            )
        }

        is EcoGuardDestination.AppealSubmitted -> {
            AppealSubmittedScreen(
                "9월 22일(화) 08:04",
                "9월 29일(화) 12:20",
                { controller.onAppealEvent(destination.recordId, it) },
            )
        }

        EcoGuardDestination.AppealHistory -> {
            AppealHistoryScreen(
                demoAppealHistory(demoState),
                {
                    controller.onAppealEvent(
                        demoState.appealRecordId,
                        it,
                    )
                },
            )
        }

        is EcoGuardDestination.AppealResult -> {
            DemoAppealResultRoute(
                destination.appealId,
                demoState,
                controller,
                navigator,
            )
        }

        is EcoGuardDestination.AppealSendFailed -> {
            AppealResultScreen(
                AppealResultUiState.SendFailed,
                {
                    controller.onAppealEvent(
                        destination.recordId,
                        it,
                    )
                },
            )
        }

        EcoGuardDestination.MonthPicker -> {
            MonthPickerDialog(
                demoState.selectedYear,
                demoState.selectedMonth,
                navigator::goBack,
                {
                    year,
                    month,
                    ->
                    controller.onActivityEvent(
                        ActivityScreenEvent.MonthApply(
                            year,
                            month,
                        ),
                    )
                },
                showDialog = false,
            )
        }

        EcoGuardDestination.LogoutConfirm, EcoGuardDestination.WithdrawConfirm -> {
            val isWithdrawal = destination == EcoGuardDestination.WithdrawConfirm
            val titleRes =
                if (isWithdrawal) {
                    R.string.withdraw_confirmation_title
                } else {
                    R.string.logout_confirmation_title
                }
            val descriptionRes =
                if (isWithdrawal) {
                    R.string.withdraw_confirmation_description
                } else {
                    R.string.logout_confirmation_description
                }
            EcoConfirmDialog(
                stringResource(titleRes),
                stringResource(descriptionRes),
                stringResource(R.string.action_cancel),
                stringResource(if (isWithdrawal) R.string.action_withdraw else R.string.action_logout),
                navigator::goBack,
                {
                    val confirmation =
                        if (isWithdrawal) {
                            MenuScreenEvent.WithdrawConfirm
                        } else {
                            MenuScreenEvent.LogoutConfirm
                        }
                    controller.onMenuEvent(confirmation)
                },
                isDestructive = true,
                showDialog = false,
            )
        }

        is EcoGuardDestination.VerificationBlocked -> {
            DemoBlockedRoute(destination, controller, navigator)
        }

        EcoGuardDestination.Login, EcoGuardDestination.TeacherAccountGuide -> {
            error("Auth destination in main stack")
        }
    }
}

@Composable
private fun DemoVerificationResultRoute(
    recordId: Long,
    demoState: DemoAppState,
    controller: DemoFlowController,
    navigator: EcoGuardMainNavigator,
) {
    val record = demoState.records.find { it.recordId == recordId }
    if (record == null) {
        DemoMissingScreen(R.string.demo_missing_record, navigator::goBack)
        return
    }
    if (record.status == StatusChipType.NOT_SUBMITTED) {
        VerificationDetailScreen(
            record.dateLabel,
            stringResource(record.status.labelRes),
            record.status,
            record.areaName,
            record.dateLabel,
            { controller.onVerificationResultEvent(recordId, it) },
        )
        return
    }
    val resultState =
        when (record.status) {
            StatusChipType.APPROVED -> {
                VerificationResultUiState.Approved(
                    record.areaName,
                    record.submittedTime.orEmpty(),
                    record.earnedMinutes,
                )
            }

            StatusChipType.REJECTED -> {
                VerificationResultUiState.Rejected(
                    record.areaName,
                    record.submittedTime.orEmpty(),
                    DemoData.REJECTION_REASON,
                    "구역 표지판이 보이게 다시 찍어 주세요.",
                )
            }

            StatusChipType.REVIEWING -> {
                VerificationResultUiState.TeacherReviewing(
                    record.areaName,
                    record.submittedTime.orEmpty(),
                )
            }

            StatusChipType.NOT_SUBMITTED -> {
                VerificationResultUiState.LoadFailed
            }
        }
    VerificationResultRoute(resultState, { controller.onVerificationResultEvent(recordId, it) })
}

private fun demoAppealHistory(demoState: DemoAppState): List<AppealHistoryItemUiModel> =
    demoState.submittedAppealRecordIds.map { recordId ->
        AppealHistoryItemUiModel(100 + recordId, "9월 22일(화)", 2, "9월 29일(화) 12:20", StatusChipType.REVIEWING, null)
    } +
        listOf(
            AppealHistoryItemUiModel(3, "9월 22일(화)", 2, "9월 29일(화) 12:20", StatusChipType.REVIEWING, null),
            AppealHistoryItemUiModel(2, "9월 22일(화)", 1, "9월 22일(화) 13:02", StatusChipType.REJECTED, null),
            AppealHistoryItemUiModel(1, "9월 21일(월)", 1, "9월 21일(월) 12:40", StatusChipType.APPROVED, 10),
        )

@Composable
private fun DemoAppealResultRoute(
    appealId: Long,
    demoState: DemoAppState,
    controller: DemoFlowController,
    navigator: EcoGuardMainNavigator,
) {
    when {
        appealId == 1L -> {
            AppealResultScreen(
                AppealResultUiState.Approved(
                    "9월 21일(월)",
                    1,
                    10,
                ),
                {
                    controller.onAppealEvent(
                        1,
                        it,
                    )
                },
            )
        }

        appealId == 2L -> {
            AppealResultScreen(
                AppealResultUiState.Rejected(
                    "9월 22일(화)",
                    1,
                    "사진에 구역 표지판이 보이지 않아요",
                    "표지판이 보이게 다시 찍어 주세요.",
                ),
                { controller.onAppealEvent(2, it) },
            )
        }

        appealId == 3L || appealId - 100 in demoState.submittedAppealRecordIds -> {
            AppealReviewingScreen(navigator::goBack)
        }

        else -> {
            DemoMissingScreen(R.string.demo_missing_appeal, navigator::goBack)
        }
    }
}

@Composable
private fun DemoBlockedRoute(
    destination: EcoGuardDestination.VerificationBlocked,
    controller: DemoFlowController,
    navigator: EcoGuardMainNavigator,
) {
    val blockReason =
        when (destination.reason) {
            EcoGuardDestination.BlockReason.OUTSIDE_TIME -> {
                VerificationBlockReason.OutsideVerificationTime(
                    DemoData.START_TIME,
                    DemoData.END_TIME,
                )
            }

            EcoGuardDestination.BlockReason.ALREADY_SUBMITTED -> {
                VerificationBlockReason.AlreadySubmitted(DemoData.SUBMITTED_TIME)
            }

            EcoGuardDestination.BlockReason.CAMERA_PERMISSION -> {
                VerificationBlockReason.CameraPermissionRequired
            }
        }
    VerificationBlockedSheet(
        blockReason,
        onPrimaryActionClick = {
            navigator.goBack()
            if (destination.reason == EcoGuardDestination.BlockReason.ALREADY_SUBMITTED) {
                navigator.push(EcoGuardDestination.VerificationDetail(DemoData.TODAY_RECORD_ID))
            }
        },
        onSecondaryActionClick = navigator::goBack,
        onDismissRequest = navigator::goBack,
        showSheet = false,
    )
}
