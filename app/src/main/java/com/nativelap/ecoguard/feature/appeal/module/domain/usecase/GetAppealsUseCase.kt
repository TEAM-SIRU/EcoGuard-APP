package com.nativelap.ecoguard.feature.appeal.module.domain.usecase

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.appeal.module.domain.model.Appeal
import com.nativelap.ecoguard.feature.appeal.module.domain.model.AppealStatus
import com.nativelap.ecoguard.feature.appeal.module.domain.repository.AppealRepository
import javax.inject.Inject

class GetAppealsUseCase
    @Inject
    constructor(
        private val appealRepository: AppealRepository,
    ) {
        suspend operator fun invoke(status: AppealStatus? = null): AppResult<List<Appeal>> =
            appealRepository.getAppeals(status)
    }
