package com.nativelap.ecoguard.feature.recruitment.module.domain.repository

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.Applicant
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.ApplyCommand
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.ApplyResult
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.CreateRecruitmentCommand
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.CreateRecruitmentResult
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.CurrentRecruitment
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.MyApplicationStatus
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.RecruitmentSummary
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.UpdateRecruitmentCommand

interface RecruitmentRepository {
    suspend fun getRecruitments(): AppResult<List<RecruitmentSummary>>

    suspend fun createRecruitment(command: CreateRecruitmentCommand): AppResult<CreateRecruitmentResult>

    suspend fun getApplicants(recruitmentId: Long): AppResult<List<Applicant>>

    suspend fun apply(command: ApplyCommand): AppResult<ApplyResult>

    suspend fun deleteRecruitment(recruitmentId: Long): AppResult<Unit>

    suspend fun updateRecruitment(command: UpdateRecruitmentCommand): AppResult<Unit>

    suspend fun getCurrentRecruitment(): AppResult<CurrentRecruitment>

    suspend fun getMyApplicationStatus(): AppResult<MyApplicationStatus>
}
