package com.nativelap.ecoguard.feature.activity.module.data.repository

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.core.network.toAppResult
import com.nativelap.ecoguard.feature.activity.module.data.mapper.toDomain
import com.nativelap.ecoguard.feature.activity.module.data.remote.ActivityRemoteDataSource
import com.nativelap.ecoguard.feature.activity.module.domain.model.MyActivity
import com.nativelap.ecoguard.feature.activity.module.domain.model.StudentActivity
import com.nativelap.ecoguard.feature.activity.module.domain.model.WeeklyActivity
import com.nativelap.ecoguard.feature.activity.module.domain.repository.ActivityRepository
import javax.inject.Inject

class ActivityRepositoryImpl
    @Inject
    constructor(
        private val remoteDataSource: ActivityRemoteDataSource,
    ) : ActivityRepository {
        override suspend fun getStudentActivity(
            studentId: Long,
            year: Int?,
            month: Int?,
        ): AppResult<MyActivity> =
            when (val result = remoteDataSource.getStudentActivity(studentId, year, month)) {
                is NetworkResult.Success -> AppResult.Success(result.value.toDomain())
                is NetworkResult.Failure -> result.toAppResult()
            }

        override suspend fun searchStudents(keyword: String?): AppResult<List<StudentActivity>> =
            when (val result = remoteDataSource.searchStudents(keyword)) {
                is NetworkResult.Success -> AppResult.Success(result.value.map { it.toDomain() })
                is NetworkResult.Failure -> result.toAppResult()
            }

        override suspend fun getMyActivity(
            year: Int?,
            month: Int?,
        ): AppResult<MyActivity> =
            when (val result = remoteDataSource.getMyActivity(year, month)) {
                is NetworkResult.Success -> AppResult.Success(result.value.toDomain())
                is NetworkResult.Failure -> result.toAppResult()
            }

        override suspend fun getMyWeeklyActivity(): AppResult<WeeklyActivity> =
            when (val result = remoteDataSource.getMyWeeklyActivity()) {
                is NetworkResult.Success -> AppResult.Success(result.value.toDomain())
                is NetworkResult.Failure -> result.toAppResult()
            }
    }
