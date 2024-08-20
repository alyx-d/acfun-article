package com.qt.app.feature.video.api.service

import com.qt.app.core.utils.Util.toMap
import com.qt.app.feature.video.api.dto.VideoInfoDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface VideoService {

    @GET("/")
    @Headers("UserAgent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36")
    suspend fun acfunHome(): Response<String>

    @GET("/v/ac{id}")
    @Headers("UserAgent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36")
    suspend fun acfunVideo(@Path("id") id: String): Response<String>

    @GET("/v/ac{id}")
    suspend fun acfunVideoInfo(@Path("id") id: String, @QueryMap query: Map<String, String> = VideoInfoDTO().toMap()) : Response<String>
}