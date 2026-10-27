package com.nativelap.ecoguard.feature.recruitment.module.domain.repository

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.ApplyCommand
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.ApplyResult
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.CurrentRecruitment
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.MyApplicationStatus

interface RecruitmentRepository {
    suspend fun apply(command: ApplyCommand): AppResult<ApplyResult>

    suspend fun getCurrentRecruitment(): AppResult<CurrentRecruitment>

    suspend fun getMyApplicationStatus(): AppResult<MyApplicationStatus>
}
