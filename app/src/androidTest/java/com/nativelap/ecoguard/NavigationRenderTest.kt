package com.nativelap.ecoguard

import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nativelap.ecoguard.feature.notice.view.NoticeDetailScreen
import com.nativelap.ecoguard.feature.notice.view.NoticeListScreen
import com.nativelap.ecoguard.feature.notice.viewmodel.NoticeDetailUiState
import com.nativelap.ecoguard.feature.notice.viewmodel.NoticeUiState
import com.nativelap.ecoguard.feature.notification.view.NotificationScreen
import com.nativelap.ecoguard.feature.notification.viewmodel.NotificationUiModel
import com.nativelap.ecoguard.feature.notification.viewmodel.NotificationUiState
import com.nativelap.ecoguard.ui.component.EcoBottomTab
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationRenderTest {
    @get:Rule val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun rendersNoticeLoadingEmptyAndFailureStates() {
        composeRule.activityRule.scenario.onActivity { it.enableEdgeToEdge() }
        var state by mutableIntStateOf(0)
        composeRule.setContent {
            EcoGuardTheme {
                when (state) {
                    0 -> NoticeListScreen(NoticeUiState.Loading, {})
                    1 -> NoticeListScreen(NoticeUiState.Empty, {})
                    else -> NoticeListScreen(NoticeUiState.Failed, {})
                }
            }
        }
        composeRule.onNodeWithText("공지사항").assertIsDisplayed()
        composeRule.runOnIdle { state = 1 }
        composeRule.waitForIdle()
        composeRule.runOnIdle { state = 2 }
        composeRule.waitForIdle()
    }

    @Test
    fun rendersNotificationEmptyStateWithoutSampleItems() {
        composeRule.activityRule.scenario.onActivity { it.enableEdgeToEdge() }
        composeRule.setContent {
            EcoGuardTheme {
                NotificationScreen(NotificationUiState.Empty, EcoBottomTab.HOME, {})
            }
        }
        composeRule.onNodeWithText("새로운 알림이 없어요").assertIsDisplayed()
    }

    @Test
    fun rendersTotalNotificationCountSeparatelyFromUnreadCount() {
        composeRule.activityRule.scenario.onActivity { it.enableEdgeToEdge() }
        val notifications =
            listOf(
                NotificationUiModel(1, "NOTICE", "읽지 않은 알림", "내용", "11월 27일 09:00"),
                NotificationUiModel(2, "NOTICE", "읽은 알림", "내용", "11월 26일 09:00", isRead = true),
            )
        composeRule.setContent {
            EcoGuardTheme {
                NotificationScreen(
                    NotificationUiState.Content(unreadCount = 1, notifications = notifications),
                    EcoBottomTab.HOME,
                    {},
                )
            }
        }

        composeRule.onNodeWithText("전체 2건").assertIsDisplayed()
    }

    @Test
    fun rendersNoticeDetailLoadingAndFailureStates() {
        composeRule.activityRule.scenario.onActivity { it.enableEdgeToEdge() }
        var state by mutableIntStateOf(0)
        composeRule.setContent {
            EcoGuardTheme {
                if (state == 0) {
                    NoticeDetailScreen(NoticeDetailUiState.Loading, {})
                } else {
                    NoticeDetailScreen(NoticeDetailUiState.Failed, {})
                }
            }
        }
        composeRule.waitForIdle()
        composeRule.runOnIdle { state = 1 }
        composeRule.waitForIdle()
    }
}
