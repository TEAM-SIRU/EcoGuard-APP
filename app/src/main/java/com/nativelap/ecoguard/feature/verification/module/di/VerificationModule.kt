package com.nativelap.ecoguard.feature.verification.module.di

import com.nativelap.ecoguard.feature.verification.module.data.remote.VerificationRemoteDataSource
import com.nativelap.ecoguard.feature.verification.module.data.remote.VerificationRemoteDataSourceImpl
import com.nativelap.ecoguard.feature.verification.module.data.repository.VerificationRepositoryImpl
import com.nativelap.ecoguard.feature.verification.module.domain.repository.VerificationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class VerificationModule {
    @Binds
    abstract fun bindVerificationRemoteDataSource(
        implementation: VerificationRemoteDataSourceImpl,
    ): VerificationRemoteDataSource

    @Binds
    abstract fun bindVerificationRepository(implementation: VerificationRepositoryImpl): VerificationRepository
}
