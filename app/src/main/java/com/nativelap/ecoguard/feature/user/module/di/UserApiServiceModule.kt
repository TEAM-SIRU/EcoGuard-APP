package com.nativelap.ecoguard.feature.user.module.di

import com.nativelap.ecoguard.feature.user.module.data.remote.UserApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserApiServiceModule {
    @Provides
    @Singleton
    fun provideUserApiService(
        @Named("authenticated") retrofit: Retrofit,
    ): UserApiService = retrofit.create(UserApiService::class.java)
}
