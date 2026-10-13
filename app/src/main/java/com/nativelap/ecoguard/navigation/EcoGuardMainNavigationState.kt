package com.nativelap.ecoguard.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import com.nativelap.ecoguard.ui.component.EcoBottomTab

class EcoGuardMainNavigationState(
    val backStacks: Map<EcoBottomTab, NavBackStack<NavKey>>,
    selectedTabIndex: MutableState<Int>,
) {
    var selectedTabIndex by selectedTabIndex
    val selectedTab: EcoBottomTab get() = EcoBottomTab.entries[selectedTabIndex]
    val currentBackStack: NavBackStack<NavKey> get() = backStacks.getValue(selectedTab)
    val canGoBack: Boolean get() = currentBackStack.size > 1 || selectedTab != EcoBottomTab.HOME
}

@Composable
fun rememberEcoGuardMainNavigationState(): EcoGuardMainNavigationState {
    val selectedIndex = rememberSaveable { mutableIntStateOf(0) }
    val home = rememberNavBackStack(EcoGuardDestination.Home)
    val area = rememberNavBackStack(EcoGuardDestination.Area)
    val activity = rememberNavBackStack(EcoGuardDestination.Activity)
    val menu = rememberNavBackStack(EcoGuardDestination.Menu)
    return androidx.compose.runtime.remember {
        EcoGuardMainNavigationState(
            mapOf(
                EcoBottomTab.HOME to home,
                EcoBottomTab.AREA to area,
                EcoBottomTab.ACTIVITY to activity,
                EcoBottomTab.MY_PAGE to menu,
            ),
            selectedIndex,
        )
    }
}

@Composable
fun EcoGuardMainNavigationState.toEntries(entryProvider: (NavKey) -> NavEntry<NavKey>): List<NavEntry<NavKey>> {
    // 비활성 탭도 decorator를 유지해 입력·스크롤 상태가 탭 전환 후 복원되도록 한다.
    val decoratedTabs =
        EcoBottomTab.entries.map { tab ->
            androidx.compose.runtime.key(tab) {
                rememberDecoratedNavEntries(
                    backStack = backStacks.getValue(tab),
                    entryDecorators = listOf(rememberSaveableStateHolderNavEntryDecorator()),
                    entryProvider = entryProvider,
                )
            }
        }
    return decoratedTabs[selectedTabIndex]
}
