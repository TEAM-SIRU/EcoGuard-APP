package com.nativelap.ecoguard.feature.activity.module.di

import com.nativelap.ecoguard.feature.activity.module.data.remote.ActivityApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ActivityApiServiceModule {
    @Provides
    @Singleton
    fun provideActivityApiService(
        @Named("authenticated") retrofit: Retrofit,
    ): ActivityApiService = retrofit.create(ActivityApiService::class.java)
}
