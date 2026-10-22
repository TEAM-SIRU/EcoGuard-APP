package com.nativelap.ecoguard.feature.appeal.module.data.di

import com.nativelap.ecoguard.feature.appeal.module.data.remote.AppealRemoteDataSource
import com.nativelap.ecoguard.feature.appeal.module.data.remote.AppealRemoteDataSourceImpl
import com.nativelap.ecoguard.feature.appeal.module.data.repository.AppealRepositoryImpl
import com.nativelap.ecoguard.feature.appeal.module.domain.repository.AppealRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppealDataModule {
    @Binds
    abstract fun bindAppealRemoteDataSource(implementation: AppealRemoteDataSourceImpl): AppealRemoteDataSource

    @Binds
    abstract fun bindAppealRepository(implementation: AppealRepositoryImpl): AppealRepository
}
