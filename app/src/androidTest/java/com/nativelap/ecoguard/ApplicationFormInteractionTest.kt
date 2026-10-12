package com.nativelap.ecoguard

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import com.nativelap.ecoguard.feature.recruitment.view.ApplicationRoute
import com.nativelap.ecoguard.feature.recruitment.viewmodel.ApplicationUiState
import com.nativelap.ecoguard.feature.recruitment.viewmodel.RecruitmentScreenEvent
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ApplicationFormInteractionTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun motivationIsRequiredLimitedAndRestored() {
        val restoration = StateRestorationTester(composeRule)
        var applyCount = 0
        restoration.setContent {
            EcoGuardTheme {
                ApplicationRoute(ApplicationUiState(2, 3, 5, "최민준")) { event ->
                    if (event == RecruitmentScreenEvent.ApplyClick) applyCount++
                }
            }
        }
        composeRule.onAllNodes(hasSetTextAction()).assertCountEquals(1)
        composeRule.onNodeWithText("신청하기").assertIsNotEnabled()
        composeRule.onNode(hasSetTextAction()).performTextInput("  \n")
        composeRule.onNodeWithText("신청하기").assertIsNotEnabled()
        composeRule.onNode(hasSetTextAction()).performTextReplacement("가".repeat(201))
        composeRule.onNodeWithText("200/200").assertExists()
        composeRule.onNodeWithText("신청하기").assertIsEnabled()
        val unicodeBoundary = "가".repeat(199) + "😀"
        composeRule.onNodeWithContentDescription("신청 동기").performTextReplacement(unicodeBoundary + "나")
        composeRule.onNodeWithText(unicodeBoundary).assertExists()
        restoration.emulateSavedInstanceStateRestore()
        composeRule.onNodeWithText("200/200").assertExists()
        composeRule.onNodeWithText("신청하기").performClick()
        composeRule.runOnIdle { assertEquals(1, applyCount) }
    }

    @Test
    fun externalMotivationUpdateReplacesDraftForSameStudent() {
        val state = mutableStateOf(ApplicationUiState(2, 3, 5, "최민준"))
        composeRule.setContent { EcoGuardTheme { ApplicationRoute(state.value) } }
        composeRule.onNodeWithContentDescription("신청 동기").performTextInput("로컬 입력")
        composeRule.runOnIdle { state.value = state.value.copy(motivation = "복원된 신청 동기") }
        composeRule.onNodeWithText("복원된 신청 동기").assertExists()
        composeRule.runOnIdle { state.value = state.value.copy(motivation = "") }
        composeRule.onNodeWithText("신청하기").assertIsNotEnabled()
    }

    @Test
    fun applyingDisablesInputAndSubmission() {
        composeRule.setContent {
            EcoGuardTheme {
                ApplicationRoute(ApplicationUiState(2, 3, 5, "최민준", "학교를 깨끗하게 만들고 싶어요", isApplying = true))
            }
        }
        composeRule.onNode(hasText("학교를 깨끗하게 만들고 싶어요")).assertIsNotEnabled()
        composeRule.onNodeWithText("신청하기").assertIsNotEnabled()
    }
}
