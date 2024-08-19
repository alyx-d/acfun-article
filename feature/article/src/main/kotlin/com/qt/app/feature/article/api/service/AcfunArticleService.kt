package com.qt.app.feature.article.api.service

import com.qt.app.feature.article.api.dto.ArticleListParamDTO
import com.qt.app.feature.article.api.vo.ArticleVO
import com.qt.app.feature.article.api.vo.CommentPageVO
import com.qt.app.feature.article.api.vo.ResultVO
import com.qt.app.feature.article.api.vo.SubCommentPageVO
import com.qt.app.feature.article.api.vo.UserEmotionVO
import com.qt.app.core.utils.Util.toMap
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap

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
    suspend fun getArticleDetail(@Path("articleId") articleId: Int): Response<String>
}

interface AcfunArticleCommentsService {

    @POST("/rest/pc-direct/emotion/getUserEmotion")
    suspend fun getUserEmotion(): UserEmotionVO

    @GET("/rest/pc-direct/comment/list")
    suspend fun getArticleCommentList(@QueryMap query: Map<String, String>): CommentPageVO

    @GET("/rest/pc-direct/comment/sublist")
    suspend fun getArticleSubCommentList(@QueryMap query: Map<String, String>): SubCommentPageVO
}