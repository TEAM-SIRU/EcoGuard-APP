package com.nativelap.ecoguard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.click
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.feature.activity.view.ActivitySummaryContent
import com.nativelap.ecoguard.feature.activity.viewmodel.ActivityUiState
import com.nativelap.ecoguard.feature.home.view.HomeNoticeCard
import com.nativelap.ecoguard.feature.home.viewmodel.HomeNoticeUiModel
import com.nativelap.ecoguard.ui.component.EcoSegmentedControl
import com.nativelap.ecoguard.ui.component.InlineEmptyState
import com.nativelap.ecoguard.ui.component.SectionHeader
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class CompactTouchTargetTest {
    @get:Rule val composeRule = createComposeRule()

    @Test fun sectionActionHasExpandedTouchTarget() {
        var clicks = 0
        composeRule.setContent {
            EcoGuardTheme {
                Column(Modifier.padding(40.dp)) {
                    SectionHeader("기록", actionText = "전체보기", onActionClick = { clicks++ })
                }
            }
        }
        clickNearTouchEdge("전체보기")
        composeRule.runOnIdle { assertEquals(1, clicks) }
    }

    @Test fun segmentTouchExpansionSelectsOnlyTheIntendedSegment() {
        var selected = -1
        composeRule.setContent {
            EcoGuardTheme {
                EcoSegmentedControl(listOf("1층", "2층"), 0, { selected = it }, Modifier.padding(40.dp))
            }
        }
        clickNearTouchEdge("2층")
        composeRule.runOnIdle { assertEquals(1, selected) }
    }

    @Test fun compactEmptyActionKeepsExpandedTouchTarget() {
        var clicks = 0
        composeRule.setContent {
            EcoGuardTheme {
                InlineEmptyState(R.drawable.ic_list_30, "빈 기록", "안내", Modifier.padding(40.dp), "다시 시도", { clicks++ })
            }
        }
        clickNearTouchEdge("다시 시도")
        composeRule.runOnIdle { assertEquals(1, clicks) }
    }

    @Test fun noticeCloseAndDetailRemainIndependentAtTouchEdges() {
        var closes = 0
        var details = 0
        composeRule.setContent {
            EcoGuardTheme {
                HomeNoticeCard(HomeNoticeUiModel("2026. 09. 01", "활동 안내", "본문", false), {
                    closes++
                }, { details++ }, Modifier.padding(40.dp))
            }
        }
        val closeBounds = composeRule.onNodeWithContentDescription("닫기").fetchSemanticsNode().boundsInRoot
        val edge = with(composeRule.density) { 23.dp.toPx() }
        composeRule.onRoot().performTouchInput { click(Offset(closeBounds.center.x, closeBounds.center.y + edge)) }
        clickNearTouchEdge("자세히 보기")
        composeRule.runOnIdle {
            assertEquals(1, closes)
            assertEquals(1, details)
        }
    }

    @Test fun monthSelectorKeepsExpandedTouchTarget() {
        var clicks = 0
        composeRule.setContent {
            EcoGuardTheme {
                ActivitySummaryContent(ActivityUiState.Content(2026, 9, 0, 0, 0, 0, emptyList()), {
                    clicks++
                }, Modifier.padding(top = 40.dp))
            }
        }
        clickNearTouchEdge("2026년 9월")
        composeRule.runOnIdle { assertEquals(1, clicks) }
    }

    private fun clickNearTouchEdge(text: String) {
        val bounds = composeRule.onNodeWithText(text).fetchSemanticsNode().boundsInRoot
        val touchHalfHeight = with(composeRule.density) { 23.dp.toPx() }
        composeRule.onRoot().performTouchInput {
            click(Offset(bounds.center.x, bounds.center.y + touchHalfHeight))
        }
    }
}
