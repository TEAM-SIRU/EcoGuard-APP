package com.nativelap.ecoguard.feature.notification.module.di

import com.nativelap.ecoguard.feature.notification.module.data.NotificationRepositoryImpl
import com.nativelap.ecoguard.feature.notification.module.data.remote.NotificationApiService
import com.nativelap.ecoguard.feature.notification.module.data.remote.NotificationRemoteDataSource
import com.nativelap.ecoguard.feature.notification.module.data.remote.NotificationRemoteDataSourceImpl
import com.nativelap.ecoguard.feature.notification.module.domain.NotificationRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationModule {
    @Binds
    abstract fun bindRemote(implementation: NotificationRemoteDataSourceImpl): NotificationRemoteDataSource

    @Binds
    abstract fun bindRepository(implementation: NotificationRepositoryImpl): NotificationRepository

    companion object {
        @Provides
        fun provideApi(
            @Named("authenticated") retrofit: Retrofit,
        ): NotificationApiService = retrofit.create(NotificationApiService::class.java)
    }
}
