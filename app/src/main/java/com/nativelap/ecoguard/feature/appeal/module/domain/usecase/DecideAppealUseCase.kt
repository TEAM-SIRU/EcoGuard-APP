package com.nativelap.ecoguard.feature.appeal.module.domain.usecase

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.appeal.module.domain.model.AppealDecision
import com.nativelap.ecoguard.feature.appeal.module.domain.repository.AppealRepository
import javax.inject.Inject

class DecideAppealUseCase
    @Inject
    constructor(
        private val appealRepository: AppealRepository,
    ) {
        suspend operator fun invoke(
            appealId: Long,
            decision: AppealDecision,
        ): AppResult<Unit> = appealRepository.decideAppeal(appealId, decision)
    }
