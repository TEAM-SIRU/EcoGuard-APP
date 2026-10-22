package com.nativelap.ecoguard.feature.activity.module.data.remote

import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.feature.activity.module.data.dto.MyActivityResponseDto
import com.nativelap.ecoguard.feature.activity.module.data.dto.StudentActivityResponseDto
import com.nativelap.ecoguard.feature.activity.module.data.dto.WeeklyActivityResponseDto

interface ActivityRemoteDataSource {
    suspend fun getStudentActivity(
        studentId: Long,
        year: Int?,
        month: Int?,
    ): NetworkResult<MyActivityResponseDto>

    suspend fun searchStudents(keyword: String?): NetworkResult<List<StudentActivityResponseDto>>

    suspend fun getMyActivity(
        year: Int?,
        month: Int?,
    ): NetworkResult<MyActivityResponseDto>

    suspend fun getMyWeeklyActivity(): NetworkResult<WeeklyActivityResponseDto>
}
