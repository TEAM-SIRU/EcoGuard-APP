package com.nativelap.ecoguard.feature.recruitment.module.domain.usecase

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.ApplyCommand
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.ApplyResult
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.CurrentRecruitment
import com.nativelap.ecoguard.feature.recruitment.module.domain.model.MyApplicationStatus
import com.nativelap.ecoguard.feature.recruitment.module.domain.repository.RecruitmentRepository
import javax.inject.Inject

class ApplyForRecruitmentUseCase
    @Inject
    constructor(
        private val repository: RecruitmentRepository,
    ) {
        suspend operator fun invoke(command: ApplyCommand): AppResult<ApplyResult> = repository.apply(command)
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
