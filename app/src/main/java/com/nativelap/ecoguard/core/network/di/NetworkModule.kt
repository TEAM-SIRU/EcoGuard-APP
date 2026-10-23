package com.nativelap.ecoguard.core.network.di

import com.nativelap.ecoguard.BuildConfig
import com.nativelap.ecoguard.core.network.auth.BearerTokenInterceptor
import com.nativelap.ecoguard.core.network.auth.TokenRefreshApi
import com.nativelap.ecoguard.core.network.auth.TokenRefreshAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Named
import javax.inject.Singleton

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
    @Named("unauthenticated")
    fun provideUnauthenticatedOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides
    @Singleton
    @Named("authenticated")
    fun provideAuthenticatedOkHttpClient(
        @Named("unauthenticated") unauthenticatedClient: OkHttpClient,
        bearerTokenInterceptor: BearerTokenInterceptor,
        tokenRefreshAuthenticator: TokenRefreshAuthenticator,
    ): OkHttpClient =
        unauthenticatedClient
            .newBuilder()
            .addInterceptor(bearerTokenInterceptor)
            .authenticator(tokenRefreshAuthenticator)
            .build()

    @Provides
    @Singleton
    @Named("unauthenticated")
    fun provideUnauthenticatedRetrofit(
        json: Json,
        @Named("unauthenticated") okHttpClient: OkHttpClient,
    ): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

    @Provides
    @Singleton
    @Named("authenticated")
    fun provideAuthenticatedRetrofit(
        json: Json,
        @Named("authenticated") okHttpClient: OkHttpClient,
    ): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

    @Provides
    @Singleton
    fun provideTokenRefreshApi(
        @Named("unauthenticated") retrofit: Retrofit,
    ): TokenRefreshApi = retrofit.create(TokenRefreshApi::class.java)
}
