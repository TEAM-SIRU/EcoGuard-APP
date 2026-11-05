package com.nativelap.ecoguard.navigation

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.nativelap.ecoguard.feature.home.view.HomePreviewFixtures
import com.nativelap.ecoguard.feature.home.view.HomeScreen
import com.nativelap.ecoguard.feature.home.viewmodel.HomeScreenEvent
import com.nativelap.ecoguard.feature.home.viewmodel.HomeSectionUiModel
import com.nativelap.ecoguard.feature.home.viewmodel.TodayCleaningStatus
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class HomeServerIdentityTest {
    @get:Rule val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun appealUsesTodayVerificationInsteadOfOlderRejection() {
        val original = HomePreviewFixtures.cleaningHome(TodayCleaningStatus.Rejected("사유"))
        val section = original.section as HomeSectionUiModel.Cleaning
        val state =
            original.copy(
                section = section.copy(todayCleaning = section.todayCleaning.copy(verificationId = 101)),
            )
        var event: HomeScreenEvent? = null
        composeRule.setContent { EcoGuardTheme { HomeScreen(state, onEvent = { event = it }) } }
        composeRule.onNodeWithText("이의신청하기").performScrollTo().performClick()
        composeRule.runOnIdle { assertEquals(101L, (event as HomeScreenEvent.AppealClick).verificationId) }
    }

    @Test
    fun noTodayIdDisablesAppealInsteadOfUsingOlderRecord() {
        val state = HomePreviewFixtures.cleaningHome(TodayCleaningStatus.Rejected("사유"))
        composeRule.setContent { EcoGuardTheme { HomeScreen(state, onEvent = {}) } }
        composeRule.onNodeWithText("이의신청하기").performScrollTo().assertIsNotEnabled()
    }

    @Test
    fun noticeAndSubmittedDetailEventsCarryServerIds() {
        val original = HomePreviewFixtures.cleaningHome(TodayCleaningStatus.AiReviewing("08:04"), hasNotice = true)
        val section = original.section as HomeSectionUiModel.Cleaning
        val state =
            original.copy(
                notice = original.notice?.copy(noticeId = 31),
                section = section.copy(todayCleaning = section.todayCleaning.copy(verificationId = 101)),
            )
        var event: HomeScreenEvent? = null
        composeRule.setContent { EcoGuardTheme { HomeScreen(state, onEvent = { event = it }) } }
        composeRule.onNodeWithText("자세히 보기").performClick()
        composeRule.runOnIdle { assertEquals(HomeScreenEvent.NoticeDetailClick(31), event) }
        composeRule.onNodeWithText("인증 상세 보기").performScrollTo().performClick()
        composeRule.runOnIdle { assertEquals(HomeScreenEvent.SubmittedPhotoClick(101), event) }
    }
}
