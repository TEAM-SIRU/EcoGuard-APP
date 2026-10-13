package com.nativelap.ecoguard.feature.demo.view

import com.nativelap.ecoguard.feature.activity.viewmodel.ActivityScreenEvent
import com.nativelap.ecoguard.feature.appeal.viewmodel.AppealScreenEvent
import com.nativelap.ecoguard.feature.area.viewmodel.AreaScreenEvent
import com.nativelap.ecoguard.feature.demo.viewmodel.DemoAppState
import com.nativelap.ecoguard.feature.demo.viewmodel.DemoData
import com.nativelap.ecoguard.feature.home.viewmodel.HomeScreenEvent
import com.nativelap.ecoguard.feature.menu.viewmodel.MenuScreenEvent
import com.nativelap.ecoguard.feature.notice.viewmodel.NoticeScreenEvent
import com.nativelap.ecoguard.feature.notification.viewmodel.NotificationTarget
import com.nativelap.ecoguard.feature.recruitment.viewmodel.RecruitmentScreenEvent
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationResultScreenEvent
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationScreenEvent
import com.nativelap.ecoguard.navigation.EcoGuardDestination
import com.nativelap.ecoguard.navigation.EcoGuardMainNavigator
import com.nativelap.ecoguard.ui.component.EcoBottomTab

// Route의 샘플 이벤트 처리. Navigator에는 이미 결정된 목적지만 전달한다.
class DemoFlowController(
    private val demoState: DemoAppState,
    private val navigator: EcoGuardMainNavigator,
    private val onSignOut: () -> Unit,
) {
    fun startVerification() {
        navigator.push(
            if (demoState.isTodaySubmitted) {
                EcoGuardDestination.VerificationBlocked(EcoGuardDestination.BlockReason.ALREADY_SUBMITTED)
            } else {
                EcoGuardDestination.CameraGuide
            },
        )
    }

    private fun openVerificationResult(recordId: Long) {
        val record = demoState.records.find { it.recordId == recordId }
        if (record?.status == com.nativelap.ecoguard.ui.component.StatusChipType.NOT_SUBMITTED) {
            startVerification()
        } else {
            navigator.push(EcoGuardDestination.VerificationResult(recordId))
        }
    }

    fun onHomeEvent(event: HomeScreenEvent) {
        when (event) {
            HomeScreenEvent.NoticeIconClick -> {
                navigator.push(EcoGuardDestination.Notifications)
            }

            HomeScreenEvent.NoticeCloseClick -> {
                demoState.isNoticeVisible = false
            }

            HomeScreenEvent.NoticeDetailClick -> {
                navigator.push(EcoGuardDestination.NoticeDetail(1))
            }

            HomeScreenEvent.CleaningVerificationClick -> {
                startVerification()
            }

            HomeScreenEvent.SubmittedPhotoClick -> {
                navigator.push(
                    EcoGuardDestination.VerificationDetail(DemoData.TODAY_RECORD_ID),
                )
            }

            HomeScreenEvent.ActivityRecordClick, HomeScreenEvent.AllRecordsClick -> {
                navigator.navigateToTab(
                    EcoBottomTab.ACTIVITY,
                )
            }

            HomeScreenEvent.AppealClick -> {
                openAppeal(DemoData.TODAY_RECORD_ID)
            }

            HomeScreenEvent.RecruitmentClick -> {
                navigator.push(EcoGuardDestination.Recruitment)
            }

            HomeScreenEvent.ApplicationResultClick -> {
                navigator.push(EcoGuardDestination.ApplicationResult)
            }

            is HomeScreenEvent.RecordClick -> {
                openVerificationResult(event.recordId)
            }

            is HomeScreenEvent.TabSelect -> {
                navigator.navigateToTab(event.tab)
            }

            HomeScreenEvent.RetryClick -> {
                Unit
            }

            HomeScreenEvent.BackClick -> {
                navigator.goBack()
            }

            HomeScreenEvent.HomeClick -> {
                navigator.returnToHome()
            }

            HomeScreenEvent.NoticeListClick -> {
                navigator.push(EcoGuardDestination.Notices)
            }
        }
    }

    fun onAreaEvent(event: AreaScreenEvent) {
        when (event) {
            is AreaScreenEvent.FloorSelect -> demoState.selectedFloor = event.floor
            is AreaScreenEvent.TabSelect -> navigator.navigateToTab(event.tab)
            AreaScreenEvent.CameraClick -> startVerification()
            AreaScreenEvent.RetryClick -> Unit
        }
    }

    fun onActivityEvent(event: ActivityScreenEvent) {
        when (event) {
            ActivityScreenEvent.MonthSelectorClick -> {
                navigator.push(EcoGuardDestination.MonthPicker)
            }

            is ActivityScreenEvent.MonthApply -> {
                demoState.selectedYear = event.year
                demoState.selectedMonth = event.month
                navigator.goBack()
            }

            ActivityScreenEvent.MonthPickerDismiss -> {
                navigator.goBack()
            }

            is ActivityScreenEvent.RecordClick -> {
                openVerificationResult(event.recordId)
            }

            ActivityScreenEvent.StartVerificationClick, ActivityScreenEvent.CameraClick -> {
                startVerification()
            }

            is ActivityScreenEvent.TabSelect -> {
                navigator.navigateToTab(event.tab)
            }

            ActivityScreenEvent.RetryClick -> {
                Unit
            }
        }
    }

    fun onMenuEvent(event: MenuScreenEvent) {
        when (event) {
            MenuScreenEvent.AreaClick -> {
                navigator.navigateToTab(EcoBottomTab.AREA)
            }

            MenuScreenEvent.ApplicationResultClick -> {
                navigator.push(
                    if (demoState.isApplicationCompleted) {
                        EcoGuardDestination.ApplicationResult
                    } else {
                        EcoGuardDestination.Recruitment
                    },
                )
            }

            is MenuScreenEvent.CleaningNotificationToggle -> {
                demoState.isCleaningNotificationEnabled = event.isEnabled
            }

            MenuScreenEvent.AppealHistoryClick -> {
                navigator.push(EcoGuardDestination.AppealHistory)
            }

            MenuScreenEvent.NoticeClick -> {
                navigator.push(EcoGuardDestination.Notices)
            }

            MenuScreenEvent.HelpClick -> {
                Unit
            }

            MenuScreenEvent.LogoutClick -> {
                navigator.push(EcoGuardDestination.LogoutConfirm)
            }

            MenuScreenEvent.WithdrawClick -> {
                navigator.push(EcoGuardDestination.WithdrawConfirm)
            }

            MenuScreenEvent.LogoutConfirm, MenuScreenEvent.WithdrawConfirm -> {
                onSignOut()
            }

            is MenuScreenEvent.TabSelect -> {
                navigator.navigateToTab(event.tab)
            }

            MenuScreenEvent.CameraClick -> {
                startVerification()
            }
        }
    }

    fun onRecruitmentEvent(event: RecruitmentScreenEvent) {
        when (event) {
            is RecruitmentScreenEvent.MotivationChange -> {
                demoState.motivation = event.motivation
            }

            RecruitmentScreenEvent.ApplyClick -> {
                if (!demoState.isApplicationCompleted) {
                    navigator.push(EcoGuardDestination.Application)
                } else {
                    navigator.push(EcoGuardDestination.ApplicationResult)
                }
            }

            RecruitmentScreenEvent.BackClick -> {
                navigator.goBack()
            }

            RecruitmentScreenEvent.HomeClick -> {
                navigator.returnToHome()
            }

            RecruitmentScreenEvent.RetryClick -> {
                Unit
            }
        }
    }

    fun onApplicationEvent(event: RecruitmentScreenEvent) {
        if (event == RecruitmentScreenEvent.ApplyClick) {
            if (demoState.motivation.isNotBlank() && !demoState.isApplicationCompleted) {
                demoState.isApplicationCompleted = true
                navigator.replaceFlow(EcoGuardDestination.Recruitment, EcoGuardDestination.ApplicationResult)
            }
        } else {
            onRecruitmentEvent(event)
        }
    }

    fun onVerificationEvent(
        event: VerificationScreenEvent,
        appealRecordId: Long? = null,
    ) {
        when (event) {
            VerificationScreenEvent.BackClick, VerificationScreenEvent.CloseClick -> {
                navigator.goBack()
            }

            VerificationScreenEvent.TakePhotoClick -> {
                navigator.push(EcoGuardDestination.CameraCapture())
            }

            VerificationScreenEvent.ShutterClick -> {
                if (appealRecordId != null) {
                    demoState.addAppealPhoto(appealRecordId)
                    navigator.goBack()
                } else {
                    navigator.push(EcoGuardDestination.PhotoConfirm)
                }
            }

            VerificationScreenEvent.FlipCameraClick -> {
                demoState.isFrontCamera = !demoState.isFrontCamera
            }

            VerificationScreenEvent.RetakeClick -> {
                navigator.push(EcoGuardDestination.CameraCapture())
            }

            VerificationScreenEvent.SendClick,
            VerificationScreenEvent.ResendSamePhotoClick,
            VerificationScreenEvent.CheckUploadStatusClick,
            -> {
                if (!demoState.isTodaySubmitted) {
                    demoState.isTodaySubmitted = true
                    navigator.replaceFlow(EcoGuardDestination.CameraGuide, EcoGuardDestination.SubmissionCompleted)
                }
            }

            VerificationScreenEvent.HomeClick -> {
                navigator.returnToHome()
            }
        }
    }

    fun onVerificationResultEvent(
        recordId: Long,
        event: VerificationResultScreenEvent,
    ) {
        when (event) {
            VerificationResultScreenEvent.BackClick, VerificationResultScreenEvent.ConfirmClick -> navigator.goBack()
            VerificationResultScreenEvent.HomeClick -> navigator.returnToHome()
            VerificationResultScreenEvent.AppealClick -> openAppeal(recordId)
            VerificationResultScreenEvent.RetryClick -> Unit
        }
    }

    private fun openAppeal(recordId: Long) {
        demoState.beginAppeal(recordId)
        navigator.push(EcoGuardDestination.AppealForm(recordId))
    }

    fun onAppealEvent(
        recordId: Long,
        event: AppealScreenEvent,
    ) {
        when (event) {
            is AppealScreenEvent.ContentChange -> {
                demoState.updateAppealContent(recordId, event.appealContent)
            }

            AppealScreenEvent.AddPhotoClick -> {
                if (demoState.appealDraft(recordId).photoCount <
                    3
                ) {
                    navigator.push(EcoGuardDestination.CameraCapture(appealRecordId = recordId))
                }
            }

            AppealScreenEvent.SubmitClick, AppealScreenEvent.RetrySendClick -> {
                if (demoState.appealDraft(recordId).content.isNotBlank()) {
                    demoState.submitAppeal(recordId)
                    navigator.replaceFlow(
                        EcoGuardDestination.AppealForm(recordId),
                        EcoGuardDestination.AppealSubmitted(recordId),
                    )
                }
            }

            AppealScreenEvent.EditContentClick, AppealScreenEvent.ResubmitClick -> {
                openAppeal(recordId)
            }

            AppealScreenEvent.ActivityRecordClick -> {
                navigator.navigateToTab(EcoBottomTab.ACTIVITY)
            }

            is AppealScreenEvent.HistoryItemClick -> {
                navigator.push(EcoGuardDestination.AppealResult(event.appealId))
            }

            AppealScreenEvent.HomeClick -> {
                navigator.returnToHome()
            }

            AppealScreenEvent.BackClick -> {
                navigator.goBack()
            }
        }
    }

    fun onNoticeEvent(event: NoticeScreenEvent) {
        when (event) {
            NoticeScreenEvent.BackClick -> navigator.goBack()
            NoticeScreenEvent.RetryClick -> Unit
            NoticeScreenEvent.ReturnToListClick -> navigator.push(EcoGuardDestination.Notices)
            is NoticeScreenEvent.NoticeClick -> navigator.push(EcoGuardDestination.NoticeDetail(event.noticeId))
        }
    }

    fun openNotification(notificationId: Long): Boolean {
        val notification = DemoData.notifications.find { it.notificationId == notificationId } ?: return false
        val destination =
            when (val target = notification.target) {
                NotificationTarget.Recruitment -> {
                    EcoGuardDestination.Recruitment
                }

                NotificationTarget.ApplicationResult -> {
                    EcoGuardDestination.ApplicationResult
                }

                is NotificationTarget.Notice -> {
                    if (DemoData.notices.any { it.noticeId == target.noticeId }) {
                        EcoGuardDestination.NoticeDetail(
                            target.noticeId,
                        )
                    } else {
                        null
                    }
                }

                is NotificationTarget.Verification -> {
                    if (demoState.records.any {
                            it.recordId == target.recordId
                        }
                    ) {
                        EcoGuardDestination.VerificationResult(target.recordId)
                    } else {
                        null
                    }
                }

                is NotificationTarget.Appeal -> {
                    if (target.appealId in
                        listOf(1L, 2L, 3L)
                    ) {
                        EcoGuardDestination.AppealResult(target.appealId)
                    } else {
                        null
                    }
                }
            } ?: return false
        if (notificationId !in
            demoState.readNotificationIds
        ) {
            demoState.readNotificationIds =
                demoState.readNotificationIds + notificationId
        }
        navigator.push(destination)
        return true
    }
}
