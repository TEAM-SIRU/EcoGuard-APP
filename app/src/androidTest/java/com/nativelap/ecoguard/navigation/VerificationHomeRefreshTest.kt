package com.nativelap.ecoguard.navigation

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.ViewModelProvider
import androidx.test.espresso.Espresso
import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.core.presentation.findActivity
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.ApplicationStatus
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.ApplicationStatusValue
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.ApplyCommand
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.ApplyResult
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.CurrentRecruitment
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.MyApplicationStatus
import com.nativelap.ecoguard.feature.recruitment.module.domain.repository.RecruitmentRepository
import com.nativelap.ecoguard.feature.recruitment.module.domain.usecase.ApplyForRecruitmentUseCase
import com.nativelap.ecoguard.feature.recruitment.view.ApplicationRoute
import com.nativelap.ecoguard.feature.recruitment.viewmodel.ApplicationUiState
import com.nativelap.ecoguard.feature.recruitment.viewmodel.ApplicationViewModel
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationSubmissionUiState
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import kotlinx.coroutines.CompletableDeferred
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class VerificationHomeRefreshTest {
    @get:Rule val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun completedSubmissionRefreshesHomeBeforeSystemBack() {
        var upload by mutableStateOf<VerificationSubmissionUiState>(VerificationSubmissionUiState.Idle)
        var onSuccessScreen by mutableStateOf(true)
        var refreshCount = 0
        composeRule.setContent {
            val tracker = ViewModelProvider(LocalContext.current.findActivity())[HomeRefreshTracker::class.java]
            RefreshHomeAfterVerification(upload, tracker::claimVerificationRefresh) { refreshCount++ }
            BackHandler(onSuccessScreen) { onSuccessScreen = false }
            Text(if (onSuccessScreen) "완료" else "홈")
        }

        composeRule.runOnIdle {
            upload = VerificationSubmissionUiState.Completed(123L, "2026-01-01T00:00:00")
        }
        composeRule.waitForIdle()
        Espresso.pressBack()
        composeRule.onNodeWithText("홈").assertIsDisplayed()
        composeRule.runOnIdle { assertEquals(1, refreshCount) }
    }

    @Test
    fun failedVerificationDoesNotRefreshHomeAfterRouteReentry() {
        var refreshCount = 0
        var routeVisible by mutableStateOf(true)
        composeRule.setContent {
            if (routeVisible) {
                val tracker = ViewModelProvider(LocalContext.current.findActivity())[HomeRefreshTracker::class.java]
                RefreshHomeAfterVerification(
                    VerificationSubmissionUiState.Failed,
                    tracker::claimVerificationRefresh,
                ) { refreshCount++ }
            }
        }

        composeRule.waitForIdle()
        composeRule.runOnIdle { routeVisible = false }
        composeRule.runOnIdle { routeVisible = true }
        composeRule.waitForIdle()
        composeRule.runOnIdle { assertEquals(0, refreshCount) }
    }

    @Test
    fun pendingApplicationRefreshesHomeAfterRouteLeaves() {
        val response = CompletableDeferred<AppResult<ApplyResult>>()
        val repository =
            object : RecruitmentRepository {
                override suspend fun apply(command: ApplyCommand): AppResult<ApplyResult> = response.await()

                override suspend fun getCurrentRecruitment(): AppResult<CurrentRecruitment> = error("unused")

                override suspend fun getMyApplicationStatus(): AppResult<MyApplicationStatus> = error("unused")
            }
        val viewModel = ApplicationViewModel(ApplyForRecruitmentUseCase(repository))
        val initialState = ApplicationUiState(1, 1, "01", "학생")
        var routeVisible by mutableStateOf(true)
        var refreshCount = 0
        composeRule.setContent {
            val count by viewModel.successfulSubmissionCount.collectAsState()
            val tracker = ViewModelProvider(LocalContext.current.findActivity())[HomeRefreshTracker::class.java]
            RefreshHomeAfterApplication(count, tracker::claimApplicationRefresh) { refreshCount++ }
            EcoGuardTheme {
                if (routeVisible) {
                    ApplicationRoute(1, initialState, viewModel = viewModel)
                } else {
                    Text("홈")
                }
            }
        }
        composeRule.waitForIdle()
        composeRule.runOnIdle {
            viewModel.updateMotivation("신청합니다")
            viewModel.submitApplication()
            routeVisible = false
        }
        composeRule.onNodeWithText("홈").assertIsDisplayed()
        composeRule.runOnIdle {
            response.complete(
                AppResult.Success(
                    ApplyResult(
                        applicationId = 1,
                        status = ApplicationStatusValue(ApplicationStatus.PENDING, "PENDING"),
                        order = 1,
                        studentNumber = "01",
                        name = "학생",
                        appliedAt = "2026-01-01T00:00:00",
                    ),
                ),
            )
        }
        composeRule.waitUntil(5000) { refreshCount == 1 }
        composeRule.runOnIdle { assertEquals(1, refreshCount) }
    }

    @Test
    fun applicationSuccessRefreshesOnceAcrossActivityRecreation() {
        var refreshCount = 0
        val successfulSubmissionCount = 1L
        composeRule.setContent {
            val tracker = ViewModelProvider(LocalContext.current.findActivity())[HomeRefreshTracker::class.java]
            RefreshHomeAfterApplication(successfulSubmissionCount, tracker::claimApplicationRefresh) { refreshCount++ }
        }
        composeRule.waitForIdle()
        composeRule.runOnIdle { assertEquals(1, refreshCount) }

        composeRule.activityRule.scenario.recreate()
        composeRule.setContent {
            val tracker = ViewModelProvider(LocalContext.current.findActivity())[HomeRefreshTracker::class.java]
            RefreshHomeAfterApplication(successfulSubmissionCount, tracker::claimApplicationRefresh) { refreshCount++ }
        }
        composeRule.waitForIdle()
        composeRule.runOnIdle { assertEquals(1, refreshCount) }
    }

    @Test
    fun verificationSuccessRefreshesOnceAcrossActivityRecreation() {
        var refreshCount = 0
        val completed = VerificationSubmissionUiState.Completed(123L, "2026-01-01T00:00:00")
        composeRule.setContent {
            val tracker = ViewModelProvider(LocalContext.current.findActivity())[HomeRefreshTracker::class.java]
            RefreshHomeAfterVerification(completed, tracker::claimVerificationRefresh) { refreshCount++ }
        }
        composeRule.waitForIdle()
        composeRule.runOnIdle { assertEquals(1, refreshCount) }

        composeRule.activityRule.scenario.recreate()
        composeRule.setContent {
            val tracker = ViewModelProvider(LocalContext.current.findActivity())[HomeRefreshTracker::class.java]
            RefreshHomeAfterVerification(completed, tracker::claimVerificationRefresh) { refreshCount++ }
        }
        composeRule.waitForIdle()
        composeRule.runOnIdle { assertEquals(1, refreshCount) }
    }
}
