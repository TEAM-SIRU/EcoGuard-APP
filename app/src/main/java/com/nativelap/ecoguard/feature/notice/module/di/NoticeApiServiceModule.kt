package com.nativelap.ecoguard.feature.notice.module.di

import com.nativelap.ecoguard.feature.notice.module.data.remote.NoticeApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NoticeApiServiceModule {
    @Provides
    @Singleton
    fun provideNoticeApiService(retrofit: Retrofit): NoticeApiService = retrofit.create(NoticeApiService::class.java)
}
