package com.qt.app.api.ds

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.qt.app.api.dto.SubCommentListParamDTO
import com.qt.app.api.service.AcfunArticleCommentsService
import com.qt.app.api.vo.SubComment
import com.qt.app.util.Util.toMap

class ArticleSubCommentDataSource(
    private val service: AcfunArticleCommentsService,
    private val sourceId: Int,
    private val rootCommentId: Int,
) : PagingSource<Int, SubComment>() {
    override fun getRefreshKey(state: PagingState<Int, SubComment>): Int? {
        return null
    }
    private var totalPage: Int = -1
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SubComment> {
        val currPage = params.key ?: 1
        return try {
            if (totalPage > -1 && currPage > totalPage) return LoadResult.Error(Exception("no more"))
            val query = SubCommentListParamDTO(sourceId, rootCommentId).apply {
                this.page = currPage
            }.toMap()
            val data = service.getArticleSubCommentList(query)
            totalPage = data.totalPage
            LoadResult.Page(
                data = data.subComments,
                prevKey = if (currPage == 1) null else currPage - 1,
                nextKey = currPage + 1
            )
        }catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }
}