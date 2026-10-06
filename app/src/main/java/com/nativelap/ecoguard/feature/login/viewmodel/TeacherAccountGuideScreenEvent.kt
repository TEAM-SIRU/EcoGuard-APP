package com.nativelap.ecoguard.feature.login.viewmodel

sealed interface TeacherAccountGuideScreenEvent {
    data object CopyWebAddressClick : TeacherAccountGuideScreenEvent

    data object ShareWebAddressClick : TeacherAccountGuideScreenEvent

    data object LogoutClick : TeacherAccountGuideScreenEvent
}
