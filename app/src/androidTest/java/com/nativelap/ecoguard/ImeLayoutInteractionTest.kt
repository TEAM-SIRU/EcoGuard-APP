package com.nativelap.ecoguard

import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.nativelap.ecoguard.feature.appeal.view.AppealFormScreen
import com.nativelap.ecoguard.feature.appeal.viewmodel.AppealFormUiState
import com.nativelap.ecoguard.feature.recruitment.view.ApplicationScreen
import com.nativelap.ecoguard.feature.recruitment.viewmodel.ApplicationUiState
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ImeLayoutInteractionTest {
    @get:Rule val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test fun applicationActionStaysAboveKeyboard() = verifyIme(false)

    @Test fun appealActionStaysAboveKeyboard() = verifyIme(true)

    private fun verifyIme(isAppeal: Boolean) {
        var imeBottom = 0
        composeRule.activityRule.scenario.onActivity { it.enableEdgeToEdge() }
        composeRule.setContent {
            val bottom = WindowInsets.ime.getBottom(LocalDensity.current)
            SideEffect { imeBottom = bottom }
            EcoGuardTheme {
                if (isAppeal) {
                    AppealFormScreen(AppealFormUiState("반려 사유", "확인해 주세요", 0, 1, 200, "08:10"), {})
                } else {
                    ApplicationScreen(ApplicationUiState(2, 3, "05", "최민준", "학교를 깨끗하게 만들고 싶어요"), {})
                }
            }
        }
        val inputLabel = if (isAppeal) "내용" else "신청 동기"
        composeRule.onNodeWithContentDescription(inputLabel).performScrollTo().performClick()
        composeRule.waitUntil(10000) { imeBottom > 0 }
        composeRule.waitForIdle()
        val action =
            composeRule
                .onNodeWithText(
                    if (isAppeal) "이의신청 보내기" else "신청하기",
                ).fetchSemanticsNode()
                .boundsInWindow
        val screenHeight =
            composeRule.activity.windowManager.maximumWindowMetrics.bounds
                .height()
        assertTrue(
            "CTA가 키보드에 가려짐: $action / IME $imeBottom / window $screenHeight",
            action.bottom <= screenHeight - imeBottom + 2,
        )
    }
}
