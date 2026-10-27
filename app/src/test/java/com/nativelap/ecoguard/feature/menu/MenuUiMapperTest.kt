package com.nativelap.ecoguard.feature.menu

import com.nativelap.ecoguard.core.domain.result.AppError
import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.menu.viewmodel.MenuUiState
import com.nativelap.ecoguard.feature.menu.viewmodel.toMenuUiState
import com.nativelap.ecoguard.feature.user.module.domain.model.UserProfile
import com.nativelap.ecoguard.feature.user.module.domain.model.UserRole
import org.junit.Assert.assertEquals
import org.junit.Test

class MenuUiMapperTest {
    @Test
    fun `profile success maps only identity provided by server`() {
        val profile = UserProfile(42, "민준", UserRole.STUDENT, null, 2, 3)

        assertEquals(
            MenuUiState.Content("민준", null, 2, 3),
            AppResult.Success(profile).toMenuUiState(),
        )
    }

    @Test
    fun `profile failure or missing name does not invent identity`() {
        assertEquals(MenuUiState.Failed, AppResult.Failure(AppError.Connectivity).toMenuUiState())
        assertEquals(
            MenuUiState.Failed,
            AppResult.Success(UserProfile(42, null, UserRole.STUDENT, null, 2, 3)).toMenuUiState(),
        )
    }
}
