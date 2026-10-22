package com.nativelap.ecoguard.feature.verification.module.di

import com.nativelap.ecoguard.feature.verification.module.data.remote.VerificationApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object VerificationApiModule {
    @Provides
    @Singleton
    fun provideVerificationApiService(retrofit: Retrofit): VerificationApiService =
        retrofit.create(VerificationApiService::class.java)
}
