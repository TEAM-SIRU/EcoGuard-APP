package com.nativelap.ecoguard.feature.cleaningarea

import com.nativelap.ecoguard.core.domain.result.AppError
import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.core.network.NetworkError
import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.feature.cleaningarea.module.data.dto.AreaMapResponseDto
import com.nativelap.ecoguard.feature.cleaningarea.module.data.dto.AssignedStudentResponseDto
import com.nativelap.ecoguard.feature.cleaningarea.module.data.dto.AssignmentResponseDto
import com.nativelap.ecoguard.feature.cleaningarea.module.data.dto.MyAssignmentResponseDto
import com.nativelap.ecoguard.feature.cleaningarea.module.data.mapper.toDomain
import com.nativelap.ecoguard.feature.cleaningarea.module.data.remote.CleaningAreaRemoteDataSource
import com.nativelap.ecoguard.feature.cleaningarea.module.data.repository.CleaningAreaRepositoryImpl
import com.nativelap.ecoguard.feature.cleaningarea.module.domain.repository.CleaningAreaRepository
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CleaningAreaRepositoryTest {
    @Test
    fun `cleaning area response decodes swagger active field`() {
        val response =
            Json.decodeFromString<AreaMapResponseDto>(
                """
                {
                  "areaId": 12,
                  "zoneCode": "main_stair_a",
                  "name": "본관 계단 A",
                  "description": null,
                  "cleanTime": "08:00-08:10",
                  "semester": "FIRST",
                  "assignedStudents": [],
                  "active": true
                }
                """.trimIndent(),
            )

        assertTrue(response.active)
        assertEquals("main_stair_a", response.zoneCode)
    }

    @Test
    fun `assignment response maps exact server fields`() {
        val response =
            MyAssignmentResponseDto(
                areaId = 12,
                zoneCode = "main_stair_a",
                areaName = "본관 계단 A",
                description = "바닥 청소",
                cleanTime = "08:00-08:10",
                members =
                    listOf(
                        AssignedStudentResponseDto(
                            studentId = 6,
                            studentNumber = "10203",
                            name = "홍길동",
                        ),
                    ),
            )

        val assignment = response.toDomain()

        assertEquals(12L, assignment.areaId)
        assertEquals("main_stair_a", assignment.zoneCode)
        assertEquals("본관 계단 A", assignment.areaName)
        assertEquals("08:00-08:10", assignment.cleanTime)
        assertEquals("홍길동", assignment.members.single().name)
    }

    @Test
    fun `repository returns the student's assignment`() =
        runBlocking {
            val repository: CleaningAreaRepository = CleaningAreaRepositoryImpl(FakeCleaningAreaRemoteDataSource())

            val result = repository.getMyAssignment()

            assertTrue(result is AppResult.Success)
            assertEquals(12L, (result as AppResult.Success).value.areaId)
        }

    @Test
    fun `repository preserves no assignment response status`() =
        runBlocking {
            val repository =
                CleaningAreaRepositoryImpl(
                    FakeCleaningAreaRemoteDataSource(NetworkResult.Failure(NetworkError.Http(404))),
                )

            val result = repository.getMyAssignment()

            assertEquals(AppResult.Failure(AppError.Http(404)), result)
        }

    private class FakeCleaningAreaRemoteDataSource(
        private val assignmentResult: NetworkResult<MyAssignmentResponseDto> =
            NetworkResult.Success(
                MyAssignmentResponseDto(
                    areaId = 12,
                    zoneCode = "main_stair_a",
                    areaName = "본관 계단 A",
                    description = "바닥 청소",
                    cleanTime = "08:00-08:10",
                    members = listOf(AssignedStudentResponseDto(6, "10203", "홍길동")),
                ),
            ),
    ) : CleaningAreaRemoteDataSource {
        override suspend fun getAreas() = NetworkResult.Success(emptyList<AreaMapResponseDto>())

        override suspend fun assignStudents(
            areaId: Long,
            studentIds: List<Long>,
        ) = NetworkResult.Success(emptyList<AssignmentResponseDto>())

        override suspend fun updateArea(
            areaId: Long,
            active: Boolean?,
        ) = NetworkResult.Success(Unit)

        override suspend fun unassignStudent(
            areaId: Long,
            studentId: Long,
        ) = NetworkResult.Success(Unit)

        override suspend fun getMyAssignment() = assignmentResult
    }
}
