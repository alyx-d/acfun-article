package com.qt.app.core.data.ds

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.qt.app.core.data.dto.CommentListParamDTO
import com.qt.app.core.data.service.AcfunArticleCommentsService
import com.qt.app.core.data.vo.CommentPageVO
import com.qt.app.core.network.utils.toMap

class ArticleCommentsDataSource(
    private val service: AcfunArticleCommentsService,
    private val sourceId: Int,
) : PagingSource<Int, CommentPageVO.Comment>() {

    override fun getRefreshKey(state: PagingState<Int, CommentPageVO.Comment>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CommentPageVO.Comment> {
        val currPage = params.key ?: 1
        return try {
            val param = CommentListParamDTO(sourceId).apply {
                page = currPage
            }
                .toMap()
            val data = service.getArticleCommentList(param)
            LoadResult.Page(
                data = data.rootComments.onEach { item -> item.info = data },
                prevKey = if (currPage == 1) null else currPage - 1,
                nextKey = data.nextPage
            )
        } catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }
}