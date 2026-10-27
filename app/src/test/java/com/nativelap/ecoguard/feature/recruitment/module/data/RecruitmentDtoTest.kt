package com.nativelap.ecoguard.feature.recruitment.module.data

import com.nativelap.ecoguard.feature.recruitment.module.data.dto.ApplyRequestDto
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.ApplyResponseDto
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.CurrentRecruitmentResponseDto
import com.nativelap.ecoguard.feature.recruitment.module.data.mapper.toDomain
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.RecruitmentPeriodStatus
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class RecruitmentDtoTest {
    private val json =
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
            encodeDefaults = false
        }

    @Test
    fun `apply request serializes motivation with API field name`() {
        val body = json.encodeToString(ApplyRequestDto(motivation = "환경 보호에 관심이 있습니다"))

        assertEquals("""{"motivation":"환경 보호에 관심이 있습니다"}""", body)
    }

    @Test
    fun `current recruitment decodes Swagger full field and nested values`() {
        val response =
            json.decodeFromString<CurrentRecruitmentResponseDto>(
                """{"recruitmentId":12,"semester":"2026-2","grade":3,"classNo":2,"period":{"start":"2026-10-01T07:30:00","end":"2026-10-07T07:30:00"},"activityTime":{"start":"12:30","end":"13:00"},"periodStatus":"OPEN","maxCount":20,"currentApplicants":8,"alreadyApplied":false,"full":true}""",
            )

        val model = response.toDomain()
        assertEquals(12L, model.recruitmentId)
        assertEquals(3, model.grade)
        assertEquals("2026-10-01T07:30:00", model.period?.start)
        assertEquals("12:30", model.activityTime?.start)
        assertEquals(RecruitmentPeriodStatus.OPEN, model.periodStatus.value)
        assertEquals(true, model.full)
    }

    @Test
    fun `apply response preserves nullable student number as string`() {
        val response =
            json.decodeFromString<ApplyResponseDto>(
                """{"applicationId":42,"status":"PENDING","order":3,"studentNumber":"30207","name":"학생","appliedAt":"2026-10-24T10:15:00"}""",
            )

        assertEquals(42L, response.applicationId)
        assertEquals("PENDING", response.status)
        assertEquals(3L, response.order)
        assertEquals("30207", response.studentNumber)
        assertEquals("2026-10-24T10:15:00", response.appliedAt)
    }

    @Test
    fun `current recruitment retains unknown period status and null optional fields`() {
        val model =
            json
                .decodeFromString<CurrentRecruitmentResponseDto>(
                    """{"periodStatus":"PAUSED"}""",
                ).toDomain()

        assertEquals(RecruitmentPeriodStatus.UNKNOWN, model.periodStatus.value)
        assertEquals("PAUSED", model.periodStatus.rawValue)
        assertNull(model.full)
        assertNull(model.activityTime)
    }
}
