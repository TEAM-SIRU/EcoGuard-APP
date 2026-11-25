package com.nativelap.ecoguard.feature.notification

import com.nativelap.ecoguard.core.network.ApiExecutor
import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.feature.notification.module.data.NotificationRepositoryImpl
import com.nativelap.ecoguard.feature.notification.module.data.remote.NotificationApiService
import com.nativelap.ecoguard.feature.notification.module.data.remote.NotificationRemoteDataSourceImpl
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class NotificationRepositoryTest {
    @Test
    fun `lists inbox and maps unread count and notification details`() =
        runBlocking {
            val requests = mutableListOf<Request>()
            val repository = repository(requests)
            val response = repository.getInbox()
            check(response is NetworkResult.Success)

            val item = response.value.notifications.single()
            assertEquals(1L, response.value.unreadCount)
            assertEquals(42L, item.id)
            assertEquals("새 공지", item.body)
            assertFalse(item.isRead)
            assertEquals("GET", requests.single().method)
            assertEquals("/api/v1/notifications", requests.single().url.encodedPath)
        }

    @Test
    fun `sends individual and bulk read requests and accepts empty responses`() =
        runBlocking {
            val requests = mutableListOf<Request>()
            val repository = repository(requests)
            assertEquals(NetworkResult.Success(Unit), repository.markRead(42))
            assertEquals(NetworkResult.Success(Unit), repository.markAllRead())

            assertEquals(
                listOf("/api/v1/notifications/42/read", "/api/v1/notifications/read-all"),
                requests.map { it.url.encodedPath },
            )
            assertEquals(listOf("PATCH", "PATCH"), requests.map { it.method })
        }

    private fun repository(requests: MutableList<Request>): NotificationRepositoryImpl {
        val jsonType = "application/json".toMediaType()
        val client =
            OkHttpClient
                .Builder()
                .addInterceptor { chain ->
                    val request = chain.request()
                    requests.add(request)
                    val body =
                        if (request.method == "GET") {
                            """
                            {"unreadCount":1,"notifications":[{"id":42,"type":"NOTICE_CREATED",
                              "title":"공지","body":"새 공지","refId":7,"isRead":false,"createdAt":"2026-11-25T09:00:00"}]}
                            """.trimIndent()
                        } else {
                            ""
                        }
                    Response
                        .Builder()
                        .request(request)
                        .protocol(Protocol.HTTP_1_1)
                        .code(200)
                        .message("OK")
                        .body(body.toResponseBody(jsonType))
                        .build()
                }.build()
        val api =
            Retrofit
                .Builder()
                .baseUrl("https://api.example.com/")
                .client(client)
                .addConverterFactory(Json { ignoreUnknownKeys = true }.asConverterFactory(jsonType))
                .build()
                .create(NotificationApiService::class.java)
        return NotificationRepositoryImpl(NotificationRemoteDataSourceImpl(api, ApiExecutor()))
    }
}
