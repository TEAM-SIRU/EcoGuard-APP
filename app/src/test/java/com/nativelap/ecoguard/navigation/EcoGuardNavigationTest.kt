package com.nativelap.ecoguard.navigation

import androidx.compose.runtime.mutableStateOf
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.nativelap.ecoguard.ui.component.EcoBottomTab
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class EcoGuardNavigationTest {
    private fun state() =
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
    fun tabStacksRemainIndependent() {
        val state = state()
        val navigator = EcoGuardMainNavigator(state)
        navigator.push(EcoGuardDestination.Notices)
        navigator.navigateToTab(EcoBottomTab.ACTIVITY)
        navigator.push(EcoGuardDestination.VerificationDetail(42))
        navigator.navigateToTab(EcoBottomTab.HOME)
        assertEquals(EcoGuardDestination.Notices, state.currentBackStack.last())
        assertEquals(
            EcoGuardDestination.VerificationDetail(42),
            state.backStacks.getValue(EcoBottomTab.ACTIVITY).last(),
        )
    }

    @Test
    fun backPopsRouteBeforeReturningToHomeTab() {
        val state = state()
        val navigator = EcoGuardMainNavigator(state)
        navigator.navigateToTab(EcoBottomTab.ACTIVITY)
        navigator.push(EcoGuardDestination.MonthPicker)
        navigator.goBack()
        assertEquals(EcoGuardDestination.Activity, state.currentBackStack.last())
        navigator.goBack()
        assertEquals(EcoBottomTab.HOME, state.selectedTab)
        assertFalse(state.canGoBack)
    }

    @Test
    fun duplicateDestinationPushDoesNotDuplicateBackStack() {
        val state = state()
        val navigator = EcoGuardMainNavigator(state)
        navigator.push(EcoGuardDestination.Notices)
        navigator.push(EcoGuardDestination.NoticeDetail(9))
        navigator.push(EcoGuardDestination.Notices)
        assertEquals(listOf(EcoGuardDestination.Home, EcoGuardDestination.Notices), state.currentBackStack.toList())
    }
}
