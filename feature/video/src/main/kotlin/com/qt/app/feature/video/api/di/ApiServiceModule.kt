package com.qt.app.feature.video.api.di

import com.qt.app.core.network.service.ApiContentType
import com.qt.app.core.network.service.RetrofitService
import com.qt.app.feature.video.api.service.VideoService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
internal object ApiServiceModule {

    @Provides
    @Singleton
    fun provideVideoService(@RetrofitService(ApiContentType.STRING) retrofit: Retrofit): VideoService =
        retrofit.create()
}