package com.nativelap.ecoguard.feature.recruitment.module.domain.usecase

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
import com.nativelap.ecoguard.feature.recruitment.module.domain.repository.RecruitmentRepository
import javax.inject.Inject

class GetRecruitmentsUseCase
    @Inject
    constructor(
        private val repository: RecruitmentRepository,
    ) {
        suspend operator fun invoke(): AppResult<List<RecruitmentSummary>> = repository.getRecruitments()
    }

class CreateRecruitmentUseCase
    @Inject
    constructor(
        private val repository: RecruitmentRepository,
    ) {
        suspend operator fun invoke(command: CreateRecruitmentCommand): AppResult<CreateRecruitmentResult> =
            repository.createRecruitment(command)
    }

class GetApplicantsUseCase
    @Inject
    constructor(
        private val repository: RecruitmentRepository,
    ) {
        suspend operator fun invoke(recruitmentId: Long): AppResult<List<Applicant>> =
            repository.getApplicants(recruitmentId)
    }

class ApplyForRecruitmentUseCase
    @Inject
    constructor(
        private val repository: RecruitmentRepository,
    ) {
        suspend operator fun invoke(command: ApplyCommand): AppResult<ApplyResult> = repository.apply(command)
    }

class DeleteRecruitmentUseCase
    @Inject
    constructor(
        private val repository: RecruitmentRepository,
    ) {
        suspend operator fun invoke(recruitmentId: Long): AppResult<Unit> = repository.deleteRecruitment(recruitmentId)
    }

class UpdateRecruitmentUseCase
    @Inject
    constructor(
        private val repository: RecruitmentRepository,
    ) {
        suspend operator fun invoke(command: UpdateRecruitmentCommand): AppResult<Unit> =
            repository.updateRecruitment(command)
    }

class GetCurrentRecruitmentUseCase
    @Inject
    constructor(
        private val repository: RecruitmentRepository,
    ) {
        suspend operator fun invoke(): AppResult<CurrentRecruitment> = repository.getCurrentRecruitment()
    }

class GetMyApplicationStatusUseCase
    @Inject
    constructor(
        private val repository: RecruitmentRepository,
    ) {
        suspend operator fun invoke(): AppResult<MyApplicationStatus> = repository.getMyApplicationStatus()
    }
