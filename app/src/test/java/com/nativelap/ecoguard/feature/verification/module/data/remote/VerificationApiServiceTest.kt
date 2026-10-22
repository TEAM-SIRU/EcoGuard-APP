package com.nativelap.ecoguard.feature.verification.module.data.remote

import okhttp3.MultipartBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

class VerificationApiServiceTest {
    @Test
    fun `verification endpoint methods preserve Swagger paths and verbs`() {
        assertEquals("api/v1/verifications", method("getManualReviewQueue").getAnnotation(GET::class.java).value)
        assertEquals("api/v1/verifications", method("submitVerification").getAnnotation(POST::class.java).value)
        assertEquals(
            "api/v1/verifications/{verificationId}/review",
            method("getReviewResult").getAnnotation(GET::class.java).value,
        )
        assertEquals(
            "api/v1/verifications/{verificationId}/review",
            method("decideReview").getAnnotation(PATCH::class.java).value,
        )
        assertEquals("api/v1/verifications/today", method("getTodayVerification").getAnnotation(GET::class.java).value)
        assertEquals("api/v1/verifications/me", method("getMyVerifications").getAnnotation(GET::class.java).value)
    }

    @Test
    fun `submit endpoint preserves optional query headers and photo part`() {
        val submitMethod = method("submitVerification")

        assertNotNull(submitMethod.getAnnotation(Multipart::class.java))
        assertEquals(
            "areaId",
            submitMethod.parameterAnnotations[0]
                .filterIsInstance<Query>()
                .single()
                .value,
        )
        assertEquals(
            "Idempotency-Key",
            submitMethod.parameterAnnotations[1]
                .filterIsInstance<Header>()
                .single()
                .value,
        )
        assertEquals(
            "X-Submit-Started-At",
            submitMethod.parameterAnnotations[2]
                .filterIsInstance<Header>()
                .single()
                .value,
        )
        assertEquals(MultipartBody.Part::class.java, submitMethod.parameterTypes[3])
        assertEquals(
            "",
            submitMethod.parameterAnnotations[3]
                .filterIsInstance<Part>()
                .single()
                .value,
        )
    }

    @Test
    fun `review decision endpoint uses path id and JSON body`() {
        val decisionMethod = method("decideReview")

        assertEquals(
            "verificationId",
            decisionMethod.parameterAnnotations[0]
                .filterIsInstance<Path>()
                .single()
                .value,
        )
        assertNotNull(decisionMethod.parameterAnnotations[1].filterIsInstance<Body>().single())
    }

    private fun method(name: String) = VerificationApiService::class.java.methods.single { it.name == name }
}
