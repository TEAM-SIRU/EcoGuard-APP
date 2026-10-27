package com.nativelap.ecoguard.feature.user

import com.nativelap.ecoguard.feature.user.module.data.dto.MyProfileResponseDto
import com.nativelap.ecoguard.feature.user.module.data.mapper.toDomain
import com.nativelap.ecoguard.feature.user.module.domain.model.UserRole
import org.junit.Assert.assertEquals
import org.junit.Test

class UserProfileMappingTest {
    @Test
    fun `profile maps server identity and student class values`() {
        val profile = MyProfileResponseDto(42, "민준", "STUDENT", "20301", 2, 3).toDomain()

        assertEquals(42L, profile.userId)
        assertEquals("민준", profile.name)
        assertEquals(UserRole.STUDENT, profile.role)
        assertEquals("20301", profile.studentNumber)
        assertEquals(2, profile.grade)
        assertEquals(3, profile.classNo)
    }

    @Test
    fun `unknown profile role does not prevent profile decoding`() {
        assertEquals(UserRole.UNKNOWN, MyProfileResponseDto(42, "민준", "NEW_ROLE", null, null, null).toDomain().role)
    }
}
