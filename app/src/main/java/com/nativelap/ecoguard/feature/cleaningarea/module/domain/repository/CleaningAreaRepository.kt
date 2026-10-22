package com.nativelap.ecoguard.feature.cleaningarea.module.domain.repository

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.cleaningarea.module.domain.model.AreaActivationUpdate
import com.nativelap.ecoguard.feature.cleaningarea.module.domain.model.AreaAssignment
import com.nativelap.ecoguard.feature.cleaningarea.module.domain.model.CleaningArea
import com.nativelap.ecoguard.feature.cleaningarea.module.domain.model.MyCleaningAssignment

interface CleaningAreaRepository {
    suspend fun getAreas(): AppResult<List<CleaningArea>>

    suspend fun assignStudents(
        areaId: Long,
        studentIds: List<Long>,
    ): AppResult<List<AreaAssignment>>

    suspend fun updateArea(
        areaId: Long,
        update: AreaActivationUpdate,
    ): AppResult<Unit>

    suspend fun unassignStudent(
        areaId: Long,
        studentId: Long,
    ): AppResult<Unit>

    suspend fun getMyAssignment(): AppResult<MyCleaningAssignment>
}
