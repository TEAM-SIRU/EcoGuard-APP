package com.nativelap.ecoguard

import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.espresso.Espresso
import com.nativelap.ecoguard.feature.verification.view.PhotoConfirmScreen
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationScreenEvent
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class VerificationPhotoInteractionTest {
    @get:Rule val composeRule = createComposeRule()

    @Test
    fun uploadingPhotoPreventsRetakeAndBackNavigation() {
        val events = mutableListOf<VerificationScreenEvent>()
        composeRule.setContent {
            EcoGuardTheme {
                PhotoConfirmScreen(
                    cleaningStartTime = "08:00",
                    cleaningEndTime = "08:30",
                    areaName = "2학년 복도",
                    capturedTime = "08:10",
                    onEvent = events::add,
                    photoPath = null,
                    isSending = true,
                )
            }
        }

        composeRule.onNodeWithText("다시 찍기").assertIsNotEnabled()
        composeRule.onNodeWithContentDescription("뒤로 가기").assertIsNotEnabled()
        Espresso.pressBack()
        composeRule.runOnIdle { assertTrue(events.isEmpty()) }
    }
}
