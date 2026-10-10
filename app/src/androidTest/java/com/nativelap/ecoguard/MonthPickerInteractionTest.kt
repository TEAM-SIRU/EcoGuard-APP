package com.nativelap.ecoguard

import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.nativelap.ecoguard.feature.activity.view.MonthPickerDialog
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class MonthPickerInteractionTest {
    @get:Rule val composeRule = createComposeRule()

    @Test fun returningToLatestYearClampsFutureMonthBeforeApply() {
        var applied: Pair<Int, Int>? = null
        composeRule.setContent {
            EcoGuardTheme {
                MonthPickerDialog(
                    2026,
                    9,
                    {},
                    { year, month -> applied = year to month },
                    latestYear = 2026,
                    latestMonth = 9,
                )
            }
        }
        composeRule.onNodeWithContentDescription("다음 연도").assertIsNotEnabled()
        composeRule.onNodeWithContentDescription("이전 연도").performClick()
        composeRule.onNodeWithText("12월").performClick()
        composeRule.onNodeWithContentDescription("다음 연도").performClick()
        composeRule.onNodeWithText("9월").assertIsSelected()
        composeRule.onNodeWithText("12월").assertIsNotEnabled()
        composeRule.onNodeWithText("적용").performClick()
        composeRule.runOnIdle { assertEquals(2026 to 9, applied) }
    }
}
