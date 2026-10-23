package com.nativelap.ecoguard.feature.auth.data

import com.nativelap.ecoguard.core.network.auth.AccessTokenProvider
import com.nativelap.ecoguard.core.network.auth.SessionTokenAccess
import com.nativelap.ecoguard.feature.auth.domain.AuthRepository
import com.nativelap.ecoguard.feature.auth.domain.SessionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthBindingsModule {
    @Binds
    abstract fun bindAuthRemoteDataSource(implementation: AuthRemoteDataSourceImpl): AuthRemoteDataSource

    @Binds
    abstract fun bindAccessTokenProvider(sessionManager: SessionManager): AccessTokenProvider

    @Binds
    abstract fun bindSessionTokenAccess(sessionManager: SessionManager): SessionTokenAccess

    @Binds
    abstract fun bindSessionRepository(sessionManager: SessionManager): SessionRepository

    @Binds
    abstract fun bindAuthRepository(implementation: AuthRepositoryImpl): AuthRepository
}
