package com.nativelap.ecoguard.feature.menu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.user.module.domain.usecase.GetMyProfileUseCase
import com.nativelap.ecoguard.feature.user.module.domain.usecase.WithdrawUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel
    @Inject
    constructor(
        private val getMyProfile: GetMyProfileUseCase,
        private val withdrawUseCase: WithdrawUseCase,
    ) : ViewModel() {
        private val mutableUiState = MutableStateFlow<MenuUiState>(MenuUiState.Loading)
        val uiState = mutableUiState.asStateFlow()

        init {
            loadProfile()
        }

        fun retry() {
            loadProfile()
        }

        fun withdraw() {
            val current = mutableUiState.value as? MenuUiState.Content ?: return
            if (current.isWithdrawing || current.isWithdrawn) return

            mutableUiState.update {
                current.copy(isWithdrawing = true, hasWithdrawalFailed = false)
            }
            viewModelScope.launch {
                when (withdrawUseCase()) {
                    is AppResult.Success -> {
                        mutableUiState.update { state ->
                            (state as? MenuUiState.Content)?.copy(
                                isWithdrawing = false,
                                isWithdrawn = true,
                            ) ?: state
                        }
                    }

                    is AppResult.Failure -> {
                        mutableUiState.update { state ->
                            (state as? MenuUiState.Content)?.copy(
                                isWithdrawing = false,
                                hasWithdrawalFailed = true,
                            ) ?: state
                        }
                    }
                }
            }
        }

        private fun loadProfile() {
            mutableUiState.value = MenuUiState.Loading
            viewModelScope.launch {
                mutableUiState.value = getMyProfile().toMenuUiState()
            }
        }
    }
