package com.nativelap.ecoguard.feature.appeal.module.domain.usecase

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.appeal.module.domain.model.MyAppeal
import com.nativelap.ecoguard.feature.appeal.module.domain.repository.AppealRepository
import javax.inject.Inject

class GetMyAppealsUseCase
    @Inject
    constructor(
        private val appealRepository: AppealRepository,
    ) {
        suspend operator fun invoke(): AppResult<List<MyAppeal>> = appealRepository.getMyAppeals()
    }
