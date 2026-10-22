package com.nativelap.ecoguard.feature.cleaningarea.module.domain.usecase

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.cleaningarea.module.domain.model.AreaActivationUpdate
import com.nativelap.ecoguard.feature.cleaningarea.module.domain.repository.CleaningAreaRepository
import javax.inject.Inject

class UpdateCleaningAreaUseCase
    @Inject
    constructor(
        private val repository: CleaningAreaRepository,
    ) {
        suspend operator fun invoke(
            areaId: Long,
            update: AreaActivationUpdate,
        ): AppResult<Unit> = repository.updateArea(areaId, update)
    }
