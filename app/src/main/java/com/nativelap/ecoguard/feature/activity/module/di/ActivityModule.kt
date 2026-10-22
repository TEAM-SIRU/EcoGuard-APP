package com.nativelap.ecoguard.feature.activity.module.di

import com.nativelap.ecoguard.feature.activity.module.data.remote.ActivityRemoteDataSource
import com.nativelap.ecoguard.feature.activity.module.data.remote.ActivityRemoteDataSourceImpl
import com.nativelap.ecoguard.feature.activity.module.data.repository.ActivityRepositoryImpl
import com.nativelap.ecoguard.feature.activity.module.domain.repository.ActivityRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ActivityModule {
    @Binds
    abstract fun bindActivityRemoteDataSource(implementation: ActivityRemoteDataSourceImpl): ActivityRemoteDataSource

    @Binds
    abstract fun bindActivityRepository(implementation: ActivityRepositoryImpl): ActivityRepository
}
