package com.nativelap.ecoguard.feature.cleaningarea.module.di

import com.nativelap.ecoguard.feature.cleaningarea.module.data.remote.CleaningAreaApiService
import com.nativelap.ecoguard.feature.cleaningarea.module.data.remote.CleaningAreaRemoteDataSource
import com.nativelap.ecoguard.feature.cleaningarea.module.data.remote.CleaningAreaRemoteDataSourceImpl
import com.nativelap.ecoguard.feature.cleaningarea.module.data.repository.CleaningAreaRepositoryImpl
import com.nativelap.ecoguard.feature.cleaningarea.module.domain.repository.CleaningAreaRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CleaningAreaModule {
    @Binds
    abstract fun bindCleaningAreaRemoteDataSource(
        implementation: CleaningAreaRemoteDataSourceImpl,
    ): CleaningAreaRemoteDataSource

    @Binds
    abstract fun bindCleaningAreaRepository(implementation: CleaningAreaRepositoryImpl): CleaningAreaRepository
}

@Module
@InstallIn(SingletonComponent::class)
object CleaningAreaApiServiceModule {
    @Provides
    @Singleton
    fun provideCleaningAreaApiService(
        @Named("authenticated") retrofit: Retrofit,
    ): CleaningAreaApiService = retrofit.create(CleaningAreaApiService::class.java)
}
