package com.qt.app.feature.video.api.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface VideoService {

    @GET("/")
    @Headers("UserAgent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36")
    suspend fun acfunHome(): Response<String>
}