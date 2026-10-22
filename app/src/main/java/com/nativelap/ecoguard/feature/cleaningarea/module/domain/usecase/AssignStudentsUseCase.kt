package com.nativelap.ecoguard.feature.cleaningarea.module.domain.usecase

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.cleaningarea.module.domain.model.AreaAssignment
import com.nativelap.ecoguard.feature.cleaningarea.module.domain.repository.CleaningAreaRepository
import javax.inject.Inject

class AssignStudentsUseCase
    @Inject
    constructor(
        private val repository: CleaningAreaRepository,
    ) {
        suspend operator fun invoke(
            areaId: Long,
            studentIds: List<Long>,
        ): AppResult<List<AreaAssignment>> = repository.assignStudents(areaId, studentIds)
    }
