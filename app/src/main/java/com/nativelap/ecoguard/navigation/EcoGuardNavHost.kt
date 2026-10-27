package com.nativelap.ecoguard.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.DialogProperties
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.nativelap.ecoguard.core.presentation.LocalFeatureSessionId
import com.nativelap.ecoguard.core.presentation.clearFeatureViewModelsForSession
import com.nativelap.ecoguard.core.presentation.findActivity
import com.nativelap.ecoguard.core.presentation.rememberActivityViewModel
import com.nativelap.ecoguard.feature.activity.view.ActivityRoute
import com.nativelap.ecoguard.feature.activity.viewmodel.ActivityScreenEvent
import com.nativelap.ecoguard.feature.appeal.view.AppealFormRoute
import com.nativelap.ecoguard.feature.appeal.view.AppealHistoryRoute
import com.nativelap.ecoguard.feature.appeal.viewmodel.AppealFormUiState
import com.nativelap.ecoguard.feature.appeal.viewmodel.AppealScreenEvent
import com.nativelap.ecoguard.feature.area.view.AreaRoute
import com.nativelap.ecoguard.feature.area.viewmodel.AreaScreenEvent
import com.nativelap.ecoguard.feature.auth.presentation.AuthUiState
import com.nativelap.ecoguard.feature.home.view.HomeRoute
import com.nativelap.ecoguard.feature.home.viewmodel.HomeScreenEvent
import com.nativelap.ecoguard.feature.home.viewmodel.HomeSectionUiModel
import com.nativelap.ecoguard.feature.home.viewmodel.HomeUiState
import com.nativelap.ecoguard.feature.home.viewmodel.HomeViewModel
import com.nativelap.ecoguard.feature.home.viewmodel.TodayCleaningStatus
import com.nativelap.ecoguard.feature.login.view.LoginRoute
import com.nativelap.ecoguard.feature.login.view.TeacherAccountGuideRoute
import com.nativelap.ecoguard.feature.login.viewmodel.LoginUiState
import com.nativelap.ecoguard.feature.menu.view.MenuRoute
import com.nativelap.ecoguard.feature.menu.viewmodel.MenuScreenEvent
import com.nativelap.ecoguard.feature.menu.viewmodel.MenuUiState
import com.nativelap.ecoguard.feature.menu.viewmodel.MenuViewModel
import com.nativelap.ecoguard.feature.notice.view.NoticeDetailRoute
import com.nativelap.ecoguard.feature.notice.view.NoticeListRoute
import com.nativelap.ecoguard.feature.notice.viewmodel.NoticeScreenEvent
import com.nativelap.ecoguard.feature.notification.view.NotificationRoute
import com.nativelap.ecoguard.feature.notification.viewmodel.NotificationUiState
import com.nativelap.ecoguard.feature.recruitment.view.ApplicationResultScreen
import com.nativelap.ecoguard.feature.recruitment.view.ApplicationRoute
import com.nativelap.ecoguard.feature.recruitment.view.RecruitmentRoute
import com.nativelap.ecoguard.feature.recruitment.viewmodel.ApplicationResultUiState
import com.nativelap.ecoguard.feature.recruitment.viewmodel.ApplicationUiState
import com.nativelap.ecoguard.feature.recruitment.viewmodel.RecruitmentScreenEvent
import com.nativelap.ecoguard.feature.verification.view.CameraCaptureRoute
import com.nativelap.ecoguard.feature.verification.view.CameraGuideScreen
import com.nativelap.ecoguard.feature.verification.view.PhotoConfirmScreen
import com.nativelap.ecoguard.feature.verification.view.SubmissionCompletedScreen
import com.nativelap.ecoguard.feature.verification.view.UploadFailedScreen
import com.nativelap.ecoguard.feature.verification.view.VerificationDetailRoute
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationResultScreenEvent
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationScreenEvent
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationSubmissionUiState
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationSubmissionViewModel
import com.nativelap.ecoguard.ui.component.EcoBottomTab
import java.io.File
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun EcoGuardNavHost(
    authUiState: AuthUiState,
    onLoginClick: () -> Unit,
    onSignOut: () -> Unit,
    onWithdrawalSuccess: () -> Unit = {},
) {
    val sessionOwner = LocalContext.current.findActivity()
    var previousSessionId by remember { mutableStateOf(authUiState.sessionId) }
    LaunchedEffect(authUiState.sessionId) {
        val endedSessionId = previousSessionId
        if (endedSessionId != null && endedSessionId != authUiState.sessionId) {
            clearFeatureViewModelsForSession(sessionOwner, endedSessionId)
        }
        previousSessionId = authUiState.sessionId
    }
    key(authUiState.sessionId ?: "signed-out") {
        if (authUiState.sessionId != null) {
            CompositionLocalProvider(LocalFeatureSessionId provides authUiState.sessionId) {
                EcoGuardMainNavDisplay(onSignOut, onWithdrawalSuccess = onWithdrawalSuccess)
            }
        } else {
            val backStack = rememberNavBackStack(EcoGuardDestination.Login)
            NavDisplay(
                backStack = backStack,
                onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
                entryProvider =
                    androidx.navigation3.runtime.entryProvider {
                        entry<EcoGuardDestination.Login> {
                            LoginRoute(
                                LoginUiState(
                                    authUiState.isLoggingIn,
                                    authUiState.hasLoginFailed,
                                    authUiState.hasSessionClearFailed,
                                    authUiState.hasServerLogoutFailed,
                                    authUiState.hasRetryableLogoutPending,
                                ),
                                onLoginClick,
                                onTeacherAccountGuideClick = {
                                    backStack.add(EcoGuardDestination.TeacherAccountGuide)
                                },
                            )
                        }
                        entry<EcoGuardDestination.TeacherAccountGuide> {
                            TeacherAccountGuideRoute(onLogoutClick = {
                                if (backStack.size > 1) backStack.removeLastOrNull()
                            })
                        }
                    },
            )
        }
    }
}

@Composable
internal fun EcoGuardMainNavDisplay(
    onSignOut: () -> Unit,
    navigationState: EcoGuardMainNavigationState = rememberEcoGuardMainNavigationState(),
    onWithdrawalSuccess: () -> Unit = {},
) {
    val navigator = remember(navigationState) { EcoGuardMainNavigator(navigationState) }
    val homeViewModel = rememberActivityViewModel<HomeViewModel>()
    val home by homeViewModel.uiState.collectAsState()
    val menu by rememberActivityViewModel<MenuViewModel>().uiState.collectAsState()
    val submitViewModel = rememberActivityViewModel<VerificationSubmissionViewModel>()
    val upload by submitViewModel.uiState.collectAsState()

    fun camera() {
        val today = ((home as? HomeUiState.Content)?.section as? HomeSectionUiModel.Cleaning)?.todayCleaning ?: return
        val status = today.status as? TodayCleaningStatus.NotSubmitted ?: return
        navigator.push(
            EcoGuardDestination.CameraGuide(
                today.areaId,
                today.assignedAreaName,
                today.cleaningStartTime,
                today.cleaningEndTime,
                status.timeUntilDeadline,
            ),
        )
    }
    val provider: (NavKey) -> NavEntry<NavKey> = { key ->
        val destination = key as EcoGuardDestination
        val metadata =
            when (destination) {
                EcoGuardDestination.MonthPicker,
                EcoGuardDestination.LogoutConfirm,
                EcoGuardDestination.WithdrawConfirm,
                -> {
                    DialogSceneStrategy
                        .dialog(
                            DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false),
                        )
                }

                is EcoGuardDestination.VerificationBlocked -> {
                    EcoGuardBottomSheetSceneStrategy.bottomSheet()
                }

                else -> {
                    emptyMap()
                }
            }
        NavEntry(key, metadata = metadata) {
            when (destination) {
                EcoGuardDestination.Home -> {
                    HomeRoute(onEvent = { e ->
                        when (e) {
                            HomeScreenEvent.NotificationClick -> {
                                navigator.push(EcoGuardDestination.Notifications)
                            }

                            HomeScreenEvent.NoticeListClick, HomeScreenEvent.NoticeDetailClick -> {
                                navigator.push(EcoGuardDestination.Notices)
                            }

                            HomeScreenEvent.CleaningVerificationClick -> {
                                camera()
                            }

                            HomeScreenEvent.ActivityRecordClick, HomeScreenEvent.AllRecordsClick -> {
                                navigator.navigateToTab(EcoBottomTab.ACTIVITY)
                            }

                            is HomeScreenEvent.AppealClick -> {
                                e.verificationId?.let { verificationId ->
                                    navigator.push(
                                        EcoGuardDestination.AppealForm(
                                            verificationId,
                                            e.rejectionReason,
                                            e.verificationDeadlineTime,
                                        ),
                                    )
                                } ?: navigator.push(EcoGuardDestination.AppealHistory)
                            }

                            HomeScreenEvent.RecruitmentClick, HomeScreenEvent.ApplicationResultClick -> {
                                navigator.push(EcoGuardDestination.Recruitment)
                            }

                            is HomeScreenEvent.TabSelect -> {
                                navigator.navigateToTab(e.tab)
                            }

                            is HomeScreenEvent.RecordClick -> {
                                navigator.push(EcoGuardDestination.VerificationDetail(e.recordId))
                            }

                            HomeScreenEvent.BackClick -> {
                                navigator.goBack()
                            }

                            HomeScreenEvent.HomeClick -> {
                                navigator.returnToHome()
                            }

                            else -> {
                                Unit
                            }
                        }
                    })
                }

                EcoGuardDestination.Area -> {
                    AreaRoute(onEvent = { e ->
                        when (e) {
                            is AreaScreenEvent.TabSelect -> navigator.navigateToTab(e.tab)
                            AreaScreenEvent.CameraClick -> camera()
                            else -> Unit
                        }
                    })
                }

                EcoGuardDestination.Activity -> {
                    ActivityRoute(onEvent = { e ->
                        when (e) {
                            is ActivityScreenEvent.TabSelect -> {
                                navigator.navigateToTab(e.tab)
                            }

                            ActivityScreenEvent.CameraClick, ActivityScreenEvent.StartVerificationClick -> {
                                camera()
                            }

                            is ActivityScreenEvent.RecordClick -> {
                                navigator.push(
                                    EcoGuardDestination.VerificationDetail(e.recordId),
                                )
                            }

                            ActivityScreenEvent.MonthSelectorClick -> {
                                navigator.push(EcoGuardDestination.MonthPicker)
                            }

                            else -> {
                                Unit
                            }
                        }
                    })
                }

                EcoGuardDestination.Menu -> {
                    MenuRoute(onEvent = { e ->
                        when (e) {
                            is MenuScreenEvent.TabSelect -> navigator.navigateToTab(e.tab)
                            MenuScreenEvent.AreaClick -> navigator.navigateToTab(EcoBottomTab.AREA)
                            MenuScreenEvent.ApplicationResultClick -> navigator.push(EcoGuardDestination.Recruitment)
                            MenuScreenEvent.AppealHistoryClick -> navigator.push(EcoGuardDestination.AppealHistory)
                            MenuScreenEvent.NoticeClick -> navigator.push(EcoGuardDestination.Notices)
                            MenuScreenEvent.LogoutConfirm -> onSignOut()
                            MenuScreenEvent.WithdrawalSucceeded -> onWithdrawalSuccess()
                            MenuScreenEvent.CameraClick -> camera()
                            else -> Unit
                        }
                    })
                }

                EcoGuardDestination.Notifications -> {
                    NotificationRoute(
                        NotificationUiState.NotIntegrated,
                        navigationState.selectedTab,
                        navigator::goBack,
                        navigator::navigateToTab,
                        ::camera,
                        { false },
                    )
                }

                EcoGuardDestination.Notices -> {
                    NoticeListRoute(onEvent = { e ->
                        when (e) {
                            NoticeScreenEvent.BackClick -> {
                                navigator.goBack()
                            }

                            is NoticeScreenEvent.NoticeClick -> {
                                navigator.push(
                                    EcoGuardDestination.NoticeDetail(e.noticeId),
                                )
                            }

                            else -> {
                                Unit
                            }
                        }
                    })
                }

                is EcoGuardDestination.NoticeDetail -> {
                    NoticeDetailRoute(destination.noticeId, onEvent = { e ->
                        if (e == NoticeScreenEvent.BackClick || e == NoticeScreenEvent.ReturnToListClick) {
                            navigator.goBack()
                        } else if (e is NoticeScreenEvent.NoticeClick) {
                            navigator.push(EcoGuardDestination.NoticeDetail(e.noticeId))
                        }
                    })
                }

                EcoGuardDestination.Recruitment -> {
                    RecruitmentRoute(onEvent = { e ->
                        if (e ==
                            RecruitmentScreenEvent.BackClick
                        ) {
                            navigator.goBack()
                        } else if (e ==
                            RecruitmentScreenEvent.HomeClick
                        ) {
                            navigator.returnToHome()
                        }
                    }, onOpenApplication = {
                        id,
                        grade,
                        cls,
                        ->
                        navigator.push(EcoGuardDestination.Application(id, grade, cls))
                    })
                }

                is EcoGuardDestination.Application -> {
                    ApplicationRoute(
                        destination.recruitmentId,
                        ApplicationUiState(
                            destination.grade,
                            destination.classNo,
                            (menu as? MenuUiState.Content)?.studentNumber.orEmpty(),
                            (menu as? MenuUiState.Content)?.userName.orEmpty(),
                        ),
                        onEvent = { e ->
                            if (e ==
                                RecruitmentScreenEvent.BackClick
                            ) {
                                navigator.goBack()
                            } else if (e ==
                                RecruitmentScreenEvent.HomeClick
                            ) {
                                navigator.returnToHome()
                            }
                        },
                        onSubmitted = { r ->
                            navigator.push(EcoGuardDestination.ApplicationResult(r.applicationOrder, r.appliedDateTime))
                        },
                    )
                }

                is EcoGuardDestination.ApplicationResult -> {
                    ApplicationResultScreen(
                        ApplicationResultUiState.Completed(
                            destination.applicationOrder,
                            destination.appliedDateTime,
                        ),
                        onEvent = {
                            navigator.returnToHome()
                        },
                    )
                }

                is EcoGuardDestination.CameraGuide -> {
                    CameraGuideScreen(destination.areaName, onEvent = { e ->
                        if (e ==
                            VerificationScreenEvent.TakePhotoClick
                        ) {
                            navigator.push(
                                EcoGuardDestination.CameraCapture(
                                    destination.areaId,
                                    destination.areaName,
                                    destination.cleaningStartTime,
                                    destination.cleaningEndTime,
                                    destination.timeRemaining,
                                    destination.cleaningEndTime,
                                ),
                            )
                        } else if (e ==
                            VerificationScreenEvent.BackClick
                        ) {
                            navigator.goBack()
                        }
                    })
                }

                is EcoGuardDestination.CameraCapture -> {
                    CameraCaptureRoute(
                        destination.areaName,
                        destination.timeRemaining,
                        destination.deadlineTime,
                        onEvent = {
                            if (it == VerificationScreenEvent.BackClick ||
                                it == VerificationScreenEvent.CloseClick
                            ) {
                                navigator.goBack()
                            }
                        },
                        onPhotoCaptured = { p ->
                            navigator.push(
                                EcoGuardDestination.PhotoConfirm(
                                    destination.areaId,
                                    destination.areaName,
                                    destination.cleaningStartTime,
                                    destination.cleaningEndTime,
                                    java.time.LocalTime
                                        .now()
                                        .format(DateTimeFormatter.ofPattern("HH:mm")),
                                    destination.deadlineTime,
                                    p,
                                ),
                            )
                        },
                    )
                }

                is EcoGuardDestination.PhotoConfirm -> {
                    PhotoConfirmScreen(
                        destination.cleaningStartTime,
                        destination.cleaningEndTime,
                        destination.areaName,
                        destination.capturedTime,
                        onEvent = { e ->
                            when (e) {
                                VerificationScreenEvent.SendClick -> {
                                    submitViewModel.submit(destination.photoPath, destination.areaId)
                                }

                                VerificationScreenEvent.RetakeClick -> {
                                    File(destination.photoPath).delete()
                                    submitViewModel.reset()
                                    navigator.goBack()
                                }

                                VerificationScreenEvent.BackClick -> {
                                    File(destination.photoPath).delete()
                                    submitViewModel.reset()
                                    navigator.goBack()
                                }

                                else -> {
                                    Unit
                                }
                            }
                        },
                        photoPath = destination.photoPath,
                        isSending = upload is VerificationSubmissionUiState.Submitting,
                    )
                    when (val result = upload) {
                        is VerificationSubmissionUiState.Completed -> {
                            androidx.compose.runtime.LaunchedEffect(result) {
                                navigator.push(
                                    EcoGuardDestination.SubmissionCompleted(
                                        destination.areaName,
                                        result.submittedAt,
                                        result.verificationId,
                                        destination.photoPath,
                                    ),
                                )
                            }
                        }

                        VerificationSubmissionUiState.Failed -> {
                            androidx.compose.runtime.LaunchedEffect(
                                destination.photoPath,
                            ) {
                                navigator.push(
                                    EcoGuardDestination.UploadFailed(
                                        destination.areaId,
                                        destination.areaName,
                                        destination.photoPath,
                                        destination.deadlineTime,
                                    ),
                                )
                            }
                        }

                        else -> {
                            Unit
                        }
                    }
                }

                is EcoGuardDestination.UploadFailed -> {
                    UploadFailedScreen(destination.deadlineTime, onEvent = { e ->
                        when (e) {
                            VerificationScreenEvent.ResendSamePhotoClick -> {
                                submitViewModel.submit(destination.photoPath, destination.areaId)
                            }

                            VerificationScreenEvent.HomeClick -> {
                                File(destination.photoPath).delete()
                                submitViewModel.reset()
                                navigator.returnToHome()
                            }

                            VerificationScreenEvent.BackClick -> {
                                File(destination.photoPath).delete()
                                submitViewModel.reset()
                                navigator.returnToHome()
                            }

                            else -> {
                                Unit
                            }
                        }
                    })
                    if (upload is VerificationSubmissionUiState.Completed) {
                        androidx.compose.runtime.LaunchedEffect(upload) {
                            val result = upload as VerificationSubmissionUiState.Completed
                            navigator.push(
                                EcoGuardDestination.SubmissionCompleted(
                                    destination.areaName,
                                    result.submittedAt,
                                    result.verificationId,
                                    destination.photoPath,
                                ),
                            )
                        }
                    }
                }

                is EcoGuardDestination.SubmissionCompleted -> {
                    SubmissionCompletedScreen(destination.areaName, destination.submittedAt.toDisplayTime(), onEvent = {
                        File(destination.photoPath).delete()
                        submitViewModel.reset()
                        navigator.returnToHome()
                    })
                }

                EcoGuardDestination.AppealHistory -> {
                    AppealHistoryRoute(onEvent = { e ->
                        when (e) {
                            AppealScreenEvent.BackClick -> navigator.goBack()
                            AppealScreenEvent.HomeClick -> navigator.returnToHome()
                            AppealScreenEvent.ActivityRecordClick -> navigator.navigateToTab(EcoBottomTab.ACTIVITY)
                            else -> Unit
                        }
                    })
                }

                is EcoGuardDestination.AppealForm -> {
                    AppealFormRoute(
                        destination.recordId,
                        AppealFormUiState(
                            destination.rejectionReason,
                            "",
                            0,
                            3,
                            500,
                            destination.verificationDeadlineTime,
                        ),
                        onEvent = { if (it == AppealScreenEvent.BackClick) navigator.goBack() },
                        onSubmitted = {
                            navigator.goBack()
                            navigator.push(EcoGuardDestination.AppealHistory)
                        },
                    )
                }

                is EcoGuardDestination.AppealSubmitted, is EcoGuardDestination.AppealResult -> {
                    navigator.goBack()
                }

                is EcoGuardDestination.VerificationDetail -> {
                    VerificationDetailRoute(
                        verificationId = destination.recordId,
                        onEvent = { event ->
                            when (event) {
                                VerificationResultScreenEvent.BackClick -> {
                                    navigator
                                        .goBack()
                                }

                                VerificationResultScreenEvent.HomeClick -> {
                                    navigator
                                        .returnToHome()
                                }

                                else -> {
                                    Unit
                                }
                            }
                        },
                    )
                }

                EcoGuardDestination.Login,
                EcoGuardDestination.TeacherAccountGuide,
                EcoGuardDestination.VerificationTimeEnded,
                is EcoGuardDestination.VerificationResult,
                is EcoGuardDestination.VerificationDayOff,
                is EcoGuardDestination.VerificationBlocked,
                is EcoGuardDestination.AppealSendFailed,
                EcoGuardDestination.MonthPicker,
                EcoGuardDestination.LogoutConfirm,
                EcoGuardDestination.WithdrawConfirm,
                -> {
                    navigator.goBack()
                }
            }
        }
    }
    BackHandler(navigationState.currentBackStack.size == 1 && navigationState.canGoBack) { navigator.goBack() }
    NavDisplay(
        entries = navigationState.toEntries(provider),
        onBack = navigator::goBack,
        sceneStrategies = listOf(DialogSceneStrategy(), EcoGuardBottomSheetSceneStrategy()),
    )
}

private fun String.toDisplayTime(): String =
    runCatching {
        DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault()).format(Instant.parse(this))
    }.getOrElse { substringAfter('T').take(5).ifBlank { this } }
