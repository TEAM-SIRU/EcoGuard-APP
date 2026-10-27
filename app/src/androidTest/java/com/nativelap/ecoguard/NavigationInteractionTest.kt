package com.nativelap.ecoguard

import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso
import com.nativelap.ecoguard.feature.auth.presentation.AuthUiState
import com.nativelap.ecoguard.navigation.EcoGuardNavHost
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import org.junit.Rule
import org.junit.Test

class NavigationInteractionTest {
    @get:Rule val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun loginScreenKeepsTeacherGuideAndReturnsToSignIn() {
        composeRule.activityRule.scenario.onActivity { it.enableEdgeToEdge() }
        var clicks by mutableIntStateOf(0)
        composeRule.setContent {
            EcoGuardTheme {
                EcoGuardNavHost(AuthUiState(), onLoginClick = { clicks += 1 }, onSignOut = {})
            }
        }
        composeRule.onNodeWithText("DataGSM으로 로그인").assertIsDisplayed()
        composeRule.onNodeWithText("선생님 계정 안내").assertIsDisplayed().performClick()
        composeRule.onNodeWithText("선생님은 웹에서 이용해 주세요").assertIsDisplayed()
        Espresso.pressBack()
        composeRule.onNodeWithText("DataGSM으로 로그인").assertIsDisplayed()
        composeRule.runOnIdle { assert(clicks == 0) }
    }
}
