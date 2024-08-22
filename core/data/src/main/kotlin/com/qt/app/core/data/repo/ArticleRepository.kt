package com.qt.app.core.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.qt.app.core.data.ds.ArticleCommentsDataSource
import com.qt.app.core.data.ds.ArticleListDataSource
import com.qt.app.core.data.ds.ArticleSubCommentDataSource
import com.qt.app.core.data.service.AcfunArticleCommentsService
import com.qt.app.core.data.service.AcfunArticleDetailService
import com.qt.app.core.data.service.AcfunArticleService
import com.qt.app.core.data.vo.ArticleVO
import com.qt.app.core.data.vo.CommentPageVO
import com.qt.app.core.data.vo.SubCommentPageVO
import com.qt.app.core.data.vo.UserEmotionVO
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val acfunArticleService: AcfunArticleService,
    private val acfunArticleDetailService: AcfunArticleDetailService,
    private val acfunArticleCommentsService: AcfunArticleCommentsService,
) {
    fun getArticleList(tabId: Int): Flow<PagingData<ArticleVO>> {
        return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 5),
            pagingSourceFactory = { ArticleListDataSource(acfunArticleService, tabId) },
        ).flow
    }

    suspend fun getArticleDetail(articleId: Int): Response<String> {
        return acfunArticleDetailService.getArticleDetail(articleId)
    }


    fun getArticleCommentList(sourceId: Int): Flow<PagingData<CommentPageVO.Comment>> {
        return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 5),
            pagingSourceFactory = { ArticleCommentsDataSource(acfunArticleCommentsService, sourceId) }
        ).flow
    }

    fun getArticleSubCommentList(sourceId: Int, rootCommentId: Int): Flow<PagingData<SubCommentPageVO.SubComment>> {
        return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 5),
            pagingSourceFactory = { ArticleSubCommentDataSource( acfunArticleCommentsService,sourceId, rootCommentId) }
        ).flow
    }

    suspend fun getUserEmotion(): UserEmotionVO {
        return acfunArticleCommentsService.getUserEmotion()
    }
}