package com.nativelap.ecoguard.feature.notification

import com.nativelap.ecoguard.feature.notification.module.data.dto.NotificationListResponseDto
import com.nativelap.ecoguard.feature.notification.module.data.dto.NotificationResponseDto
import com.nativelap.ecoguard.feature.notification.module.data.dto.NotificationSettingResponseDto
import com.nativelap.ecoguard.feature.notification.module.data.mapper.toDomain
import kotlinx.serialization.json.JsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Test

class NotificationMapperTest {
    @Test
    fun `notification inbox maps server fields to domain values`() {
        val inbox =
            NotificationListResponseDto(
                unreadCount = 1,
                notifications =
                    listOf(
                        NotificationResponseDto(
                            id = 42,
                            type = "VERIFICATION_APPROVED",
                            title = "인증 승인",
                            body = "청소 인증이 승인됐어요.",
                            refId = JsonPrimitive(12),
                            createdAt = "2026-11-27T09:30:00+09:00",
                            isRead = false,
                        ),
                    ),
            ).toDomain()

        assertEquals(1, inbox.unreadCount)
        assertEquals(42L, inbox.notifications.single().id)
        assertEquals("VERIFICATION_APPROVED", inbox.notifications.single().type)
        assertEquals("12", inbox.notifications.single().refId)
        assertEquals(false, inbox.notifications.single().isRead)
    }

    @Test
    fun `notification setting maps category label and enabled state`() {
        val setting =
            NotificationSettingResponseDto(
                category = "VERIFICATION",
                label = "인증",
                isEnabled = true,
            ).toDomain()

        assertEquals("VERIFICATION", setting.category)
        assertEquals("인증", setting.label)
        assertEquals(true, setting.isEnabled)
    }
}
