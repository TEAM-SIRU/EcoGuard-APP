package com.nativelap.ecoguard

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.nativelap.ecoguard.feature.activity.view.ActivityScreen
import com.nativelap.ecoguard.feature.activity.viewmodel.ActivityScreenEvent
import com.nativelap.ecoguard.feature.activity.viewmodel.ActivityUiState
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ActivityEmptyInteractionTest {
    @get:Rule val composeRule = createComposeRule()

    @Test fun emptyActionRemainsReachableBelowLargeStatistics() {
        var action: ActivityScreenEvent? = null
        composeRule.setContent {
            EcoGuardTheme {
                ActivityScreen(ActivityUiState.Content(2026, 9, 0, 0, 0, 0, emptyList()), { action = it })
            }
        }
        composeRule
            .onNodeWithText("청소 인증하러 가기")
            .performScrollTo()
            .assertIsDisplayed()
            .performClick()
        composeRule.runOnIdle { assertEquals(ActivityScreenEvent.StartVerificationClick, action) }
    }
}
