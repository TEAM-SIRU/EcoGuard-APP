package com.nativelap.ecoguard.feature.cleaningarea.module.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AssignStudentsRequestDto(
    @SerialName("studentIds") val studentIds: List<Long>,
)

@Serializable
data class AssignmentResponseDto(
    @SerialName("assignmentId") val assignmentId: Long? = null,
    @SerialName("studentId") val studentId: Long? = null,
)

@Serializable
data class UpdateAreaRequestDto(
    @SerialName("active") val active: Boolean? = null,
)

@Serializable
data class AreaMapResponseDto(
    @SerialName("areaId") val areaId: Long,
    @SerialName("zoneCode") val zoneCode: String,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String? = null,
    @SerialName("cleanTime") val cleanTime: String? = null,
    @SerialName("semester") val semester: String,
    @SerialName("assignedStudents") val assignedStudents: List<AssignedStudentResponseDto>,
    @SerialName("active") val active: Boolean,
)

@Serializable
data class AssignedStudentResponseDto(
    @SerialName("studentId") val studentId: Long,
    @SerialName("studentNumber") val studentNumber: String? = null,
    @SerialName("name") val name: String,
)

@Serializable
data class MyAssignmentResponseDto(
    @SerialName("areaId") val areaId: Long,
    @SerialName("zoneCode") val zoneCode: String,
    @SerialName("areaName") val areaName: String,
    @SerialName("description") val description: String? = null,
    @SerialName("cleanTime") val cleanTime: String? = null,
    @SerialName("members") val members: List<AssignedStudentResponseDto>,
)
