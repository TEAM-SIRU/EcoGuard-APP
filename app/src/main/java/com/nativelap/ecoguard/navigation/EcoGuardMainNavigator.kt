package com.nativelap.ecoguard.navigation

import com.nativelap.ecoguard.ui.component.EcoBottomTab

class EcoGuardMainNavigator(
    private val navigationState: EcoGuardMainNavigationState,
) {
    fun navigateToTab(tab: EcoBottomTab) {
        navigationState.selectedTabIndex = tab.ordinal
    }

    fun push(destination: EcoGuardDestination) {
        val backStack = navigationState.currentBackStack
        val existingIndex = backStack.indexOf(destination)
        if (existingIndex >= 0) {
            while (backStack.lastIndex > existingIndex) backStack.removeLastOrNull()
        } else {
            backStack.add(destination)
        }
    }

    fun goBack() {
        val backStack = navigationState.currentBackStack
        if (backStack.size > 1) {
            backStack.removeLastOrNull()
        } else {
            navigateToTab(EcoBottomTab.HOME)
        }
    }

    fun replaceFlow(
        startDestination: EcoGuardDestination,
        destination: EcoGuardDestination,
    ) {
        val backStack = navigationState.currentBackStack
        val startIndex = backStack.indexOf(startDestination)
        if (startIndex > 0) {
            while (backStack.size > startIndex) backStack.removeLastOrNull()
        } else {
            while (backStack.size > 1) backStack.removeLastOrNull()
        }
        push(destination)
    }

    fun returnToHome() {
        while (navigationState.currentBackStack.size > 1) navigationState.currentBackStack.removeLastOrNull()
        navigateToTab(EcoBottomTab.HOME)
        while (navigationState.currentBackStack.size > 1) navigationState.currentBackStack.removeLastOrNull()
    }
}
