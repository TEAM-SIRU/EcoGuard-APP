package com.nativelap.ecoguard.feature.activity.module.data.remote

import com.nativelap.ecoguard.core.network.ApiExecutor
import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.feature.activity.module.data.dto.MyActivityResponseDto
import com.nativelap.ecoguard.feature.activity.module.data.dto.StudentActivityResponseDto
import com.nativelap.ecoguard.feature.activity.module.data.dto.WeeklyActivityResponseDto
import javax.inject.Inject

class ActivityRemoteDataSourceImpl
    @Inject
    constructor(
        private val apiService: ActivityApiService,
        private val apiExecutor: ApiExecutor,
    ) : ActivityRemoteDataSource {
        override suspend fun getStudentActivity(
            studentId: Long,
            year: Int?,
            month: Int?,
        ): NetworkResult<MyActivityResponseDto> =
            apiExecutor.execute {
                apiService.getStudentActivity(studentId, year, month)
            }

        override suspend fun searchStudents(keyword: String?): NetworkResult<List<StudentActivityResponseDto>> =
            apiExecutor.execute { apiService.searchStudents(keyword) }

        override suspend fun getMyActivity(
            year: Int?,
            month: Int?,
        ): NetworkResult<MyActivityResponseDto> = apiExecutor.execute { apiService.getMyActivity(year, month) }

        override suspend fun getMyWeeklyActivity(): NetworkResult<WeeklyActivityResponseDto> =
            apiExecutor.execute { apiService.getMyWeeklyActivity() }
    }
