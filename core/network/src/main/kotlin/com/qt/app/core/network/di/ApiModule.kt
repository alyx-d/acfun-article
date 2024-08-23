package com.qt.app.core.network.di

import com.qt.app.core.network.service.ApiContentType
import com.qt.app.core.network.service.BaseService
import com.qt.app.core.network.service.RetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ApiModule {
    @Provides
    @Singleton
    fun provideApiJson() = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun provideOkhttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addNetworkInterceptor(loggingInterceptor())
            .build()

    @Provides
    @Singleton
    @RetrofitService(ApiContentType.JSON)
    fun provideApiJsonRetrofit(apiJson: Json, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BaseService.ACFUN.baseUrl)
            .addConverterFactory(apiJson.asConverterFactory("application/json; charset=utf8".toMediaType()))
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    @RetrofitService(ApiContentType.STRING)
    fun provideApiStringRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BaseService.ACFUN.baseUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(okHttpClient)
            .build()
}

private fun loggingInterceptor() = HttpLoggingInterceptor {
    // Log.d("HttpLog", it)
}.apply { level = HttpLoggingInterceptor.Level.BODY }