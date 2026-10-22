package com.nativelap.ecoguard.feature.user.module.di

import com.nativelap.ecoguard.feature.user.module.data.remote.UserRemoteDataSource
import com.nativelap.ecoguard.feature.user.module.data.remote.UserRemoteDataSourceImpl
import com.nativelap.ecoguard.feature.user.module.data.repository.UserRepositoryImpl
import com.nativelap.ecoguard.feature.user.module.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UserModule {
    @Binds
    abstract fun bindUserRemoteDataSource(implementation: UserRemoteDataSourceImpl): UserRemoteDataSource

    @Binds
    abstract fun bindUserRepository(implementation: UserRepositoryImpl): UserRepository
}
