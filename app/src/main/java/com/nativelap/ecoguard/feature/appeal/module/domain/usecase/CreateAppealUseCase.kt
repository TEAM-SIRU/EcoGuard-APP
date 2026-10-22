package com.nativelap.ecoguard.feature.appeal.module.domain.usecase

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.appeal.module.domain.repository.AppealRepository
import javax.inject.Inject

class CreateAppealUseCase
    @Inject
    constructor(
        private val appealRepository: AppealRepository,
    ) {
        suspend operator fun invoke(
            verificationId: Long,
            content: String,
        ): AppResult<Unit> = appealRepository.createAppeal(verificationId, content)
    }
