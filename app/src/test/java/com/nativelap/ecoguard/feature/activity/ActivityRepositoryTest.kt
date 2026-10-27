package com.nativelap.ecoguard.feature.activity

import com.nativelap.ecoguard.core.domain.result.AppError
import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.core.network.NetworkError
import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.feature.activity.module.data.dto.ActivityRecordDto
import com.nativelap.ecoguard.feature.activity.module.data.dto.ActivitySummaryDto
import com.nativelap.ecoguard.feature.activity.module.data.dto.MyActivityResponseDto
import com.nativelap.ecoguard.feature.activity.module.data.dto.StudentActivityResponseDto
import com.nativelap.ecoguard.feature.activity.module.data.mapper.toDomain
import com.nativelap.ecoguard.feature.activity.module.data.remote.ActivityRemoteDataSource
import com.nativelap.ecoguard.feature.activity.module.data.repository.ActivityRepositoryImpl
import com.nativelap.ecoguard.feature.activity.module.domain.model.ActivityResultStatus
import com.nativelap.ecoguard.feature.activity.module.domain.model.MyActivity
import com.nativelap.ecoguard.feature.activity.module.domain.repository.ActivityRepository
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ActivityRepositoryTest {
    @Test
    fun `monthly response decodes swagger fields and local date`() {
        val response =
            Json.decodeFromString<MyActivityResponseDto>(
                """
                {
                  "totalMinutes": 35,
                  "year": 2026,
                  "month": 10,
                  "monthlyMinutes": 10,
                  "summary": {
                    "completedDays": 1,
                    "requiredDays": 5,
                    "approvedCount": 1,
                    "rejectedCount": 0,
                    "notSubmittedCount": 0
                  },
                  "records": [{
                    "date": "2026-10-23",
                    "area": "본관 복도",
                    "result": "APPROVED",
                    "verificationId": 20,
                    "photoUrl": null,
                    "minutes": 10
                  }]
                }
                """.trimIndent(),
            )

        assertEquals("2026-10-23", response.records.single().date)
        assertEquals(1, response.summary.approvedCount)
    }

    @Test
    fun `monthly response maps required server fields and records`() {
        val response =
            MyActivityResponseDto(
                totalMinutes = 35,
                year = 2026,
                month = 10,
                monthlyMinutes = 10,
                summary = ActivitySummaryDto(1, 5, 1, 0, 0),
                records =
                    listOf(
                        ActivityRecordDto(
                            date = "2026-10-23",
                            area = "본관 2층 복도",
                            result = "APPROVED",
                            verificationId = 91,
                            photoUrl = null,
                            minutes = 10,
                        ),
                    ),
            )

        val mappedActivity = response.toDomain()

        assertEquals(35L, mappedActivity.totalMinutes)
        assertEquals(2026, mappedActivity.year)
        assertEquals(10, mappedActivity.month)
        assertEquals(1, mappedActivity.summary.approvedCount)
        assertEquals(ActivityResultStatus.APPROVED, mappedActivity.records.single().result)
        assertEquals(91L, mappedActivity.records.single().verificationId)
    }

    @Test
    fun `repository returns mapped monthly activity`() =
        runBlocking {
            val repository: ActivityRepository = ActivityRepositoryImpl(FakeActivityRemoteDataSource())

            val result = repository.getMyActivity(2026, 10)

            assertTrue(result is AppResult.Success)
            assertEquals(2026, (result as AppResult.Success).value.year)
        }

    @Test
    fun `repository preserves request failure`() =
        runBlocking {
            val repository =
                ActivityRepositoryImpl(FakeActivityRemoteDataSource(NetworkResult.Failure(NetworkError.Http(503))))

            val result = repository.getMyActivity(2026, 10)

            assertEquals(AppResult.Failure(AppError.Http(503)), result)
        }

    private class FakeActivityRemoteDataSource(
        private val result: NetworkResult<MyActivityResponseDto> =
            NetworkResult.Success(
                MyActivityResponseDto(
                    totalMinutes = 35,
                    year = 2026,
                    month = 10,
                    monthlyMinutes = 10,
                    summary = ActivitySummaryDto(1, 5, 1, 0, 0),
                    records = emptyList(),
                ),
            ),
    ) : ActivityRemoteDataSource {
        override suspend fun getStudentActivity(
            studentId: Long,
            year: Int?,
            month: Int?,
        ) = result

        override suspend fun searchStudents(keyword: String?) =
            NetworkResult.Success(emptyList<StudentActivityResponseDto>())

        override suspend fun getMyActivity(
            year: Int?,
            month: Int?,
        ) = result

        override suspend fun getMyWeeklyActivity() =
            NetworkResult.Success(
                com.nativelap.ecoguard.feature.activity.module.data.dto.WeeklyActivityResponseDto(
                    weekStart = "2026-10-19",
                    weekEnd = "2026-10-25",
                    completedDays = 1,
                    requiredDays = 5,
                    days = emptyList(),
                ),
            )
    }
}
