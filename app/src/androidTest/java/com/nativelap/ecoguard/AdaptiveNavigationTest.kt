package com.nativelap.ecoguard

import android.graphics.Bitmap
import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.core.view.WindowCompat
import androidx.test.platform.app.InstrumentationRegistry
import com.nativelap.ecoguard.feature.home.view.HomePreviewFixtures
import com.nativelap.ecoguard.feature.home.view.HomeScreen
import com.nativelap.ecoguard.feature.home.viewmodel.HomeScreenEvent
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.io.File

class AdaptiveNavigationTest {
    @get:Rule val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun showsExactlyOneNavigationPatternAndKeepsCameraActionAtCurrentWidth() {
        composeRule.activityRule.scenario.onActivity {
            it.enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
                navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            )
        }
        var cameraClicks = 0
        composeRule.setContent {
            EcoGuardTheme {
                HomeScreen(
                    uiState = HomePreviewFixtures.cleaningHome(HomePreviewFixtures.todayStatuses.first()),
                    onEvent = { event ->
                        if (event == HomeScreenEvent.CleaningVerificationClick) cameraClicks++
                    },
                )
            }
        }

        val isExpanded =
            composeRule.activity.resources.configuration.screenWidthDp >= 840
        if (isExpanded) {
            composeRule.onNodeWithTag("eco-navigation-rail").assertIsDisplayed()
            composeRule.onNodeWithTag("eco-bottom-navigation").assertDoesNotExist()
        } else {
            composeRule.onNodeWithTag("eco-bottom-navigation").assertIsDisplayed()
            composeRule.onNodeWithTag("eco-navigation-rail").assertDoesNotExist()
        }
        composeRule.onNodeWithText("홈").assertIsSelected()
        composeRule.onNodeWithContentDescription("청소 인증 촬영").assertExists()
        assertLightSystemBarIcons()
        captureAdaptiveNavigation(isExpanded)

        composeRule.onNodeWithContentDescription("청소 인증 촬영").performClick()
        composeRule.runOnIdle { assertTrue(cameraClicks == 1) }
    }

    private fun assertLightSystemBarIcons() {
        composeRule.activityRule.scenario.onActivity { activity ->
            val controller = WindowCompat.getInsetsController(activity.window, activity.window.decorView)
            assertTrue(controller.isAppearanceLightStatusBars)
            assertTrue(controller.isAppearanceLightNavigationBars)
        }
    }

    private fun captureAdaptiveNavigation(isExpanded: Boolean) {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val width = context.resources.configuration.screenWidthDp
        val directory = File(context.getExternalFilesDir(null), "adaptive-navigation").apply { mkdirs() }
        val bitmap = InstrumentationRegistry.getInstrumentation().uiAutomation.takeScreenshot()
        File(directory, "navigation-${width}dp-${if (isExpanded) "rail" else "bottom"}.png")
            .outputStream()
            .use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        bitmap.recycle()
    }
}
