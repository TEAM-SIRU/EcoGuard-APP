package com.nativelap.ecoguard.feature.cleaningarea.module.domain.model

data class CleaningArea(
    val areaId: Long,
    val zoneCode: String,
    val name: String,
    val description: String?,
    val cleanTime: String?,
    val semester: SemesterValue?,
    val assignedStudents: List<AssignedStudent>,
    val active: Boolean,
)

data class AssignedStudent(
    val studentId: Long,
    val studentNumber: String?,
    val name: String,
)

data class AreaAssignment(
    val assignmentId: Long?,
    val studentId: Long?,
)

data class MyCleaningAssignment(
    val areaId: Long,
    val zoneCode: String,
    val areaName: String,
    val description: String?,
    val cleanTime: String?,
    val members: List<AssignedStudent>,
)

data class AreaActivationUpdate(
    val active: Boolean? = null,
)

enum class Semester {
    COMMON,
    FIRST,
    SECOND,
    UNKNOWN,
}

data class SemesterValue(
    val value: Semester,
    val rawValue: String,
)
