package com.nativelap.ecoguard

import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.click
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTouchInput
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.test.platform.app.InstrumentationRegistry
import com.nativelap.ecoguard.ui.component.EcoConfirmDialog
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class DialogInteractionTest {
    @get:Rule val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test fun blankInteriorDoesNotDismissButOutsideDoes() {
        var dismissals = 0
        composeRule.setContent {
            val shown = remember { mutableStateOf(true) }
            EcoGuardTheme {
                Text("배경")
                if (shown.value) {
                    EcoConfirmDialog("확인", "설명", "취소", "적용", {
                        dismissals++
                        shown.value = false
                    }, {})
                }
            }
        }
        composeRule.onNodeWithText("확인").performClick()
        composeRule.onNodeWithText("적용").assertIsDisplayed()
        composeRule.runOnIdle { assertEquals(0, dismissals) }
        composeRule.onNode(isDialog()).performTouchInput { click(Offset(2f, center.y)) }
        composeRule.onNodeWithText("적용").assertDoesNotExist()
        composeRule.runOnIdle { assertEquals(1, dismissals) }
    }

    @Test fun longDialogCanScrollToAndConfirm() {
        var confirmed = 0
        composeRule.setContent {
            EcoGuardTheme {
                EcoConfirmDialog("확인", List(30) { "긴 안내 문구입니다." }.joinToString("\n"), "취소", "적용", {}, { confirmed++ })
            }
        }
        composeRule
            .onNodeWithText("적용")
            .performScrollTo()
            .assertIsDisplayed()
            .performClick()
        composeRule.runOnIdle { assertEquals(1, confirmed) }
        val bounds = composeRule.onNodeWithText("적용").fetchSemanticsNode().boundsInWindow
        val insets =
            checkNotNull(ViewCompat.getRootWindowInsets(composeRule.activity.window.decorView))
                .getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
        val height =
            composeRule.activity.windowManager.maximumWindowMetrics.bounds
                .height()
        assertTrue("버튼이 시스템 영역과 겹침: $bounds / $insets", bounds.bottom <= height - insets.bottom)
        assertTrue("버튼이 컷아웃과 겹침", bounds.top >= insets.top)
    }

    @Test fun backDismissesFullscreenDialog() {
        var dismissals = 0
        composeRule.setContent {
            val shown = remember { mutableStateOf(true) }
            EcoGuardTheme {
                Text("배경")
                if (shown.value) {
                    EcoConfirmDialog("확인", "설명", "취소", "적용", {
                        dismissals++
                        shown.value = false
                    }, {})
                }
            }
        }
        composeRule.waitForIdle()
        InstrumentationRegistry.getInstrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_BACK)
        composeRule.onNodeWithText("적용").assertDoesNotExist()
        composeRule.runOnIdle { assertEquals(1, dismissals) }
    }
}
