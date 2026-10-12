package com.nativelap.ecoguard

import android.graphics.Bitmap
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.test.platform.app.InstrumentationRegistry
import com.nativelap.ecoguard.feature.home.view.HomePreviewFixtures
import com.nativelap.ecoguard.feature.home.view.HomeScreen
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import org.junit.Rule
import org.junit.Test
import java.io.File

class LongContentInteractionTest {
    @get:Rule val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test fun homeFinalRecordRemainsReachableBelowCards() {
        composeRule.activityRule.scenario.onActivity { it.enableEdgeToEdge() }
        composeRule.setContent { EcoGuardTheme { HomeScreen(HomePreviewFixtures.notSubmittedHome, {}) } }
        composeRule.onNodeWithText("9월 22일(화) 08:04").performScrollTo().assertIsDisplayed()
        composeRule.waitForIdle()
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val bitmap = checkNotNull(instrumentation.uiAutomation.takeScreenshot())
        val output = File(instrumentation.targetContext.getExternalFilesDir(null), "redesign").apply { mkdirs() }
        File(
            output,
            "HomeScrolledToFinalRecord.png",
        ).outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        bitmap.recycle()
    }
}
