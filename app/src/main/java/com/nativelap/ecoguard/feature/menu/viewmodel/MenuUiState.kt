package com.nativelap.ecoguard.feature.menu.viewmodel

import androidx.compose.runtime.Immutable

@Immutable
data class MenuUiState(
    val studentName: String,
    val grade: Int,
    val classNumber: Int,
    val monthlyApprovalCount: Int,
    val monthlyMinutes: Int,
    val assignedAreaName: String?,
    val isApplicationCompleted: Boolean,
    val isCleaningNotificationEnabled: Boolean,
) {
    /** 프로필 원 안에 표시할 이름 뒤 두 글자(예: 최민준 → 민준). */
    val profileInitials: String
        get() = studentName.takeLast(PROFILE_INITIALS_LENGTH)

    private companion object {
        const val PROFILE_INITIALS_LENGTH = 2
    }
}
