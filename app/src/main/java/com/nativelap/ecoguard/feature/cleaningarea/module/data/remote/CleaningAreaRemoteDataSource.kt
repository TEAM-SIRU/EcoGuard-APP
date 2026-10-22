package com.nativelap.ecoguard.feature.cleaningarea.module.data.remote

import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.feature.cleaningarea.module.data.dto.AreaMapResponseDto
import com.nativelap.ecoguard.feature.cleaningarea.module.data.dto.AssignmentResponseDto
import com.nativelap.ecoguard.feature.cleaningarea.module.data.dto.MyAssignmentResponseDto

interface CleaningAreaRemoteDataSource {
    suspend fun getAreas(): NetworkResult<List<AreaMapResponseDto>>

    suspend fun assignStudents(
        areaId: Long,
        studentIds: List<Long>,
    ): NetworkResult<List<AssignmentResponseDto>>

    suspend fun updateArea(
        areaId: Long,
        active: Boolean?,
    ): NetworkResult<Unit>

    suspend fun unassignStudent(
        areaId: Long,
        studentId: Long,
    ): NetworkResult<Unit>

    suspend fun getMyAssignment(): NetworkResult<MyAssignmentResponseDto>
}
