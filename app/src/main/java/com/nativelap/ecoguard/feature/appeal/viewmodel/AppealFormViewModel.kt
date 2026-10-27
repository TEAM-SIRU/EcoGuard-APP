package com.nativelap.ecoguard.feature.appeal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.appeal.module.domain.usecase.CreateAppealUseCase
import com.nativelap.ecoguard.feature.appeal.module.domain.usecase.CreateAppealWithPhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AppealFormViewModel
    @Inject
    constructor(
        private val createAppeal: CreateAppealUseCase,
        private val createAppealWithPhotos: CreateAppealWithPhotosUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<AppealFormUiState?>(null)
        val uiState: StateFlow<AppealFormUiState?> = _uiState.asStateFlow()

        private val _selectedPhotos = MutableStateFlow<List<File>>(emptyList())
        val selectedPhotos: StateFlow<List<File>> = _selectedPhotos.asStateFlow()

        private var verificationId: Long? = null
        private var isSubmitting = false

        fun initialize(
            verificationId: Long,
            initialUiState: AppealFormUiState,
            photos: List<File>,
        ) {
            if (this.verificationId == verificationId && _uiState.value != null) return
            clearPhotos()
            this.verificationId = verificationId
            _selectedPhotos.value = photos.take(MAX_APPEAL_PHOTO_COUNT)
            _uiState.value =
                initialUiState.copy(
                    attachedPhotoCount = _selectedPhotos.value.size,
                    isSubmitting = false,
                    isSubmissionFailed = false,
                    isSubmitted = false,
                )
        }

        fun updateContent(content: String) {
            val currentState = _uiState.value ?: return
            if (currentState.isSubmitting || currentState.isSubmitted) return
            _uiState.value =
                currentState.copy(
                    appealContent = content.take(currentState.maxContentLength),
                    isSubmissionFailed = false,
                )
        }

        fun updatePhotos(photos: List<File>) {
            if (_uiState.value?.let { it.isSubmitting || it.isSubmitted } == true) return
            val updatedPhotos = photos.distinct().take(MAX_APPEAL_PHOTO_COUNT)
            (_selectedPhotos.value - updatedPhotos.toSet()).forEach(File::delete)
            _selectedPhotos.value = updatedPhotos
            _uiState.value = _uiState.value?.copy(attachedPhotoCount = updatedPhotos.size)
        }

        fun clearPhotos() {
            if (isSubmitting) return
            _selectedPhotos.value.forEach(File::delete)
            _selectedPhotos.value = emptyList()
            _uiState.value = _uiState.value?.copy(attachedPhotoCount = 0)
        }

        override fun onCleared() {
            _selectedPhotos.value.forEach(File::delete)
            _selectedPhotos.value = emptyList()
            super.onCleared()
        }

        fun submit() {
            val currentState = _uiState.value ?: return
            val currentVerificationId = verificationId ?: return
            if (!currentState.canSubmit || isSubmitting) return

            isSubmitting = true
            viewModelScope.launch {
                _uiState.value = currentState.copy(isSubmitting = true, isSubmissionFailed = false)
                val photos = _selectedPhotos.value
                val result =
                    if (photos.isEmpty()) {
                        createAppeal(currentVerificationId, currentState.appealContent.trim())
                    } else {
                        createAppealWithPhotos(
                            currentVerificationId,
                            currentState.appealContent.trim(),
                            photos,
                        )
                    }

                isSubmitting = false
                when (result) {
                    is AppResult.Success -> {
                        clearPhotos()
                        _uiState.value =
                            currentState.copy(
                                isSubmitting = false,
                                isSubmissionFailed = false,
                                attachedPhotoCount = 0,
                                isSubmitted = true,
                            )
                    }

                    is AppResult.Failure -> {
                        _uiState.value =
                            currentState.copy(
                                isSubmitting = false,
                                isSubmissionFailed = true,
                            )
                    }
                }
            }
        }

        private companion object {
            const val MAX_APPEAL_PHOTO_COUNT = 3
        }
    }
