package com.nativelap.ecoguard.core.network.di

import com.nativelap.ecoguard.BuildConfig
import com.nativelap.ecoguard.core.network.auth.AccessTokenProvider
import com.nativelap.ecoguard.core.network.auth.BearerTokenInterceptor
import com.nativelap.ecoguard.core.network.auth.NoAccessTokenProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkBindingsModule {
    @Binds
    abstract fun bindAccessTokenProvider(implementation: NoAccessTokenProvider): AccessTokenProvider
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideJson(): Json =
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
            encodeDefaults = false
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(bearerTokenInterceptor: BearerTokenInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(bearerTokenInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        json: Json,
        okHttpClient: OkHttpClient,
    ): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
}
