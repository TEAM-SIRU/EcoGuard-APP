package com.nativelap.ecoguard.feature.notification.module.di

import com.nativelap.ecoguard.feature.notification.module.data.remote.NotificationApiService
import com.nativelap.ecoguard.feature.notification.module.data.remote.NotificationRemoteDataSource
import com.nativelap.ecoguard.feature.notification.module.data.remote.NotificationRemoteDataSourceImpl
import com.nativelap.ecoguard.feature.notification.module.data.repository.NotificationRepositoryImpl
import com.nativelap.ecoguard.feature.notification.module.domain.repository.NotificationRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationModule {
    @Binds
    abstract fun bindNotificationRemoteDataSource(
        implementation: NotificationRemoteDataSourceImpl,
    ): NotificationRemoteDataSource

    @Binds
    abstract fun bindNotificationRepository(implementation: NotificationRepositoryImpl): NotificationRepository

    companion object {
        @Provides
        @Singleton
        fun provideNotificationApiService(
            @Named("authenticated") retrofit: Retrofit,
        ): NotificationApiService = retrofit.create(NotificationApiService::class.java)
    }
}
