package com.nativelap.ecoguard

import android.graphics.Bitmap
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.nativelap.ecoguard.feature.demo.viewmodel.DemoAppState
import com.nativelap.ecoguard.feature.demo.viewmodel.DemoData
import com.nativelap.ecoguard.feature.notice.view.NoticeDetailScreen
import com.nativelap.ecoguard.feature.notice.view.NoticeListScreen
import com.nativelap.ecoguard.feature.notice.viewmodel.NoticeDetailUiState
import com.nativelap.ecoguard.feature.notice.viewmodel.NoticeUiState
import com.nativelap.ecoguard.feature.notification.view.NotificationScreen
import com.nativelap.ecoguard.feature.notification.viewmodel.NotificationUiState
import com.nativelap.ecoguard.navigation.EcoGuardDestination
import com.nativelap.ecoguard.navigation.EcoGuardMainNavDisplay
import com.nativelap.ecoguard.navigation.EcoGuardMainNavigator
import com.nativelap.ecoguard.navigation.rememberEcoGuardMainNavigationState
import com.nativelap.ecoguard.ui.component.EcoBottomTab
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import org.junit.Rule
import org.junit.Test
import java.io.File

class NavigationRenderTest {
    @get:Rule val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun rendersNavigationDestinationsAndOverlayWindows() {
        val destinations =
            listOf(
                EcoGuardDestination.Notifications,
                EcoGuardDestination.Notices,
                EcoGuardDestination.NoticeDetail(1),
                EcoGuardDestination.NoticeDetail(999),
                EcoGuardDestination.Recruitment,
                EcoGuardDestination.Application,
                EcoGuardDestination.ApplicationResult,
                EcoGuardDestination.CameraGuide,
                EcoGuardDestination.CameraCapture(),
                EcoGuardDestination.PhotoConfirm,
                EcoGuardDestination.SubmissionCompleted,
                EcoGuardDestination.UploadFailed,
                EcoGuardDestination.VerificationTimeEnded,
                EcoGuardDestination.VerificationResult(1),
                EcoGuardDestination.VerificationResult(2),
                EcoGuardDestination.VerificationResult(4),
                EcoGuardDestination.VerificationDetail(3),
                EcoGuardDestination.VerificationDayOff(false),
                EcoGuardDestination.VerificationDayOff(true),
                EcoGuardDestination.VerificationBlocked(EcoGuardDestination.BlockReason.OUTSIDE_TIME),
                EcoGuardDestination.VerificationBlocked(EcoGuardDestination.BlockReason.ALREADY_SUBMITTED),
                EcoGuardDestination.VerificationBlocked(EcoGuardDestination.BlockReason.CAMERA_PERMISSION),
                EcoGuardDestination.AppealForm(2),
                EcoGuardDestination.AppealSubmitted(2),
                EcoGuardDestination.AppealHistory,
                EcoGuardDestination.AppealResult(1),
                EcoGuardDestination.AppealResult(2),
                EcoGuardDestination.AppealResult(3),
                EcoGuardDestination.AppealSendFailed(2),
                EcoGuardDestination.MonthPicker,
                EcoGuardDestination.LogoutConfirm,
                EcoGuardDestination.WithdrawConfirm,
            )
        var selectedDestination by mutableStateOf(destinations.first())
        var settledDestination: EcoGuardDestination? = null
        composeRule.activityRule.scenario.onActivity { it.enableEdgeToEdge() }
        composeRule.setContent {
            key(selectedDestination) {
                val navigationState = rememberEcoGuardMainNavigationState()
                val snapshot =
                    androidx.compose.runtime.remember {
                        DemoAppState().apply {
                            isApplicationCompleted = true
                            updateAppealContent(2, "사진에서 잘린 구역도 청소했어요. 확인해 주세요.")
                            beginAppeal(2)
                        }
                    }
                LaunchedEffect(selectedDestination) {
                    EcoGuardMainNavigator(navigationState).push(selectedDestination)
                    settledDestination = selectedDestination
                }
                EcoGuardTheme { EcoGuardMainNavDisplay({}, navigationState, snapshot) }
            }
        }
        destinations.forEachIndexed { index, destination ->
            composeRule.runOnIdle { selectedDestination = destination }
            composeRule.waitUntil(10000) { settledDestination == destination }
            composeRule.waitForIdle()
            capture("destination-${index.toString().padStart(2, '0')}")
        }
    }

    @Test
    fun rendersNewScreenStatesAndLongContent() {
        var selectedScenario by mutableIntStateOf(0)
        composeRule.activityRule.scenario.onActivity { it.enableEdgeToEdge() }
        composeRule.setContent {
            key(selectedScenario) {
                EcoGuardTheme {
                    when (selectedScenario) {
                        0 -> {
                            NotificationScreen(NotificationUiState.Loading, EcoBottomTab.HOME, {})
                        }

                        1 -> {
                            NotificationScreen(NotificationUiState.Empty, EcoBottomTab.HOME, {})
                        }

                        2 -> {
                            NotificationScreen(NotificationUiState.Failed, EcoBottomTab.HOME, {})
                        }

                        3 -> {
                            NoticeListScreen(NoticeUiState.Loading, {})
                        }

                        4 -> {
                            NoticeListScreen(NoticeUiState.Empty, {})
                        }

                        5 -> {
                            NoticeListScreen(NoticeUiState.Failed, {})
                        }

                        6 -> {
                            NoticeDetailScreen(NoticeDetailUiState.Loading, {})
                        }

                        7 -> {
                            NoticeDetailScreen(NoticeDetailUiState.Failed, {})
                        }

                        8 -> {
                            NoticeDetailScreen(
                                NoticeDetailUiState.Content(
                                    DemoData.notices.first().copy(
                                        title = "긴 공지 제목도 화면 폭과 글자 크기에 맞춰 여러 줄로 표시해요",
                                        body = "긴 본문도 스크롤해서 읽을 수 있어요.\n\n".repeat(30),
                                    ),
                                ),
                                {},
                            )
                        }
                    }
                }
            }
        }
        (0..8).forEach { scenario ->
            composeRule.runOnIdle { selectedScenario = scenario }
            composeRule.waitForIdle()
            capture("new-state-$scenario")
        }
    }

    private fun capture(name: String) {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val configuration = context.resources.configuration
        val prefix = "${configuration.screenWidthDp}dp-${configuration.fontScale}"
        val folder = File(context.getExternalFilesDir(null), "navigation-screenshots/$prefix").apply { mkdirs() }
        val automation = InstrumentationRegistry.getInstrumentation().uiAutomation
        automation.waitForIdle(500, 5000)
        val bitmap = automation.takeScreenshot()
        requireNotNull(bitmap) { "Screenshot unavailable" }
        File(folder, "$name.png").outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        bitmap.recycle()
    }
}
