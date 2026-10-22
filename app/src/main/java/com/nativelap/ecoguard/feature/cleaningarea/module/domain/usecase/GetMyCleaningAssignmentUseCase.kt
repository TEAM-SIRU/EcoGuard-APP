package com.nativelap.ecoguard.feature.cleaningarea.module.domain.usecase

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.cleaningarea.module.domain.model.MyCleaningAssignment
import com.nativelap.ecoguard.feature.cleaningarea.module.domain.repository.CleaningAreaRepository
import javax.inject.Inject

class GetMyCleaningAssignmentUseCase
    @Inject
    constructor(
        private val repository: CleaningAreaRepository,
    ) {
        suspend operator fun invoke(): AppResult<MyCleaningAssignment> = repository.getMyAssignment()
    }
