package com.nativelap.ecoguard.feature.login.view

import androidx.compose.runtime.Composable
import com.nativelap.ecoguard.feature.login.viewmodel.TeacherAccountGuideScreenEvent

@Composable
fun TeacherAccountGuideRoute(
    onCopyWebAddressClick: () -> Unit = {},
    onShareWebAddressClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
) {
    TeacherAccountGuideScreen(
        onEvent = { screenEvent ->
            when (screenEvent) {
                TeacherAccountGuideScreenEvent.CopyWebAddressClick -> onCopyWebAddressClick()
                TeacherAccountGuideScreenEvent.ShareWebAddressClick -> onShareWebAddressClick()
                TeacherAccountGuideScreenEvent.LogoutClick -> onLogoutClick()
            }
        },
    )
}
