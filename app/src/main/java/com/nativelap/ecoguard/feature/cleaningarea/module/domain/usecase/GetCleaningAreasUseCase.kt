package com.nativelap.ecoguard.feature.cleaningarea.module.domain.usecase

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.cleaningarea.module.domain.model.CleaningArea
import com.nativelap.ecoguard.feature.cleaningarea.module.domain.repository.CleaningAreaRepository
import javax.inject.Inject

class GetCleaningAreasUseCase
    @Inject
    constructor(
        private val repository: CleaningAreaRepository,
    ) {
        suspend operator fun invoke(): AppResult<List<CleaningArea>> = repository.getAreas()
    }
