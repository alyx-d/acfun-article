package com.qt.app.core.network.di

import com.qt.app.core.network.exception.NoNetworkException
import com.qt.app.core.network.service.ApiContentType
import com.qt.app.core.network.service.BaseService
import com.qt.app.core.network.service.RetrofitService
import com.qt.app.core.network.utils.isOnline
import com.qt.app.core.utils.SnackBarHostStateHolder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
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
            .addInterceptor(exceptionHandlerInterceptor())
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

private fun exceptionHandlerInterceptor() = Interceptor { chain ->
    try {
        if (isOnline().not()) throw NoNetworkException()
        else chain.proceed(chain.request())
    } catch (ex: Exception) {
        when (ex) {
            is NoNetworkException -> SnackBarHostStateHolder.showMessage("无网络")
            else -> SnackBarHostStateHolder.showMessage("系统出错,请稍后重试")
        }
        Response.Builder()
            .code(500)
            .request(chain.request())
            .protocol(Protocol.HTTP_1_1)
            .message(ex.message ?: "system error")
            .body(null)
            .build()
    }
}