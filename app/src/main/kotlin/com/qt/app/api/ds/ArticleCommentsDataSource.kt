package com.qt.app.api.ds

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.qt.app.api.AcfunArticleCommentsService
import com.qt.app.api.dto.CommentListParamDTO
import com.qt.app.api.vo.Comment
import com.qt.app.util.Util.toMap

class ArticleCommentsDataSource(
    private val service: AcfunArticleCommentsService,
    private val sourceId: String,
) : PagingSource<Int, Comment>() {

    override fun getRefreshKey(state: PagingState<Int, Comment>): Int {
        return 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Comment> {
        val currPage = params.key ?: 1
        return try {
            val param = CommentListParamDTO(sourceId).apply { page = currPage }
                .toMap()
            val data = service.getArticleCommentList(param)
            return if (data.curPage > data.totalPage) LoadResult.Error(RuntimeException("无更多数据"))
            else LoadResult.Page(
                data = data.rootComments.onEach { item -> item.info = data },
                prevKey = if (currPage == 1) null else currPage - 1,
                nextKey = currPage + 1
            )
        } catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }
}