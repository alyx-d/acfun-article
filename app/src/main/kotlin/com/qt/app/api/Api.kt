package com.qt.app.api

import android.util.Log
import com.qt.app.api.dto.ArticleListParamDTO
import com.qt.app.api.vo.ArticleVO
import com.qt.app.api.vo.CommentPageVO
import com.qt.app.api.vo.ResultVO
import com.qt.app.api.vo.SubCommentPageVO
import com.qt.app.api.vo.UserEmotionVO
import com.qt.app.util.Util.toMap
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap
import java.util.concurrent.TimeUnit

val realmIdList = arrayOf(
    arrayOf(22, 28, 4),
    arrayOf(50, 25, 34, 7, 5, 49),
    arrayOf(8, 52, 11, 45),
    arrayOf(13, 15, 18, 14, 31),
)

interface AcfunArticleService {
    @POST("/rest/pc-direct/article/feed")
    @FormUrlEncoded
    suspend fun getArticlePage(
        @FieldMap formData: Map<String, String> = ArticleListParamDTO().toMap(),
        @Field("realmId") realmId: Array<Int> = realmIdList[0]
    ): ResultVO<MutableList<ArticleVO>>
}

interface AcfunArticleDetailService {
    @GET("/a/ac{articleId}")
    @Headers("UserAgent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36")
    suspend fun getArticleDetail(@Path("articleId") articleId: Int): String
}

interface AcfunArticleCommentsService {

    @POST("/rest/pc-direct/emotion/getUserEmotion")
    suspend fun getUserEmotion(): UserEmotionVO

    @GET("/rest/pc-direct/comment/list")
    suspend fun getArticleCommentList(@QueryMap query: Map<String, String>): CommentPageVO

    @GET("/rest/pc-direct/comment/sublist")
    suspend fun getArticleSubCommentList(@QueryMap query: Map<String, String>): SubCommentPageVO
}

object Api {
    const val BASE_URL = "https://www.acfun.cn"
    fun client(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addNetworkInterceptor(HttpLoggingInterceptor {
                Log.d("HttpLog", it)
            }.apply {
                  setLevel(HttpLoggingInterceptor.Level.BODY)
            })
            .build()
    }

    val json = Json { ignoreUnknownKeys = true}
    fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json; charset=utf8".toMediaType()))
            .client(client())
            .build()
    }

    fun retrofitHtml(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    val acfunArticleService: AcfunArticleService =
        retrofit().create(AcfunArticleService::class.java)

    val acfunArticleDetailService: AcfunArticleDetailService =
        retrofitHtml().create(AcfunArticleDetailService::class.java)

    val acfunArticleCommentsService: AcfunArticleCommentsService =
        retrofit().create(AcfunArticleCommentsService::class.java)
}