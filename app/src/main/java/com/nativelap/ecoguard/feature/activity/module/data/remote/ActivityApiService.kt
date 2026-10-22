package com.nativelap.ecoguard.feature.activity.module.data.remote

import com.nativelap.ecoguard.feature.activity.module.data.dto.MyActivityResponseDto
import com.nativelap.ecoguard.feature.activity.module.data.dto.StudentActivityResponseDto
import com.nativelap.ecoguard.feature.activity.module.data.dto.WeeklyActivityResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ActivityApiService {
    @GET("api/v1/students/{studentId}/activities")
    suspend fun getStudentActivity(
        @Path("studentId") studentId: Long,
        @Query("year") year: Int?,
        @Query("month") month: Int?,
    ): Response<MyActivityResponseDto>

    @GET("api/v1/students/activities")
    suspend fun searchStudents(
        @Query("keyword") keyword: String?,
    ): Response<List<StudentActivityResponseDto>>

    @GET("api/v1/service-times/me")
    suspend fun getMyActivity(
        @Query("year") year: Int?,
        @Query("month") month: Int?,
    ): Response<MyActivityResponseDto>

    @GET("api/v1/service-times/me/weekly")
    suspend fun getMyWeeklyActivity(): Response<WeeklyActivityResponseDto>
}
