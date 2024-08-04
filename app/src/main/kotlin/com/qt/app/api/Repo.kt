package com.qt.app.api

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.qt.app.api.ds.ArticleCommentsDataSource
import com.qt.app.api.ds.ArticleListDataSource
import com.qt.app.api.vo.ArticleVO
import com.qt.app.api.vo.Comment
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repo @Inject constructor() {
    fun getArticleList(tabId: Int): Flow<PagingData<ArticleVO>> {
        return Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 3, maxSize = 30),
            pagingSourceFactory = { ArticleListDataSource(Api.acfunArticleService, tabId) },
        ).flow
    }

    suspend fun getArticleDetail(articleId: String): String {
        return Api.acfunArticleDetailService.getArticleDetail(articleId)
    }


    fun getArticleCommentList(sourceId: String): Flow<PagingData<Comment>> {
        return Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 3, maxSize = 30),
            pagingSourceFactory = { ArticleCommentsDataSource(Api.acfunArticleCommentsService, sourceId) }
        ).flow
    }
}