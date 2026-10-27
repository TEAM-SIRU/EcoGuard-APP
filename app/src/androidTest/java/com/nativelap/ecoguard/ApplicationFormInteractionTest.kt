package com.nativelap.ecoguard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.test.espresso.Espresso
import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.ApplicationStatus
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.ApplicationStatusValue
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.ApplyResult
import com.nativelap.ecoguard.feature.recruitment.module.domain.repository.RecruitmentRepository
import com.nativelap.ecoguard.feature.recruitment.module.domain.usecase.ApplyForRecruitmentUseCase
import com.nativelap.ecoguard.feature.recruitment.view.ApplicationRoute
import com.nativelap.ecoguard.feature.recruitment.view.ApplicationScreen
import com.nativelap.ecoguard.feature.recruitment.viewmodel.ApplicationResultUiState
import com.nativelap.ecoguard.feature.recruitment.viewmodel.ApplicationUiState
import com.nativelap.ecoguard.feature.recruitment.viewmodel.ApplicationViewModel
import com.nativelap.ecoguard.feature.recruitment.viewmodel.RecruitmentScreenEvent
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import kotlinx.coroutines.CompletableDeferred
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ApplicationFormInteractionTest {
    @get:Rule val composeRule = createComposeRule()

    @Test
    fun motivationIsRequiredLimitedAndRestored() {
        val restoration = StateRestorationTester(composeRule)
        var applyCount = 0
        var latestMotivation = ""
        restoration.setContent {
            var motivation by rememberSaveable { mutableStateOf("") }
            EcoGuardTheme {
                ApplicationScreen(ApplicationUiState(2, 3, "05", "민준", motivation), onEvent = { event ->
                    when (event) {
                        is RecruitmentScreenEvent.MotivationChange -> {
                            latestMotivation = event.motivation
                            motivation = event.motivation
                        }

                        RecruitmentScreenEvent.ApplyClick -> {
                            applyCount++
                        }

                        else -> {
                            Unit
                        }
                    }
                })
            }
        }
        composeRule.onAllNodes(hasSetTextAction()).assertCountEquals(1)
        composeRule.onNodeWithText("신청하기").assertIsNotEnabled()
        composeRule.onNode(hasSetTextAction()).performTextInput("  \n")
        composeRule.onNodeWithText("신청하기").assertIsNotEnabled()
        composeRule.onNode(hasSetTextAction()).performTextReplacement("가".repeat(201))
        composeRule.runOnIdle { assertEquals("가".repeat(200), latestMotivation) }
        composeRule.onNode(hasText("200/200", substring = true)).assertExists()
        composeRule.onNodeWithText("신청하기").assertIsEnabled()
        val unicodeBoundary = "가".repeat(199) + "😀"
        composeRule.onNodeWithContentDescription("신청 동기").performTextReplacement(unicodeBoundary + "나")
        composeRule.runOnIdle { assertEquals(unicodeBoundary, latestMotivation) }
        restoration.emulateSavedInstanceStateRestore()
        composeRule.runOnIdle { assertEquals(unicodeBoundary, latestMotivation) }
        composeRule.onNodeWithContentDescription("신청 동기").assertTextContains(unicodeBoundary)
        composeRule.onNodeWithText("신청하기").performClick()
        composeRule.runOnIdle { assertEquals(1, applyCount) }
    }

    @Test
    fun applyingStateDisablesInputAndSubmission() {
        var backClicks = 0
        composeRule.setContent {
            EcoGuardTheme {
                ApplicationScreen(
                    ApplicationUiState(2, 3, "05", "민준", "신청 동기", isApplying = true),
                    onEvent = { if (it == RecruitmentScreenEvent.BackClick) backClicks++ },
                )
            }
        }
        composeRule.onNodeWithContentDescription("신청 동기").assertIsNotEnabled()
        composeRule.onNodeWithText("신청하기").assertIsNotEnabled()
        composeRule.onNodeWithContentDescription("뒤로 가기").assertIsNotEnabled()
        Espresso.pressBack()
        composeRule.runOnIdle { assertEquals(0, backClicks) }
    }

    @Test
    fun completedApplicationSurvivesRouteRecreation() {
        val resultGate = CompletableDeferred<AppResult<ApplyResult>>()
        val viewModel =
            ApplicationViewModel(
                ApplyForRecruitmentUseCase(FakeRecruitmentRepository(resultGate)),
            )
        val submittedResults = mutableListOf<ApplicationResultUiState.Completed>()
        var isRouteVisible by mutableStateOf(true)

        composeRule.setContent {
            if (isRouteVisible) {
                EcoGuardTheme {
                    ApplicationRoute(
                        recruitmentId = 10,
                        uiState = ApplicationUiState(2, 3, "05", "민준"),
                        viewModel = viewModel,
                        onSubmitted = submittedResults::add,
                    )
                }
            }
        }
        composeRule.waitUntil { viewModel.uiState.value != null }
        composeRule.onNodeWithContentDescription("신청 동기").performTextInput("깨끗한 교실을 만들겠습니다.")
        composeRule.onNodeWithText("신청하기").performClick()
        composeRule.waitUntil { viewModel.uiState.value?.isApplying == true }
        composeRule.runOnIdle { isRouteVisible = false }

        resultGate.complete(
            AppResult.Success(
                ApplyResult(
                    applicationId = 100,
                    status = ApplicationStatusValue(ApplicationStatus.PENDING, "PENDING"),
                    order = 1,
                    studentNumber = "05",
                    name = "민준",
                    appliedAt = "2026-10-25T12:30:00",
                ),
            ),
        )
        composeRule.waitUntil { viewModel.submissionResult.value != null }
        composeRule.runOnIdle { isRouteVisible = true }
        composeRule.waitUntil { submittedResults.size == 1 }

        assertEquals(1, submittedResults.single().applicationOrder)
    }

    private class FakeRecruitmentRepository(
        private val applyResult: CompletableDeferred<AppResult<ApplyResult>>,
    ) : RecruitmentRepository {
        override suspend fun apply(
            command: com.nativelap.ecoguard.feature.recruitment.module.domain.model.ApplyCommand,
        ) = applyResult.await()

        override suspend fun getCurrentRecruitment() = error("Not used in this test")

        override suspend fun getMyApplicationStatus() = error("Not used in this test")
    }
}
