package com.nativelap.ecoguard.feature.user.module.domain.usecase

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.user.module.domain.model.UserProfile
import com.nativelap.ecoguard.feature.user.module.domain.repository.UserRepository
import javax.inject.Inject

class GetMyProfileUseCase
    @Inject
    constructor(
        private val repository: UserRepository,
    ) {
        suspend operator fun invoke(): AppResult<UserProfile> = repository.getMyProfile()
    }

class WithdrawUseCase
    @Inject
    constructor(
        private val repository: UserRepository,
    ) {
        suspend operator fun invoke(): AppResult<Unit> = repository.withdraw()
    }

class LogoutUseCase
    @Inject
    constructor(
        private val repository: UserRepository,
    ) {
        suspend operator fun invoke(): AppResult<Unit> = repository.logout()
    }
