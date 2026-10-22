package com.nativelap.ecoguard.feature.cleaningarea.module.data.remote

import com.nativelap.ecoguard.core.network.ApiExecutor
import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.feature.cleaningarea.module.data.dto.AreaMapResponseDto
import com.nativelap.ecoguard.feature.cleaningarea.module.data.dto.AssignmentResponseDto
import com.nativelap.ecoguard.feature.cleaningarea.module.data.dto.MyAssignmentResponseDto
import com.nativelap.ecoguard.feature.cleaningarea.module.data.dto.UpdateAreaRequestDto
import com.nativelap.ecoguard.feature.cleaningarea.module.data.mapper.toAssignStudentsRequestDto
import javax.inject.Inject

class CleaningAreaRemoteDataSourceImpl
    @Inject
    constructor(
        private val apiService: CleaningAreaApiService,
        private val apiExecutor: ApiExecutor,
    ) : CleaningAreaRemoteDataSource {
        override suspend fun getAreas(): NetworkResult<List<AreaMapResponseDto>> =
            apiExecutor.execute {
                apiService.getAreas()
            }

        override suspend fun assignStudents(
            areaId: Long,
            studentIds: List<Long>,
        ): NetworkResult<List<AssignmentResponseDto>> =
            apiExecutor.execute {
                apiService.assignStudents(areaId, studentIds.toAssignStudentsRequestDto())
            }

        override suspend fun updateArea(
            areaId: Long,
            active: Boolean?,
        ): NetworkResult<Unit> =
            apiExecutor.executeNoContent {
                apiService.updateArea(areaId, UpdateAreaRequestDto(active))
            }

        override suspend fun unassignStudent(
            areaId: Long,
            studentId: Long,
        ): NetworkResult<Unit> =
            apiExecutor.executeNoContent {
                apiService.unassignStudent(areaId, studentId)
            }

        override suspend fun getMyAssignment(): NetworkResult<MyAssignmentResponseDto> =
            apiExecutor.execute {
                apiService.getMyAssignment()
            }
    }
