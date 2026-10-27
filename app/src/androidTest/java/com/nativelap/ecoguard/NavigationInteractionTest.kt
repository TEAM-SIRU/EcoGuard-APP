package com.nativelap.ecoguard

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso
import com.nativelap.ecoguard.feature.area.view.AreaScreen
import com.nativelap.ecoguard.feature.area.viewmodel.AreaScreenEvent
import com.nativelap.ecoguard.feature.area.viewmodel.AreaUiState
import com.nativelap.ecoguard.feature.auth.presentation.AuthUiState
import com.nativelap.ecoguard.feature.home.view.HomeLoadFailedScreen
import com.nativelap.ecoguard.feature.home.viewmodel.HomeScreenEvent
import com.nativelap.ecoguard.feature.notice.view.NoticeDetailScreen
import com.nativelap.ecoguard.feature.notice.view.NoticeListScreen
import com.nativelap.ecoguard.feature.notice.viewmodel.NoticeDetailUiState
import com.nativelap.ecoguard.feature.notice.viewmodel.NoticeScreenEvent
import com.nativelap.ecoguard.feature.notice.viewmodel.NoticeUiState
import com.nativelap.ecoguard.feature.notification.view.NotificationRoute
import com.nativelap.ecoguard.feature.notification.viewmodel.NotificationUiState
import com.nativelap.ecoguard.navigation.EcoGuardDestination
import com.nativelap.ecoguard.navigation.EcoGuardMainNavigator
import com.nativelap.ecoguard.navigation.EcoGuardNavHost
import com.nativelap.ecoguard.navigation.rememberEcoGuardMainNavigationState
import com.nativelap.ecoguard.ui.component.EcoBottomTab
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import org.junit.Assert.assertEquals
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
                EcoGuardNavHost(
                    AuthUiState(),
                    onLoginClick = { clicks += 1 },
                    onSignOut = {},
                )
            }
        }
        composeRule.onNodeWithText("DataGSM으로 로그인").assertIsDisplayed()
        composeRule.onNodeWithText("선생님 계정 안내").assertIsDisplayed().performClick()
        composeRule.onNodeWithText("선생님은 웹에서 이용해 주세요").assertIsDisplayed()
        Espresso.pressBack()
        composeRule.onNodeWithText("DataGSM으로 로그인").assertIsDisplayed()
        composeRule.runOnIdle { assertEquals(0, clicks) }
    }

    @Test
    fun tabsAndSystemBackReturnToHome() {
        composeRule.activityRule.scenario.onActivity { it.enableEdgeToEdge() }
        composeRule.setContent {
            val navigationState = rememberEcoGuardMainNavigationState()
            val navigator = remember(navigationState) { EcoGuardMainNavigator(navigationState) }
            BackHandler(enabled = navigationState.canGoBack) { navigator.goBack() }
            EcoGuardTheme {
                when (navigationState.selectedTab) {
                    EcoBottomTab.HOME -> {
                        HomeLoadFailedScreen(onEvent = { event ->
                            if (event is HomeScreenEvent.TabSelect) navigator.navigateToTab(event.tab)
                        })
                    }

                    EcoBottomTab.AREA -> {
                        AreaScreen(uiState = AreaUiState.NotAssigned, onEvent = { event ->
                            if (event is AreaScreenEvent.TabSelect) navigator.navigateToTab(event.tab)
                        })
                    }

                    else -> {
                        Text("다른 탭")
                    }
                }
            }
        }

        composeRule.onNodeWithText("구역").performClick().assertIsSelected()
        Espresso.pressBack()
        composeRule.onNodeWithText("홈").assertIsSelected()
        composeRule.onNodeWithText("홈을 불러오지 못했어요").assertIsDisplayed()
    }

    @Test
    fun notificationNotIntegratedScreenReturnsToHome() {
        composeRule.activityRule.scenario.onActivity { it.enableEdgeToEdge() }
        composeRule.setContent {
            val navigationState = rememberEcoGuardMainNavigationState()
            val navigator = remember(navigationState) { EcoGuardMainNavigator(navigationState) }
            LaunchedEffect(navigator) { navigator.push(EcoGuardDestination.Notifications) }
            EcoGuardTheme {
                when (navigationState.currentBackStack.last()) {
                    EcoGuardDestination.Notifications -> {
                        NotificationRoute(
                            uiState = NotificationUiState.NotIntegrated,
                            selectedTab = navigationState.selectedTab,
                            onBack = navigator::goBack,
                            onTab = navigator::navigateToTab,
                            onCamera = {},
                            onOpenNotification = { false },
                        )
                    }

                    EcoGuardDestination.Home -> {
                        Text("홈")
                    }

                    else -> {
                        Text("다른 화면")
                    }
                }
            }
        }

        composeRule.onNodeWithText("알림 기능을 아직 연결하지 않았어요").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("뒤로 가기").performClick()
        composeRule.onNodeWithText("홈").assertIsDisplayed()
    }

    @Test
    fun missingNoticeReturnsToListAndThenToPreviousScreen() {
        composeRule.activityRule.scenario.onActivity { it.enableEdgeToEdge() }
        composeRule.setContent {
            val navigationState = rememberEcoGuardMainNavigationState()
            val navigator = remember(navigationState) { EcoGuardMainNavigator(navigationState) }
            LaunchedEffect(navigator) {
                navigator.push(EcoGuardDestination.Notifications)
                navigator.push(EcoGuardDestination.Notices)
                navigator.push(EcoGuardDestination.NoticeDetail(999))
            }
            EcoGuardTheme {
                when (navigationState.currentBackStack.last()) {
                    is EcoGuardDestination.NoticeDetail -> {
                        NoticeDetailScreen(NoticeDetailUiState.Missing) { event ->
                            if (event == NoticeScreenEvent.ReturnToListClick) navigator.goBack()
                        }
                    }

                    EcoGuardDestination.Notices -> {
                        NoticeListScreen(NoticeUiState.Empty) { event ->
                            if (event == NoticeScreenEvent.BackClick) navigator.goBack()
                        }
                    }

                    EcoGuardDestination.Notifications -> {
                        Text("알림")
                    }

                    else -> {
                        Text("홈")
                    }
                }
            }
        }

        composeRule.onNodeWithText("공지사항을 찾을 수 없어요").assertIsDisplayed()
        composeRule.onNodeWithText("목록으로 돌아가기").performClick()
        composeRule.onNodeWithText("공지사항").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("뒤로 가기").performClick()
        composeRule.onNodeWithText("알림").assertIsDisplayed()
    }
}
