package com.nativelap.ecoguard.feature.cleaningarea.module.data.mapper

import com.nativelap.ecoguard.feature.cleaningarea.module.data.dto.AreaMapResponseDto
import com.nativelap.ecoguard.feature.cleaningarea.module.data.dto.AssignStudentsRequestDto
import com.nativelap.ecoguard.feature.cleaningarea.module.data.dto.AssignedStudentResponseDto
import com.nativelap.ecoguard.feature.cleaningarea.module.data.dto.AssignmentResponseDto
import com.nativelap.ecoguard.feature.cleaningarea.module.data.dto.MyAssignmentResponseDto
import com.nativelap.ecoguard.feature.cleaningarea.module.data.dto.UpdateAreaRequestDto
import com.nativelap.ecoguard.feature.cleaningarea.module.domain.model.AreaActivationUpdate
import com.nativelap.ecoguard.feature.cleaningarea.module.domain.model.AreaAssignment
import com.nativelap.ecoguard.feature.cleaningarea.module.domain.model.AssignedStudent
import com.nativelap.ecoguard.feature.cleaningarea.module.domain.model.CleaningArea
import com.nativelap.ecoguard.feature.cleaningarea.module.domain.model.MyCleaningAssignment
import com.nativelap.ecoguard.feature.cleaningarea.module.domain.model.Semester
import com.nativelap.ecoguard.feature.cleaningarea.module.domain.model.SemesterValue

fun AreaMapResponseDto.toDomain(): CleaningArea =
    CleaningArea(
        areaId = areaId,
        zoneCode = zoneCode,
        name = name,
        description = description,
        cleanTime = cleanTime,
        semester = semester.toSemesterValue(),
        assignedStudents = assignedStudents?.map(AssignedStudentResponseDto::toDomain),
        active = active,
    )

fun AssignmentResponseDto.toDomain(): AreaAssignment =
    AreaAssignment(
        assignmentId = assignmentId,
        studentId = studentId,
    )

fun MyAssignmentResponseDto.toDomain(): MyCleaningAssignment =
    MyCleaningAssignment(
        areaId = areaId,
        zoneCode = zoneCode,
        areaName = areaName,
        description = description,
        cleanTime = cleanTime,
        members = members?.map(AssignedStudentResponseDto::toDomain),
    )

fun AssignedStudentResponseDto.toDomain(): AssignedStudent =
    AssignedStudent(
        studentId = studentId,
        studentNumber = studentNumber,
        name = name,
    )

fun List<Long>.toAssignStudentsRequestDto(): AssignStudentsRequestDto =
    AssignStudentsRequestDto(
        studentIds = this,
    )

fun AreaActivationUpdate.toRequestDto(): UpdateAreaRequestDto =
    UpdateAreaRequestDto(
        active = active,
    )

private fun String?.toSemesterValue(): SemesterValue? =
    this?.let { rawValue ->
        SemesterValue(
            value =
                when (rawValue) {
                    "COMMON" -> Semester.COMMON
                    "FIRST" -> Semester.FIRST
                    "SECOND" -> Semester.SECOND
                    else -> Semester.UNKNOWN
                },
            rawValue = rawValue,
        )
    }
