package com.nativelap.ecoguard.feature.menu.viewmodel

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.user.module.domain.model.UserProfile

internal fun AppResult<UserProfile>.toMenuUiState(): MenuUiState =
    when (this) {
        is AppResult.Success -> {
            val profile = value
            profile.name
                ?.takeIf(String::isNotBlank)
                ?.let { name ->
                    MenuUiState.Content(
                        userName = name,
                        studentNumber = profile.studentNumber,
                        grade = profile.grade,
                        classNumber = profile.classNo,
                    )
                } ?: MenuUiState.Failed
        }

        is AppResult.Failure -> {
            MenuUiState.Failed
        }
    }
