package com.nativelap.ecoguard.feature.recruitment.module.di

import com.nativelap.ecoguard.feature.recruitment.module.data.remote.RecruitmentRemoteDataSource
import com.nativelap.ecoguard.feature.recruitment.module.data.remote.RecruitmentRemoteDataSourceImpl
import com.nativelap.ecoguard.feature.recruitment.module.data.repository.RecruitmentRepositoryImpl
import com.nativelap.ecoguard.feature.recruitment.module.domain.repository.RecruitmentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RecruitmentModule {
    @Binds
    abstract fun bindRecruitmentRemoteDataSource(
        implementation: RecruitmentRemoteDataSourceImpl,
    ): RecruitmentRemoteDataSource

    @Binds
    abstract fun bindRecruitmentRepository(implementation: RecruitmentRepositoryImpl): RecruitmentRepository
}
