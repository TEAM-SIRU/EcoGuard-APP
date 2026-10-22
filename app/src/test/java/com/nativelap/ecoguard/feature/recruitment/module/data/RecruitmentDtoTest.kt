package com.nativelap.ecoguard.feature.recruitment.module.data

import com.nativelap.ecoguard.feature.recruitment.module.data.dto.ApplyRequestDto
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.RecruitmentSummaryResponseDto
import com.nativelap.ecoguard.feature.recruitment.module.data.dto.UpdateRecruitmentRequestDto
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
    fun `apply request serializes motivation with the API field name`() {
        val body = json.encodeToString(ApplyRequestDto(motivation = "환경 보호에 관심이 있습니다"))

        assertEquals("""{"motivation":"환경 보호에 관심이 있습니다"}""", body)
    }

    @Test
    fun `update request omits nullable values when no update was provided`() {
        val body = json.encodeToString(UpdateRecruitmentRequestDto())

        assertEquals("{}", body)
    }

    @Test
    fun `recruitment summary decodes nested objects and retains unknown enum values`() {
        val response =
            json.decodeFromString<RecruitmentSummaryResponseDto>(
                """{"period":{"start":"2026-10-01T07:30:00","end":"2026-10-07T07:30:00"},"periodStatus":"PAUSED"}""",
            )

        val model = response.toDomain()
        assertEquals("2026-10-01T07:30:00", model.period?.start)
        assertEquals(RecruitmentPeriodStatus.UNKNOWN, model.periodStatus.value)
        assertEquals("PAUSED", model.periodStatus.rawValue)
        assertNull(model.activityTime)
        assertNull(model.full)
    }
}
