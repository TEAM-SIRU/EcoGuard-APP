package com.nativelap.ecoguard.feature.notice

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.core.network.NetworkResult
import com.nativelap.ecoguard.feature.notice.module.data.dto.CreateNoticeRequestDto
import com.nativelap.ecoguard.feature.notice.module.data.dto.NoticeDetailResponseDto
import com.nativelap.ecoguard.feature.notice.module.data.dto.NoticeListItemResponseDto
import com.nativelap.ecoguard.feature.notice.module.data.dto.UpdateNoticeRequestDto
import com.nativelap.ecoguard.feature.notice.module.data.mapper.toDomain
import com.nativelap.ecoguard.feature.notice.module.data.remote.NoticeRemoteDataSource
import com.nativelap.ecoguard.feature.notice.module.data.repository.NoticeRepositoryImpl
import com.nativelap.ecoguard.feature.notice.viewmodel.toUiModel
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class NoticeContractMappingTest {
    @Test
    fun `notice detail accepts omitted previous and next IDs`() {
        val response =
            Json.decodeFromString<NoticeDetailResponseDto>(
                """{
                    "noticeId":23,
                    "title":"Safety",
                    "content":"Keep it clean",
                    "createdAt":"2026-10-25T12:34:56"
                }""",
            )

        assertNull(response.previousNoticeId)
        assertNull(response.nextNoticeId)
    }

    @Test
    fun `notice list decodes isRead from server contract and maps preview to UI`() {
        val response =
            Json.decodeFromString<NoticeListItemResponseDto>(
                """{
                    "noticeId":23,
                    "title":"Safety",
                    "preview":"Keep it clean",
                    "createdAt":"2026-10-25T12:34:56",
                    "read":true
                }""",
            )

        assertEquals(true, response.isRead)
        assertEquals("Keep it clean", response.toDomain().toUiModel().body)
        assertEquals("2026.10.25", response.toDomain().toUiModel().publishedDate)
        assertEquals(true, response.toDomain().toUiModel().isRead)
    }

    @Test
    fun `notice repository maps server list records without changing IDs`() =
        runBlocking {
            val repository = NoticeRepositoryImpl(FakeNoticeRemoteDataSource())

            val result = repository.getNotices()

            assertEquals(
                AppResult.Success(
                    listOf(
                        NoticeListItemResponseDto(
                            23,
                            "Safety",
                            "Keep it clean",
                            "2026-10-25T12:34:56",
                            true,
                        ).toDomain(),
                    ),
                ),
                result,
            )
        }

    private class FakeNoticeRemoteDataSource : NoticeRemoteDataSource {
        override suspend fun getNotices() =
            NetworkResult.Success(
                listOf(NoticeListItemResponseDto(23, "Safety", "Keep it clean", "2026-10-25T12:34:56", true)),
            )

        override suspend fun createNotice(request: CreateNoticeRequestDto) = error("Not used in this test")

        override suspend fun getNoticeDetail(noticeId: Long) = error("Not used in this test")

        override suspend fun deleteNotice(noticeId: Long) = error("Not used in this test")

        override suspend fun updateNotice(
            noticeId: Long,
            request: UpdateNoticeRequestDto,
        ) = error("Not used in this test")
    }
}
