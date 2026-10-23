package com.nativelap.ecoguard.feature.recruitment.module.di

import com.nativelap.ecoguard.feature.recruitment.module.data.remote.RecruitmentApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RecruitmentApiModule {
    @Provides
    @Singleton
    fun provideRecruitmentApiService(
        @Named("authenticated") retrofit: Retrofit,
    ): RecruitmentApiService = retrofit.create(RecruitmentApiService::class.java)
}
