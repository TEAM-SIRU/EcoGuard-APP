package com.nativelap.ecoguard.navigation

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.SaverScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.nativelap.ecoguard.feature.appeal.viewmodel.AppealScreenEvent
import com.nativelap.ecoguard.feature.demo.view.DemoFlowController
import com.nativelap.ecoguard.feature.demo.viewmodel.DemoAppState
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationScreenEvent
import com.nativelap.ecoguard.ui.component.EcoBottomTab
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EcoGuardNavigationTest {
    private fun navigationState(): EcoGuardMainNavigationState =
        EcoGuardMainNavigationState(
            mapOf(
                EcoBottomTab.HOME to NavBackStack<NavKey>(EcoGuardDestination.Home),
                EcoBottomTab.AREA to NavBackStack<NavKey>(EcoGuardDestination.Area),
                EcoBottomTab.ACTIVITY to NavBackStack<NavKey>(EcoGuardDestination.Activity),
                EcoBottomTab.MY_PAGE to NavBackStack<NavKey>(EcoGuardDestination.Menu),
            ),
            mutableStateOf(0),
        )

    @Test
    fun tabSwitchAndReselectionPreserveIndependentStacks() {
        val state = navigationState()
        val navigator = EcoGuardMainNavigator(state)
        navigator.push(EcoGuardDestination.Notifications)
        navigator.navigateToTab(EcoBottomTab.ACTIVITY)
        navigator.push(EcoGuardDestination.VerificationResult(2))
        navigator.navigateToTab(EcoBottomTab.HOME)
        navigator.navigateToTab(EcoBottomTab.HOME)
        assertEquals(EcoGuardDestination.Notifications, state.currentBackStack.last())
        assertEquals(EcoGuardDestination.VerificationResult(2), state.backStacks.getValue(EcoBottomTab.ACTIVITY).last())
    }

    @Test
    fun duplicateClicksAndRetakeReuseExistingEntry() {
        val state = navigationState()
        val navigator = EcoGuardMainNavigator(state)
        navigator.push(EcoGuardDestination.CameraGuide)
        navigator.push(EcoGuardDestination.CameraCapture())
        navigator.push(EcoGuardDestination.PhotoConfirm)
        navigator.push(EcoGuardDestination.CameraCapture())
        navigator.push(EcoGuardDestination.CameraCapture())
        assertEquals(
            listOf(EcoGuardDestination.Home, EcoGuardDestination.CameraGuide, EcoGuardDestination.CameraCapture()),
            state.currentBackStack.toList(),
        )
    }

    @Test
    fun completedVerificationRemovesCaptureAndConfirmation() {
        val state = navigationState()
        val navigator = EcoGuardMainNavigator(state)
        val demo = DemoAppState()
        val controller = DemoFlowController(demo, navigator, {})
        controller.startVerification()
        controller.onVerificationEvent(VerificationScreenEvent.TakePhotoClick)
        controller.onVerificationEvent(VerificationScreenEvent.ShutterClick)
        controller.onVerificationEvent(VerificationScreenEvent.SendClick)
        assertEquals(
            listOf(EcoGuardDestination.Home, EcoGuardDestination.SubmissionCompleted),
            state.currentBackStack.toList(),
        )
        navigator.goBack()
        assertEquals(listOf(EcoGuardDestination.Home), state.currentBackStack.toList())
        controller.startVerification()
        assertEquals(
            EcoGuardDestination.VerificationBlocked(EcoGuardDestination.BlockReason.ALREADY_SUBMITTED),
            state.currentBackStack.last(),
        )
    }

    @Test
    fun backClosesOverlayBeforeLeavingItsTab() {
        val state = navigationState()
        val navigator = EcoGuardMainNavigator(state)
        navigator.navigateToTab(EcoBottomTab.ACTIVITY)
        navigator.push(EcoGuardDestination.MonthPicker)
        navigator.goBack()
        assertEquals(EcoBottomTab.ACTIVITY, state.selectedTab)
        assertEquals(EcoGuardDestination.Activity, state.currentBackStack.last())
        navigator.goBack()
        assertEquals(EcoBottomTab.HOME, state.selectedTab)
        assertFalse(state.canGoBack)
    }

    @Test
    fun invalidNotificationDoesNotChangeReadStateOrStack() {
        val state = navigationState()
        val demo = DemoAppState()
        val readIds = demo.readNotificationIds
        val controller = DemoFlowController(demo, EcoGuardMainNavigator(state), {})
        assertFalse(controller.openNotification(999))
        assertEquals(readIds, demo.readNotificationIds)
        assertEquals(1, state.currentBackStack.size)
        assertTrue(controller.openNotification(1))
        assertTrue(1L in demo.readNotificationIds)
        assertEquals(EcoGuardDestination.Recruitment, state.currentBackStack.last())
    }

    @Test
    fun appealDraftAndPhotoBelongToTargetRecordAcrossTabs() {
        val state = navigationState()
        val demo = DemoAppState()
        val navigator = EcoGuardMainNavigator(state)
        val controller = DemoFlowController(demo, navigator, {})
        controller.onAppealEvent(2, AppealScreenEvent.ContentChange("record two"))
        navigator.navigateToTab(EcoBottomTab.ACTIVITY)
        controller.onAppealEvent(1, AppealScreenEvent.ContentChange("record one"))
        controller.onVerificationEvent(VerificationScreenEvent.ShutterClick, appealRecordId = 1)
        controller.onAppealEvent(2, AppealScreenEvent.SubmitClick)
        assertEquals("record two", demo.appealDraft(2).content)
        assertEquals(0, demo.appealDraft(2).photoCount)
        assertEquals(1, demo.appealDraft(1).photoCount)
        assertEquals(listOf(2L), demo.submittedAppealRecordIds)
    }

    @Test
    fun restoredDemoStateRetainsDraftsAndReadIds() {
        val demo =
            DemoAppState().apply {
                updateAppealContent(2, "saved\ntext")
                addAppealPhoto(2)
                updateAppealContent(1, "other")
                selectedMonth = 8
                readNotificationIds = listOf(1, 3)
                motivation = "saved application"
            }
        val saved = with(DemoAppState.Saver) { SaverScope { true }.save(demo) }!!
        val restored = DemoAppState.Saver.restore(saved)!!
        assertEquals(demo.appealDrafts, restored.appealDrafts)
        assertEquals(demo.readNotificationIds, restored.readNotificationIds)
        assertEquals(demo.motivation, restored.motivation)
        assertEquals(8, restored.selectedMonth)
    }
}
