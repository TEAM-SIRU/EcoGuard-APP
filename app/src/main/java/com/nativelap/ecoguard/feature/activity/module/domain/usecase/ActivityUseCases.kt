package com.nativelap.ecoguard.feature.activity.module.domain.usecase

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.activity.module.domain.model.MyActivity
import com.nativelap.ecoguard.feature.activity.module.domain.model.StudentActivity
import com.nativelap.ecoguard.feature.activity.module.domain.model.WeeklyActivity
import com.nativelap.ecoguard.feature.activity.module.domain.repository.ActivityRepository
import javax.inject.Inject

class GetStudentActivityUseCase
    @Inject
    constructor(
        private val repository: ActivityRepository,
    ) {
        suspend operator fun invoke(
            studentId: Long,
            year: Int? = null,
            month: Int? = null,
        ): AppResult<MyActivity> = repository.getStudentActivity(studentId, year, month)
    }

class SearchStudentsUseCase
    @Inject
    constructor(
        private val repository: ActivityRepository,
    ) {
        suspend operator fun invoke(keyword: String? = null): AppResult<List<StudentActivity>> =
            repository.searchStudents(keyword)
    }

class GetMyActivityUseCase
    @Inject
    constructor(
        private val repository: ActivityRepository,
    ) {
        suspend operator fun invoke(
            year: Int? = null,
            month: Int? = null,
        ): AppResult<MyActivity> = repository.getMyActivity(year, month)
    }

class GetMyWeeklyActivityUseCase
    @Inject
    constructor(
        private val repository: ActivityRepository,
    ) {
        suspend operator fun invoke(): AppResult<WeeklyActivity> = repository.getMyWeeklyActivity()
    }
