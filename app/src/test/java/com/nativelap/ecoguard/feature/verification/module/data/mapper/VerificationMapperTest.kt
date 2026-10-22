package com.nativelap.ecoguard.feature.verification.module.data.mapper

import com.nativelap.ecoguard.feature.verification.module.data.dto.ManualReviewDecisionRequestDto
import com.nativelap.ecoguard.feature.verification.module.data.dto.ReviewResultResponseDto
import com.nativelap.ecoguard.feature.verification.module.data.dto.SubmitVerificationResponseDto
import com.nativelap.ecoguard.feature.verification.module.data.dto.TodayVerificationResponseDto
import com.nativelap.ecoguard.feature.verification.module.domain.model.VerificationStatus
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class VerificationMapperTest {
    private val json = Json

    @Test
    fun `response DTO decodes missing optional properties`() {
        val response = json.decodeFromString<SubmitVerificationResponseDto>("{}")

        assertNull(response.verificationId)
        assertNull(response.status)
        assertNull(response.submittedAt)
    }

    @Test
    fun `request DTO uses exact OpenAPI property names and enum value`() {
        val encoded = json.encodeToString(ManualReviewDecisionRequestDto("APPROVED", "verified"))
        val objectValue = json.parseToJsonElement(encoded).jsonObject

        assertEquals("APPROVED", objectValue.getValue("decision").jsonPrimitive.content)
        assertEquals("verified", objectValue.getValue("reason").jsonPrimitive.content)
    }

    @Test
    fun `unknown review status is retained while mapped to unknown`() {
        val model = ReviewResultResponseDto(status = "NEW_SERVER_STATUS").toDomain()

        assertEquals(VerificationStatus.UNKNOWN, model.status?.value)
        assertEquals("NEW_SERVER_STATUS", model.status?.rawValue)
    }

    @Test
    fun `today response preserves absent optional values`() {
        val response = json.decodeFromString<TodayVerificationResponseDto>("{}")
        val model = response.toDomain()

        assertNull(model.areaId)
        assertNull(model.canSubmit)
        assertNull(model.status)
        assertNull(model.unavailableReason)
    }
}
