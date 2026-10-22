package com.nativelap.ecoguard.feature.cleaningarea.module.data.remote

import com.nativelap.ecoguard.feature.cleaningarea.module.data.dto.AreaMapResponseDto
import com.nativelap.ecoguard.feature.cleaningarea.module.data.dto.AssignStudentsRequestDto
import com.nativelap.ecoguard.feature.cleaningarea.module.data.dto.AssignmentResponseDto
import com.nativelap.ecoguard.feature.cleaningarea.module.data.dto.MyAssignmentResponseDto
import com.nativelap.ecoguard.feature.cleaningarea.module.data.dto.UpdateAreaRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface CleaningAreaApiService {
    @GET("api/v1/cleaning-areas")
    suspend fun getAreas(): Response<List<AreaMapResponseDto>>

    @POST("api/v1/cleaning-areas/{areaId}/assignments")
    suspend fun assignStudents(
        @Path("areaId") areaId: Long,
        @Body request: AssignStudentsRequestDto,
    ): Response<List<AssignmentResponseDto>>

    @PATCH("api/v1/cleaning-areas/{areaId}")
    suspend fun updateArea(
        @Path("areaId") areaId: Long,
        @Body request: UpdateAreaRequestDto,
    ): Response<Unit>

    @DELETE("api/v1/cleaning-areas/{areaId}/assignments/{studentId}")
    suspend fun unassignStudent(
        @Path("areaId") areaId: Long,
        @Path("studentId") studentId: Long,
    ): Response<Unit>

    @GET("api/v1/assignments/me")
    suspend fun getMyAssignment(): Response<MyAssignmentResponseDto>
}
