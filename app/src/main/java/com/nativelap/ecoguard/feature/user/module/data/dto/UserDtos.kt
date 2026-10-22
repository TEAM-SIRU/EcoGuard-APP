package com.nativelap.ecoguard.feature.user.module.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyProfileResponseDto(
    @SerialName("userId") val userId: Long? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("role") val role: String? = null,
    @SerialName("studentNumber") val studentNumber: String? = null,
    @SerialName("grade") val grade: Int? = null,
    @SerialName("classNo") val classNo: Int? = null,
)
