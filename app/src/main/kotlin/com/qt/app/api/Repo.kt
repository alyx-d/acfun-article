package com.qt.app.api

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.qt.app.api.ds.ArticleCommentsDataSource
import com.qt.app.api.ds.ArticleListDataSource
import com.qt.app.api.ds.ArticleSubCommentDataSource
import com.qt.app.api.vo.ArticleVO
import com.qt.app.api.vo.Comment
import com.qt.app.api.vo.SubComment
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repo @Inject constructor() {
    fun getArticleList(tabId: Int): Flow<PagingData<ArticleVO>> {
        return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 5),
            pagingSourceFactory = { ArticleListDataSource(Api.acfunArticleService, tabId) },
        ).flow
    }

    suspend fun getArticleDetail(articleId: Int): String {
        return Api.acfunArticleDetailService.getArticleDetail(articleId)
    }


    fun getArticleCommentList(sourceId: Int): Flow<PagingData<Comment>> {
        return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 5),
            pagingSourceFactory = { ArticleCommentsDataSource(Api.acfunArticleCommentsService, sourceId) }
        ).flow
    }

    fun getArticleSubCommentList(sourceId: Int, rootCommentId: Int): Flow<PagingData<SubComment>> {
        return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 5),
            pagingSourceFactory = {ArticleSubCommentDataSource( Api.acfunArticleCommentsService,sourceId, rootCommentId)}
        ).flow
    }
}