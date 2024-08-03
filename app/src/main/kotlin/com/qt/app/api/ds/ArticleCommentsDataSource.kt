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

    override fun getRefreshKey(state: PagingState<Int, Comment>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Comment> {
        val currPage = params.key ?: 1
        return try {
            val param = CommentListParamDTO(sourceId).apply { page = currPage }
                .toMap()
            val data = service.getArticleCommentList(param)
            LoadResult.Page(
                data = data.rootComments.onEach { item -> item.info = data },
                prevKey = if (currPage == 1) null else currPage - 1,
                nextKey = if (data.rootComments.isNotEmpty()) currPage + 1 else null
            )
        } catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }
}