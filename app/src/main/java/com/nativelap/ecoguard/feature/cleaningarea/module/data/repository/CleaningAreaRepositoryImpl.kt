package com.nativelap.ecoguard.feature.cleaningarea.module.data.repository

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.core.network.toAppResult
import com.nativelap.ecoguard.feature.cleaningarea.module.data.mapper.toDomain
import com.nativelap.ecoguard.feature.cleaningarea.module.data.remote.CleaningAreaRemoteDataSource
import com.nativelap.ecoguard.feature.cleaningarea.module.domain.model.AreaActivationUpdate
import com.nativelap.ecoguard.feature.cleaningarea.module.domain.model.AreaAssignment
import com.nativelap.ecoguard.feature.cleaningarea.module.domain.model.CleaningArea
import com.nativelap.ecoguard.feature.cleaningarea.module.domain.model.MyCleaningAssignment
import com.nativelap.ecoguard.feature.cleaningarea.module.domain.repository.CleaningAreaRepository
import javax.inject.Inject

class CleaningAreaRepositoryImpl
    @Inject
    constructor(
        private val remoteDataSource: CleaningAreaRemoteDataSource,
    ) : CleaningAreaRepository {
        override suspend fun getAreas(): AppResult<List<CleaningArea>> =
            remoteDataSource.getAreas().mapSuccess { areas -> areas.map { it.toDomain() } }

        override suspend fun assignStudents(
            areaId: Long,
            studentIds: List<Long>,
        ): AppResult<List<AreaAssignment>> =
            remoteDataSource.assignStudents(areaId, studentIds).mapSuccess { assignments ->
                assignments.map { it.toDomain() }
            }

        override suspend fun updateArea(
            areaId: Long,
            update: AreaActivationUpdate,
        ): AppResult<Unit> = remoteDataSource.updateArea(areaId, update.active).mapSuccess { Unit }

        override suspend fun unassignStudent(
            areaId: Long,
            studentId: Long,
        ): AppResult<Unit> = remoteDataSource.unassignStudent(areaId, studentId).mapSuccess { Unit }

        override suspend fun getMyAssignment(): AppResult<MyCleaningAssignment> =
            remoteDataSource.getMyAssignment().mapSuccess { it.toDomain() }
    }

private inline fun <T : Any, R : Any> NetworkResult<T>.mapSuccess(transform: (T) -> R): AppResult<R> =
    when (this) {
        is NetworkResult.Success -> AppResult.Success(transform(value))
        is NetworkResult.Failure -> toAppResult()
    }
