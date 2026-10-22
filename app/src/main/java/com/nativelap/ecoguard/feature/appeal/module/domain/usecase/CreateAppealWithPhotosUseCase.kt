package com.nativelap.ecoguard.feature.appeal.module.domain.usecase

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.appeal.module.domain.repository.AppealRepository
import java.io.File
import javax.inject.Inject

class CreateAppealWithPhotosUseCase
    @Inject
    constructor(
        private val appealRepository: AppealRepository,
    ) {
        suspend operator fun invoke(
            verificationId: Long,
            content: String,
            photos: List<File>,
        ): AppResult<Unit> = appealRepository.createAppealWithPhotos(verificationId, content, photos)
    }
