package com.nativelap.ecoguard

import android.graphics.Bitmap
import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.tooling.ComposableInvoker
import androidx.core.view.WindowCompat
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.io.File

/** 전 화면 Preview를 기기에서 렌더링해 상태별 시각 검증용 원본을 저장한다. */
class RedesignRenderTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun renderEveryScreenPreview() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val only = InstrumentationRegistry.getArguments().getString("preview")
        val previews = SCREEN_PREVIEWS.filter { only == null || it.second == only }
        composeRule.activityRule.scenario.onActivity { activity ->
            activity.enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
                navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            )
        }
        val selected = mutableStateOf(previews.first())
        composeRule.setContent {
            key(selected.value) { RenderPreview(selected.value.first, selected.value.second) }
        }
        val output = File(instrumentation.targetContext.getExternalFilesDir(null), "redesign").apply { mkdirs() }
        previews.forEach { preview ->
            composeRule.runOnIdle { selected.value = preview }
            composeRule.waitForIdle()
            composeRule.mainClock.advanceTimeBy(1000)
            composeRule.waitForIdle()
            instrumentation.waitForIdleSync()
            Thread.sleep(250)
            composeRule.activityRule.scenario.onActivity { activity ->
                assertEquals(
                    "시스템바 아이콘과 화면 대비: ${preview.second}",
                    preview.second != "CameraCaptureScreenPreview",
                    WindowCompat
                        .getInsetsController(
                            activity.window,
                            activity.window.decorView,
                        ).isAppearanceLightStatusBars,
                )
            }
            val bitmap = checkNotNull(instrumentation.uiAutomation.takeScreenshot())
            File(
                output,
                "${preview.second}.png",
            ).outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
            bitmap.recycle()
        }
    }

    @Composable
    @OptIn(androidx.compose.runtime.InternalComposeApi::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
    private fun RenderPreview(
        className: String,
        methodName: String,
    ) {
        ComposableInvoker.invokeComposable(className, methodName, currentComposer)
    }

    companion object {
        private val SCREEN_PREVIEWS =
            listOf(
                Pair(
                    "com.nativelap.ecoguard.feature.activity.view.ActivityLoadingScreenKt",
                    "ActivityLoadingScreenPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.activity.view.ActivityScreenKt",
                    "ActivityScreenPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.activity.view.ActivityScreenKt",
                    "ActivityScreenEmptyPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.activity.view.ActivityScreenKt",
                    "ActivityScreenLoadFailedPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.activity.view.MonthPickerDialogKt",
                    "MonthPickerDialogPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.appeal.view.AppealFormScreenKt",
                    "AppealFormScreenPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.appeal.view.AppealFormScreenKt",
                    "AppealFormScreenFilledPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.appeal.view.AppealHistoryScreenKt",
                    "AppealHistoryScreenPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.appeal.view.AppealResultScreenKt",
                    "AppealResultApprovedPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.appeal.view.AppealResultScreenKt",
                    "AppealResultRejectedPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.appeal.view.AppealResultScreenKt",
                    "AppealSendFailedPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.appeal.view.AppealSubmittedScreenKt",
                    "AppealSubmittedScreenPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.area.view.AreaLoadingScreenKt",
                    "AreaLoadingScreenPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.area.view.AreaScreenKt",
                    "AreaScreenContentPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.area.view.AreaScreenKt",
                    "AreaScreenNotAssignedPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.area.view.AreaScreenKt",
                    "AreaScreenLoadFailedPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.home.view.HomeActivityRemovedScreenKt",
                    "HomeActivityRemovedScreenPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.home.view.HomeLoadFailedScreenKt",
                    "HomeLoadFailedScreenPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.home.view.HomeLoadingScreenKt",
                    "HomeLoadingScreenPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.home.view.HomeScreenKt",
                    "HomeScreenNotSubmittedPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.home.view.HomeScreenKt",
                    "HomeScreenRejectedPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.home.view.HomeScreenKt",
                    "HomeScreenRecruitingPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.home.view.HomeScreenKt",
                    "HomeScreenWaitingAssignmentPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.home.view.HomeScreenKt",
                    "HomeOutsideTimePreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.home.view.HomeScreenKt",
                    "HomeAiReviewingPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.home.view.HomeScreenKt",
                    "HomeTeacherReviewingPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.home.view.HomeScreenKt",
                    "HomeApprovedPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.login.view.LoginScreenKt",
                    "LoginScreenPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.login.view.LoginScreenKt",
                    "LoginScreenLoadingPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.login.view.LoginScreenKt",
                    "LoginScreenFailedPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.login.view.SplashScreenKt",
                    "SplashScreenPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.login.view.TeacherAccountGuideScreenKt",
                    "TeacherAccountGuideScreenPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.menu.view.MenuRouteKt",
                    "MenuRouteLogoutDialogPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.menu.view.MenuRouteKt",
                    "MenuRouteWithdrawDialogPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.menu.view.MenuScreenKt",
                    "MenuScreenPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.notice.view.NoticeLoadFailedScreenKt",
                    "NoticeLoadFailedScreenPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.recruitment.view.ApplicationResultScreenKt",
                    "ApplicationResultScreenCompletedPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.recruitment.view.ApplicationResultScreenKt",
                    "ApplicationResultScreenFilledPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.recruitment.view.ApplicationScreenKt",
                    "ApplicationScreenPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.recruitment.view.ApplicationScreenKt",
                    "ApplicationFilledPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.recruitment.view.ApplicationScreenKt",
                    "ApplicationApplyingPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.recruitment.view.RecruitmentLoadFailedScreenKt",
                    "RecruitmentLoadFailedScreenPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.recruitment.view.RecruitmentLoadingScreenKt",
                    "RecruitmentLoadingScreenPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.recruitment.view.RecruitmentScreenKt",
                    "RecruitmentScreenOpenPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.recruitment.view.RecruitmentScreenKt",
                    "RecruitmentScreenClosedPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.recruitment.view.RecruitmentScreenKt",
                    "RecruitmentScreenAppliedPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.verification.view.CameraCaptureScreenKt",
                    "CameraCaptureScreenPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.verification.view.CameraGuideScreenKt",
                    "CameraGuideScreenPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.verification.view.PhotoConfirmScreenKt",
                    "PhotoConfirmScreenPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.verification.view.SubmissionCompletedScreenKt",
                    "SubmissionCompletedScreenPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.verification.view.UploadFailedScreenKt",
                    "UploadFailedScreenPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.verification.view.VerificationBlockedSheetKt",
                    "VerificationBlockedOutsideTimePreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.verification.view.VerificationBlockedSheetKt",
                    "VerificationBlockedAlreadySubmittedPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.verification.view.VerificationBlockedSheetKt",
                    "VerificationBlockedCameraPermissionPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.verification.view.VerificationDayOffScreenKt",
                    "VerificationDayOffWeekendPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.verification.view.VerificationDayOffScreenKt",
                    "VerificationDayOffVacationPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.verification.view.VerificationDetailScreenKt",
                    "VerificationDetailScreenPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.verification.view.VerificationResultScreenKt",
                    "VerificationResultApprovedPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.verification.view.VerificationResultScreenKt",
                    "VerificationResultRejectedPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.verification.view.VerificationResultScreenKt",
                    "VerificationResultTeacherReviewingPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.verification.view.VerificationResultScreenKt",
                    "VerificationResultFailedPreview",
                ),
                Pair(
                    "com.nativelap.ecoguard.feature.verification.view.VerificationTimeEndedScreenKt",
                    "VerificationTimeEndedScreenPreview",
                ),
            )
    }
}
