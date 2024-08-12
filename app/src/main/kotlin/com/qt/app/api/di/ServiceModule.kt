package com.qt.app.api.di

import com.qt.app.api.service.AcfunArticleCommentsService
import com.qt.app.api.service.AcfunArticleDetailService
import com.qt.app.api.service.AcfunArticleService
import com.qt.app.api.service.ApiType
import com.qt.app.api.service.RetrofitService
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
    fun provideAcfunArticleService(@RetrofitService(ApiType.JSON) retrofit: Retrofit): AcfunArticleService =
        retrofit.create()

    @Provides
    @Singleton
    fun provideAcfunArticleDetailService(@RetrofitService(ApiType.STRING) retrofit: Retrofit): AcfunArticleDetailService =
        retrofit.create()

    @Provides
    @Singleton
    fun provideAcfunArticleCommentService(@RetrofitService(ApiType.JSON) retrofit: Retrofit): AcfunArticleCommentsService =
        retrofit.create()
}