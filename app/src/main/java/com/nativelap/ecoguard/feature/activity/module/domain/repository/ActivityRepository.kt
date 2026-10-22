package com.nativelap.ecoguard.feature.activity.module.domain.repository

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.activity.module.domain.model.MyActivity
import com.nativelap.ecoguard.feature.activity.module.domain.model.StudentActivity
import com.nativelap.ecoguard.feature.activity.module.domain.model.WeeklyActivity

interface ActivityRepository {
    suspend fun getStudentActivity(
        studentId: Long,
        year: Int?,
        month: Int?,
    ): AppResult<MyActivity>

    suspend fun searchStudents(keyword: String?): AppResult<List<StudentActivity>>

    suspend fun getMyActivity(
        year: Int?,
        month: Int?,
    ): AppResult<MyActivity>

    suspend fun getMyWeeklyActivity(): AppResult<WeeklyActivity>
}
