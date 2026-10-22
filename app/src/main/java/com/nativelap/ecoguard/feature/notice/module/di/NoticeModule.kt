package com.nativelap.ecoguard.feature.notice.module.di

import com.nativelap.ecoguard.feature.notice.module.data.remote.NoticeRemoteDataSource
import com.nativelap.ecoguard.feature.notice.module.data.remote.NoticeRemoteDataSourceImpl
import com.nativelap.ecoguard.feature.notice.module.data.repository.NoticeRepositoryImpl
import com.nativelap.ecoguard.feature.notice.module.domain.repository.NoticeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NoticeModule {
    @Binds
    abstract fun bindNoticeRemoteDataSource(implementation: NoticeRemoteDataSourceImpl): NoticeRemoteDataSource

    @Binds
    abstract fun bindNoticeRepository(implementation: NoticeRepositoryImpl): NoticeRepository
}
