package com.nativelap.ecoguard.feature.cleaningarea.module.domain.usecase

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.cleaningarea.module.domain.repository.CleaningAreaRepository
import javax.inject.Inject

class UnassignStudentUseCase
    @Inject
    constructor(
        private val repository: CleaningAreaRepository,
    ) {
        suspend operator fun invoke(
            areaId: Long,
            studentId: Long,
        ): AppResult<Unit> = repository.unassignStudent(areaId, studentId)
    }
