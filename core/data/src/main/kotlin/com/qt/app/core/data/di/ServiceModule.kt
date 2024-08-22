package com.qt.app.core.data.di

import com.qt.app.core.data.service.AcfunArticleCommentsService
import com.qt.app.core.data.service.AcfunArticleDetailService
import com.qt.app.core.data.service.AcfunArticleService
import com.qt.app.core.data.service.VideoService
import com.qt.app.core.network.service.ApiContentType
import com.qt.app.core.network.service.RetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ServiceModule {

    @Provides
    @Singleton
    fun provideAcfunArticleService(@RetrofitService(ApiContentType.JSON) retrofit: Retrofit): AcfunArticleService =
        retrofit.create()

    @Provides
    @Singleton
    fun provideAcfunArticleDetailService(@RetrofitService(ApiContentType.STRING) retrofit: Retrofit): AcfunArticleDetailService =
        retrofit.create()

    @Provides
    @Singleton
    fun provideAcfunArticleCommentService(@RetrofitService(ApiContentType.JSON) retrofit: Retrofit): AcfunArticleCommentsService =
        retrofit.create()

    @Provides
    @Singleton
    fun provideVideoService(@RetrofitService(ApiContentType.STRING) retrofit: Retrofit): VideoService =
        retrofit.create()
}