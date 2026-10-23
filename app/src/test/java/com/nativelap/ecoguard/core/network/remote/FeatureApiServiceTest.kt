package com.nativelap.ecoguard.core.network.remote

import com.nativelap.ecoguard.feature.activity.module.data.remote.ActivityApiService
import com.nativelap.ecoguard.feature.appeal.module.data.dto.CreateAppealRequestDto
import com.nativelap.ecoguard.feature.appeal.module.data.remote.AppealApiService
import com.nativelap.ecoguard.feature.cleaningarea.module.data.remote.CleaningAreaApiService
import com.nativelap.ecoguard.feature.notice.module.data.remote.NoticeApiService
import com.nativelap.ecoguard.feature.recruitment.module.data.remote.RecruitmentApiService
import com.nativelap.ecoguard.feature.user.module.data.remote.UserApiService
import com.nativelap.ecoguard.feature.verification.module.data.remote.VerificationApiService
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import okio.Timeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.lang.reflect.Proxy
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine

class FeatureApiServiceTest {
    @Test
    fun `feature services match every non OAuth Swagger operation contract`() {
        val actualContracts =
            featureApiServiceTypes.flatMap { serviceType ->
                serviceType.declaredMethods.map { method ->
                    method.toApiContract(serviceType.simpleName)
                }
            }

        assertEquals(36, expectedApiContracts.size)
        assertEquals(35, expectedApiContracts.map { it.verb to it.path }.toSet().size)
        assertEquals(expectedApiContracts.size, actualContracts.size)
        assertEquals(expectedApiContracts.toSet(), actualContracts.toSet())
    }

    @Test
    fun `activity service maps student id and query parameters`() =
        runSuspendTest {
            val callFactory = RecordingCallFactory(body = "{}")
            val service = createService<ActivityApiService>(callFactory)

            service.getStudentActivity(studentId = 74, year = 2026, month = 10)

            val request = checkNotNull(callFactory.lastRequest)
            assertEquals("GET", request.method)
            assertEquals("/api/v1/students/74/activities", request.url.encodedPath)
            assertEquals("2026", request.url.queryParameter("year"))
            assertEquals("10", request.url.queryParameter("month"))
        }

    @Test
    fun `recruitment service maps applicant path parameter`() =
        runSuspendTest {
            val callFactory = RecordingCallFactory(body = "[]")
            val service = createService<RecruitmentApiService>(callFactory)

            service.getApplicants(recruitmentId = 81)

            val request = checkNotNull(callFactory.lastRequest)
            assertEquals("GET", request.method)
            assertEquals("/api/v1/recruitments/81/applications", request.url.encodedPath)
        }

    @Test
    fun `verification service maps custom headers and upload path`() =
        runSuspendTest {
            val callFactory = RecordingCallFactory(body = "{}")
            val service = createService<VerificationApiService>(callFactory)
            val photo =
                MultipartBody.Part.createFormData(
                    "photo",
                    "proof.jpg",
                    "image-bytes".toRequestBody("image/jpeg".toMediaType()),
                )

            service.submitVerification(
                areaId = 9,
                idempotencyKey = "request-42",
                submitStartedAt = "2026-10-23T10:15:30Z",
                photo = photo,
            )

            val request = checkNotNull(callFactory.lastRequest)
            assertEquals("POST", request.method)
            assertEquals("/api/v1/verifications", request.url.encodedPath)
            assertEquals("9", request.url.queryParameter("areaId"))
            assertEquals("request-42", request.header("Idempotency-Key"))
            assertEquals("2026-10-23T10:15:30Z", request.header("X-Submit-Started-At"))
            val multipartPayload = Buffer()
            checkNotNull(request.body).writeTo(multipartPayload)
            assertTrue(
                request.body
                    ?.contentType()
                    .toString()
                    .startsWith("multipart/form-data"),
            )
            assertTrue(multipartPayload.readUtf8().contains("name=\"photo\""))
        }

    @Test
    fun `appeal service sends JSON request with application JSON content type`() =
        runSuspendTest {
            val callFactory = RecordingCallFactory(body = null, statusCode = 204)
            val service = createService<AppealApiService>(callFactory)

            service.createAppeal(
                verificationId = 23,
                request = CreateAppealRequestDto(content = "검토 요청"),
            )

            val request = checkNotNull(callFactory.lastRequest)
            assertEquals("POST", request.method)
            assertEquals("/api/v1/verifications/23/appeals", request.url.encodedPath)
            val contentType = checkNotNull(request.body?.contentType())
            assertEquals("application", contentType.type)
            assertEquals("json", contentType.subtype)
        }

    @Test
    fun `appeal service sends photo request as multipart form data`() =
        runSuspendTest {
            val callFactory = RecordingCallFactory(body = null, statusCode = 204)
            val service = createService<AppealApiService>(callFactory)
            val photo =
                MultipartBody.Part.createFormData(
                    "photos",
                    "appeal.jpg",
                    "image-bytes".toRequestBody("image/jpeg".toMediaType()),
                )

            service.createAppealWithPhotos(
                verificationId = 23,
                content = "검토 요청",
                photos = listOf(photo),
            )

            val request = checkNotNull(callFactory.lastRequest)
            assertEquals("POST", request.method)
            assertEquals("/api/v1/verifications/23/appeals", request.url.encodedPath)
            assertEquals("검토 요청", request.url.queryParameter("content"))
            val multipartPayload = Buffer()
            checkNotNull(request.body).writeTo(multipartPayload)
            assertTrue(
                request.body
                    ?.contentType()
                    .toString()
                    .startsWith("multipart/form-data"),
            )
            assertTrue(multipartPayload.readUtf8().contains("name=\"photos\""))
        }

    private inline fun <reified T : Any> createService(callFactory: RecordingCallFactory): T {
        val json =
            Json {
                ignoreUnknownKeys = true
                explicitNulls = false
                encodeDefaults = false
            }
        return Retrofit
            .Builder()
            .baseUrl("https://example.test/")
            .callFactory(callFactory)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(T::class.java)
    }

    private fun java.lang.reflect.Method.toApiContract(serviceName: String): ApiContract {
        val httpOperation =
            annotations.firstNotNullOf { annotation ->
                when (annotation) {
                    is GET -> "GET" to annotation.value
                    is POST -> "POST" to annotation.value
                    is PATCH -> "PATCH" to annotation.value
                    is DELETE -> "DELETE" to annotation.value
                    else -> null
                }
            }
        val parameterContracts =
            parameterAnnotations
                .zip(parameterTypes)
                .filterNot { (_, parameterType) ->
                    parameterType.name == "kotlin.coroutines.Continuation"
                }.flatMap { (annotations, _) -> annotations.mapNotNull { it.toParameterContract() } }

        return ApiContract(
            serviceName = serviceName,
            methodName = name,
            verb = httpOperation.first,
            path = httpOperation.second,
            parameters = parameterContracts,
            isMultipart = isAnnotationPresent(Multipart::class.java),
        )
    }

    private fun Annotation.toParameterContract(): String? =
        when (this) {
            is Path -> "Path:$value"
            is Query -> "Query:$value"
            is Header -> "Header:$value"
            is Part -> value.takeIf(String::isNotEmpty)?.let { "Part:$it" } ?: "Part"
            is Body -> "Body"
            else -> null
        }

    private data class ApiContract(
        val serviceName: String,
        val methodName: String,
        val verb: String,
        val path: String,
        val parameters: List<String>,
        val isMultipart: Boolean,
    )

    private companion object {
        val featureApiServiceTypes =
            listOf(
                ActivityApiService::class.java,
                AppealApiService::class.java,
                CleaningAreaApiService::class.java,
                NoticeApiService::class.java,
                RecruitmentApiService::class.java,
                UserApiService::class.java,
                VerificationApiService::class.java,
            )

        val expectedApiContracts =
            listOf(
                contract(
                    "ActivityApiService",
                    "getStudentActivity",
                    "GET",
                    "api/v1/students/{studentId}/activities",
                    "Path:studentId",
                    "Query:year",
                    "Query:month",
                ),
                contract("ActivityApiService", "searchStudents", "GET", "api/v1/students/activities", "Query:keyword"),
                contract(
                    "ActivityApiService",
                    "getMyActivity",
                    "GET",
                    "api/v1/service-times/me",
                    "Query:year",
                    "Query:month",
                ),
                contract("ActivityApiService", "getMyWeeklyActivity", "GET", "api/v1/service-times/me/weekly"),
                contract(
                    "AppealApiService",
                    "createAppealWithPhotos",
                    "POST",
                    "api/v1/verifications/{verificationId}/appeals",
                    "Path:verificationId",
                    "Query:content",
                    "Part",
                    isMultipart = true,
                ),
                contract(
                    "AppealApiService",
                    "createAppeal",
                    "POST",
                    "api/v1/verifications/{verificationId}/appeals",
                    "Path:verificationId",
                    "Body",
                ),
                contract("AppealApiService", "getAppeals", "GET", "api/v1/appeals", "Query:status"),
                contract("AppealApiService", "getMyAppeals", "GET", "api/v1/appeals/me"),
                contract(
                    "AppealApiService",
                    "decideAppeal",
                    "PATCH",
                    "api/v1/appeals/{appealId}",
                    "Path:appealId",
                    "Body",
                ),
                contract("CleaningAreaApiService", "getAreas", "GET", "api/v1/cleaning-areas"),
                contract(
                    "CleaningAreaApiService",
                    "assignStudents",
                    "POST",
                    "api/v1/cleaning-areas/{areaId}/assignments",
                    "Path:areaId",
                    "Body",
                ),
                contract(
                    "CleaningAreaApiService",
                    "updateArea",
                    "PATCH",
                    "api/v1/cleaning-areas/{areaId}",
                    "Path:areaId",
                    "Body",
                ),
                contract(
                    "CleaningAreaApiService",
                    "unassignStudent",
                    "DELETE",
                    "api/v1/cleaning-areas/{areaId}/assignments/{studentId}",
                    "Path:areaId",
                    "Path:studentId",
                ),
                contract("CleaningAreaApiService", "getMyAssignment", "GET", "api/v1/assignments/me"),
                contract("NoticeApiService", "getNotices", "GET", "api/v1/notices"),
                contract("NoticeApiService", "createNotice", "POST", "api/v1/notices", "Body"),
                contract("NoticeApiService", "getNoticeDetail", "GET", "api/v1/notices/{noticeId}", "Path:noticeId"),
                contract("NoticeApiService", "deleteNotice", "DELETE", "api/v1/notices/{noticeId}", "Path:noticeId"),
                contract(
                    "NoticeApiService",
                    "updateNotice",
                    "PATCH",
                    "api/v1/notices/{noticeId}",
                    "Path:noticeId",
                    "Body",
                ),
                contract("RecruitmentApiService", "getRecruitments", "GET", "api/v1/recruitments"),
                contract("RecruitmentApiService", "createRecruitment", "POST", "api/v1/recruitments", "Body"),
                contract(
                    "RecruitmentApiService",
                    "getApplicants",
                    "GET",
                    "api/v1/recruitments/{recruitmentId}/applications",
                    "Path:recruitmentId",
                ),
                contract(
                    "RecruitmentApiService",
                    "apply",
                    "POST",
                    "api/v1/recruitments/{recruitmentId}/applications",
                    "Path:recruitmentId",
                    "Body",
                ),
                contract(
                    "RecruitmentApiService",
                    "deleteRecruitment",
                    "DELETE",
                    "api/v1/recruitments/{recruitmentId}",
                    "Path:recruitmentId",
                ),
                contract(
                    "RecruitmentApiService",
                    "updateRecruitment",
                    "PATCH",
                    "api/v1/recruitments/{recruitmentId}",
                    "Path:recruitmentId",
                    "Body",
                ),
                contract("RecruitmentApiService", "getCurrentRecruitment", "GET", "api/v1/recruitments/current"),
                contract("RecruitmentApiService", "getMyApplicationStatus", "GET", "api/v1/applications/me"),
                contract("UserApiService", "getMyProfile", "GET", "api/v1/users/me"),
                contract("UserApiService", "withdraw", "DELETE", "api/v1/users/me"),
                contract("UserApiService", "logout", "POST", "api/v1/auth/logout", "Header:Authorization"),
                contract("VerificationApiService", "getManualReviewQueue", "GET", "api/v1/verifications"),
                contract(
                    "VerificationApiService",
                    "submitVerification",
                    "POST",
                    "api/v1/verifications",
                    "Query:areaId",
                    "Header:Idempotency-Key",
                    "Header:X-Submit-Started-At",
                    "Part",
                    isMultipart = true,
                ),
                contract(
                    "VerificationApiService",
                    "getReviewResult",
                    "GET",
                    "api/v1/verifications/{verificationId}/review",
                    "Path:verificationId",
                ),
                contract(
                    "VerificationApiService",
                    "decideReview",
                    "PATCH",
                    "api/v1/verifications/{verificationId}/review",
                    "Path:verificationId",
                    "Body",
                ),
                contract("VerificationApiService", "getTodayVerification", "GET", "api/v1/verifications/today"),
                contract("VerificationApiService", "getMyVerifications", "GET", "api/v1/verifications/me"),
            )

        private fun contract(
            serviceName: String,
            methodName: String,
            verb: String,
            path: String,
            vararg parameters: String,
            isMultipart: Boolean = false,
        ) = ApiContract(
            serviceName = serviceName,
            methodName = methodName,
            verb = verb,
            path = path,
            parameters = parameters.toList(),
            isMultipart = isMultipart,
        )
    }
}

private class RecordingCallFactory(
    private val body: String?,
    private val statusCode: Int = 200,
) : Call.Factory {
    var lastRequest: Request? = null
        private set

    override fun newCall(request: Request): Call {
        lastRequest = request
        return Proxy.newProxyInstance(
            Call::class.java.classLoader,
            arrayOf(Call::class.java),
        ) { _, method, arguments ->
            when (method.name) {
                "request" -> {
                    request
                }

                "execute" -> {
                    buildResponse(request, body, statusCode)
                }

                "enqueue" -> {
                    val callback = arguments?.get(0) as Callback
                    callback.onResponse(
                        newCall(request),
                        buildResponse(request, body, statusCode),
                    )
                    Unit
                }

                "cancel", "addEventListener" -> {
                    Unit
                }

                "isExecuted" -> {
                    true
                }

                "isCanceled" -> {
                    false
                }

                "timeout" -> {
                    Timeout.NONE
                }

                "clone" -> {
                    newCall(request)
                }

                "tag" -> {
                    null
                }

                else -> {
                    error("Unexpected call method: ${method.name}")
                }
            }
        } as Call
    }
}

private fun buildResponse(
    request: Request,
    responseBody: String?,
    statusCode: Int,
): okhttp3.Response {
    val responseBuilder =
        okhttp3.Response
            .Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(statusCode)
            .message("Test response")
    responseBody?.let {
        responseBuilder.body(it.toResponseBody("application/json".toMediaType()))
    }
    return responseBuilder.build()
}

private fun <T> runSuspendTest(block: suspend () -> T): T {
    var result: Result<T>? = null
    val completed = CountDownLatch(1)
    block.startCoroutine(
        object : Continuation<T> {
            override val context = EmptyCoroutineContext

            override fun resumeWith(resumeResult: Result<T>) {
                result = resumeResult
                completed.countDown()
            }
        },
    )
    check(completed.await(5, TimeUnit.SECONDS)) { "Suspend test did not complete" }
    return checkNotNull(result).getOrThrow()
}
