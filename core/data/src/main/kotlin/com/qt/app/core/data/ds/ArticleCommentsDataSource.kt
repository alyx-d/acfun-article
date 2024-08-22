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

    private var totalPage: Int = -1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CommentPageVO.Comment> {
        val currPage = params.key ?: 1
        return try {
            if (totalPage > - 1 && currPage > totalPage) return LoadResult.Error(Exception("没有下一页"))
            val param = CommentListParamDTO(sourceId).apply {
                page = currPage
            }
                .toMap()
            val data = service.getArticleCommentList(param)
            totalPage = data.totalPage
            LoadResult.Page(
                data = data.rootComments.onEach { item -> item.info = data },
                prevKey = if (currPage == 1) null else currPage - 1,
                nextKey = currPage + 1
            )
        } catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }
}