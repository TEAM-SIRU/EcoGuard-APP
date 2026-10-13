package com.nativelap.ecoguard

import android.graphics.Bitmap
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.hasScrollToNodeAction
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.test.espresso.Espresso
import androidx.test.platform.app.InstrumentationRegistry
import com.nativelap.ecoguard.navigation.EcoGuardNavHost
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import org.junit.Rule
import org.junit.Test
import java.io.File

class NavigationInteractionTest {
    @get:Rule val composeRule = createAndroidComposeRule<ComponentActivity>()

    private fun startLoginScreen(restoration: StateRestorationTester? = null) {
        composeRule.activityRule.scenario.onActivity { it.enableEdgeToEdge() }
        val content: @androidx.compose.runtime.Composable () -> Unit = {
            EcoGuardTheme { EcoGuardNavHost() }
        }
        if (restoration == null) composeRule.setContent(content) else restoration.setContent(content)
    }

    private fun startApp(restoration: StateRestorationTester? = null) {
        startLoginScreen(restoration)
        composeRule.onNodeWithText("DataGSM으로 로그인").assertIsDisplayed().performClick()
    }

    @Test
    fun teacherAccountGuideOpensAndReturnsToLogin() {
        startLoginScreen()
        capture("navigation-login-teacher-guide")
        composeRule.onNodeWithText("선생님 계정 안내").assertIsDisplayed().performClick()
        composeRule.onNodeWithText("선생님은 웹에서 이용해 주세요").assertIsDisplayed()
        Espresso.pressBack()
        composeRule.onNodeWithText("DataGSM으로 로그인").assertIsDisplayed()

        composeRule.onNodeWithText("선생님 계정 안내").performClick()
        composeRule.onNodeWithText("로그아웃").performClick()
        composeRule.onNodeWithText("DataGSM으로 로그인").assertIsDisplayed()
    }

    @Test
    fun tabsAndSystemBackReturnToHomeWithoutLoggingOut() {
        startApp()
        composeRule.onNodeWithText("구역").performClick()
        composeRule.onNodeWithText("구역").assertIsSelected()
        composeRule.onNodeWithText("기록").performClick()
        composeRule.onNodeWithText("활동 기록").assertIsDisplayed()
        composeRule.onNodeWithText("마이페이지").performClick()
        Espresso.pressBack()
        composeRule.onNodeWithText("홈").assertIsSelected()
        composeRule.onNodeWithContentDescription("알림").assertIsDisplayed()
    }

    @Test
    fun notificationDestinationAndReadStateSurviveSavedStateRestore() {
        val restoration = StateRestorationTester(composeRule)
        startApp(restoration)
        composeRule.onNodeWithContentDescription("알림").performClick()
        composeRule.onNode(hasScrollToNodeAction()).performScrollToNode(hasText("새로운 공지가 있어요"))
        composeRule.onNodeWithText("새로운 공지가 있어요").performClick()
        composeRule.onNodeWithText("9월 환경지킴이 활동 안내").assertIsDisplayed()
        restoration.emulateSavedInstanceStateRestore()
        composeRule.onNodeWithText("9월 환경지킴이 활동 안내").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("뒤로 가기").performClick()
        composeRule.onNodeWithText("새로운 공지가 있어요").assert(
            SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "읽음"),
        )
        composeRule.onNode(hasScrollToNodeAction()).performScrollToNode(hasText("전체 4건"))
        composeRule.onNodeWithText("전체 4건").assertExists()
        capture("navigation-notifications")
    }

    @Test
    fun missingNoticeReturnNavigatesToListWithoutRestoringMissingDetail() {
        lateinit var navigator: com.nativelap.ecoguard.navigation.EcoGuardMainNavigator
        val restoration = StateRestorationTester(composeRule)
        restoration.setContent {
            val navigationState =
                com.nativelap.ecoguard.navigation
                    .rememberEcoGuardMainNavigationState()
            androidx.compose.runtime.SideEffect {
                navigator =
                    com.nativelap.ecoguard.navigation
                        .EcoGuardMainNavigator(navigationState)
            }
            EcoGuardTheme {
                com.nativelap.ecoguard.navigation
                    .EcoGuardMainNavDisplay({}, navigationState)
            }
        }
        composeRule.runOnIdle {
            navigator.push(com.nativelap.ecoguard.navigation.EcoGuardDestination.Notifications)
            navigator.push(
                com.nativelap.ecoguard.navigation.EcoGuardDestination
                    .NoticeDetail(999),
            )
        }

        composeRule.onNodeWithText("공지사항을 찾을 수 없어요").assertIsDisplayed()
        composeRule.onNodeWithText("목록으로 돌아가기").performClick()
        composeRule.onNodeWithText("공지사항").assertIsDisplayed()
        Espresso.pressBack()
        composeRule.onNodeWithText("알림").assertIsDisplayed()
    }

    @Test
    fun applicationDraftRestoresAndCompletedFlowDoesNotReturnToInput() {
        val restoration = StateRestorationTester(composeRule)
        startApp(restoration)
        composeRule.onNodeWithContentDescription("알림").performClick()
        composeRule.onNodeWithText("새로운 모집 공고가 있어요").performClick()
        composeRule.onNodeWithText("신청하기").performClick()
        composeRule.onNodeWithText("신청하기").assertIsNotEnabled()
        composeRule.onNodeWithContentDescription("신청 동기").performTextInput("학교를 깨끗하게 만들고 싶어요")
        restoration.emulateSavedInstanceStateRestore()
        composeRule.onNodeWithText("학교를 깨끗하게 만들고 싶어요").assertExists()
        Espresso.closeSoftKeyboard()
        composeRule.onNodeWithText("신청하기").performClick()
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.home_became_guardian)).assertExists()
        Espresso.pressBack()
        composeRule.onNodeWithText("알림").assertIsDisplayed()
        composeRule.onNode(hasSetTextAction()).assertDoesNotExist()
    }

    @Test
    fun photoSubmissionClearsCaptureFlowAndShowsSubmittedSheet() {
        startApp()
        composeRule.onNodeWithContentDescription("청소 인증 촬영").performClick()
        composeRule.onNodeWithText("촬영하기").performClick()
        composeRule.onNodeWithContentDescription("사진 찍기").performClick()
        composeRule.onNodeWithText("다시 찍기").performClick()
        composeRule.onNodeWithContentDescription("사진 찍기").performClick()
        composeRule.onNodeWithText("보내기").performClick()
        composeRule.onNodeWithText("사진을 보냈어요").assertIsDisplayed()
        Espresso.pressBack()
        composeRule.onNodeWithText("홈").assertIsSelected()
        composeRule.onNodeWithContentDescription("청소 인증 촬영").performClick()
        composeRule.onNodeWithText("오늘은 이미 인증했어요").assertIsDisplayed()
        composeRule.onNodeWithText("제출한 인증 보기").performClick()
        composeRule.onNodeWithText("제출한 사진").assertExists()
    }

    @Test
    fun monthPickerIsRestoredAndUpdatesOnlyActivityTab() {
        val restoration = StateRestorationTester(composeRule)
        startApp(restoration)
        composeRule.onNodeWithText("기록").performClick()
        val monthDescription = composeRule.activity.getString(R.string.activity_select_month)
        composeRule.onNodeWithContentDescription(monthDescription).performClick()
        composeRule.onNodeWithText("8월").performClick()
        restoration.emulateSavedInstanceStateRestore()
        composeRule.onNodeWithText("8월").assertIsSelected()
        composeRule.onNodeWithText("적용").performClick()
        composeRule.onNodeWithText("이번 달 기록이 아직 없어요").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("홈").performClick()
        composeRule.onNodeWithText("기록").performClick()
        composeRule.onNodeWithText("이번 달 기록이 아직 없어요").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun sharedAppealDraftAcrossTabsUsesDisplayedContentForSubmission() {
        startApp()
        composeRule.onNodeWithContentDescription("알림").performClick()
        composeRule.onNodeWithText("청소 인증 결과를 확인해 주세요").performClick()
        composeRule.onNodeWithText("이의신청하기").performScrollTo().performClick()
        composeRule.onNodeWithContentDescription("내용").performScrollTo().performTextInput("첫 번째 입력")
        Espresso.closeSoftKeyboard()
        Espresso.pressBack()
        Espresso.pressBack()
        composeRule.onNodeWithText("기록").performClick()
        composeRule.onNode(hasScrollToNodeAction()).performScrollToNode(hasText("9월 22일(화)"))
        composeRule.onNodeWithText("9월 22일(화)").performClick()
        composeRule.onNodeWithText("이의신청하기").performScrollTo().performClick()
        composeRule.onNodeWithContentDescription("내용").performScrollTo().performTextReplacement("두 번째 입력")
        Espresso.closeSoftKeyboard()
        composeRule.onNodeWithText("이의신청 보내기").performClick()
        composeRule.onNodeWithText("이의신청을 보냈어요").assertIsDisplayed()
        Espresso.pressBack()
        composeRule.onNodeWithText("이의신청하기").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun logoutDisposesDemoStateAndLoginStartsWithFreshHome() {
        startApp()
        composeRule.onNodeWithContentDescription("알림").performClick()
        composeRule.onNodeWithText("새로운 모집 공고가 있어요").performClick()
        composeRule.onNodeWithText("신청하기").performClick()
        composeRule.onNodeWithContentDescription("신청 동기").performTextInput("로그아웃 전 입력")
        Espresso.closeSoftKeyboard()
        Espresso.pressBack()
        Espresso.pressBack()
        composeRule.onNodeWithText("마이페이지").performClick()
        composeRule.onNodeWithText("도움말").assertIsNotEnabled()
        composeRule.onNodeWithText("로그아웃").performScrollTo().performClick()
        composeRule.onAllNodesWithText("로그아웃")[1].performClick()
        composeRule.onNodeWithText("DataGSM으로 로그인").assertIsDisplayed().performClick()
        composeRule.onNodeWithText("홈").assertIsSelected()
        composeRule.onNodeWithContentDescription("알림").performClick()
        composeRule.onNodeWithText("새로운 모집 공고가 있어요").performClick()
        composeRule.onNodeWithText("신청하기").performClick()
        composeRule.onNodeWithText("신청하기").assertIsNotEnabled()
        composeRule.onNodeWithText("로그아웃 전 입력").assertDoesNotExist()
    }

    @Test
    fun twoTabAppealEntriesUseOneRestorableDraftPerRecord() {
        lateinit var navigationState: com.nativelap.ecoguard.navigation.EcoGuardMainNavigationState
        lateinit var navigator: com.nativelap.ecoguard.navigation.EcoGuardMainNavigator
        val restoration = StateRestorationTester(composeRule)
        restoration.setContent {
            val currentState =
                com.nativelap.ecoguard.navigation
                    .rememberEcoGuardMainNavigationState()
            androidx.compose.runtime.SideEffect {
                navigationState = currentState
                navigator =
                    com.nativelap.ecoguard.navigation
                        .EcoGuardMainNavigator(currentState)
            }
            EcoGuardTheme {
                com.nativelap.ecoguard.navigation
                    .EcoGuardMainNavDisplay({}, currentState)
            }
        }
        composeRule.runOnIdle {
            navigator.push(
                com.nativelap.ecoguard.navigation.EcoGuardDestination
                    .AppealForm(2),
            )
        }
        composeRule.onNodeWithContentDescription("내용").performScrollTo().performTextInput("첫 번째 입력")
        Espresso.closeSoftKeyboard()
        composeRule.runOnIdle {
            navigator.navigateToTab(com.nativelap.ecoguard.ui.component.EcoBottomTab.ACTIVITY)
            navigator.push(
                com.nativelap.ecoguard.navigation.EcoGuardDestination
                    .AppealForm(2),
            )
        }
        composeRule.onNodeWithContentDescription("내용").performScrollTo().performTextReplacement("")
        Espresso.closeSoftKeyboard()
        composeRule.runOnIdle { navigator.navigateToTab(com.nativelap.ecoguard.ui.component.EcoBottomTab.HOME) }
        composeRule.onNodeWithText("이의신청 보내기").assertIsNotEnabled()
        composeRule.onNodeWithContentDescription("내용").performScrollTo().performTextInput("두 탭의 같은 기록 입력")
        Espresso.closeSoftKeyboard()
        restoration.emulateSavedInstanceStateRestore()
        composeRule.runOnIdle { navigator.navigateToTab(com.nativelap.ecoguard.ui.component.EcoBottomTab.ACTIVITY) }
        composeRule.onNodeWithText("두 탭의 같은 기록 입력").assertExists()
        composeRule.onNodeWithText("이의신청 보내기").performClick()
        composeRule.onNodeWithText("이의신청을 보냈어요").assertIsDisplayed()
        composeRule.runOnIdle {
            org.junit.Assert.assertEquals(
                com.nativelap.ecoguard.navigation.EcoGuardDestination
                    .AppealSubmitted(2),
                navigationState.currentBackStack.last(),
            )
        }
    }

    private fun capture(name: String) {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val folder = File(context.getExternalFilesDir(null), "navigation-screenshots").apply { mkdirs() }
        File(folder, "$name.png").outputStream().use {
            composeRule
                .onRoot()
                .captureToImage()
                .asAndroidBitmap()
                .compress(Bitmap.CompressFormat.PNG, 100, it)
        }
    }
}
