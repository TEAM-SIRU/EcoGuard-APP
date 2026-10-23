package com.nativelap.ecoguard.feature.auth.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthApiModule {
    @Provides
    @Singleton
    fun provideAuthApiService(
        @Named("unauthenticated") retrofit: Retrofit,
    ): AuthApiService = retrofit.create(AuthApiService::class.java)
}
