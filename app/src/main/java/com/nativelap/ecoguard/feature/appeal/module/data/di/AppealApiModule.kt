package com.nativelap.ecoguard.feature.appeal.module.data.di

import com.nativelap.ecoguard.feature.appeal.module.data.remote.AppealApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppealApiModule {
    @Provides
    @Singleton
    fun provideAppealApiService(
        @Named("authenticated") retrofit: Retrofit,
    ): AppealApiService = retrofit.create(AppealApiService::class.java)
}
