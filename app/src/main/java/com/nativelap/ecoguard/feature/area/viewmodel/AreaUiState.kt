package com.nativelap.ecoguard.feature.area.viewmodel

import androidx.compose.runtime.Immutable

@Immutable
sealed interface AreaUiState {
    data object Loading : AreaUiState

    data object LoadFailed : AreaUiState

    data object NotAssigned : AreaUiState

    data class Content(
        val floors: List<Int>,
        val selectedFloor: Int,
        val areaName: String,
        val areaDescription: String?,
        val cleaningTime: String?,
        val memberNames: List<String>,
    ) : AreaUiState
}
