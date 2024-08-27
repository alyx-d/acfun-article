package com.qt.app.core.data.ds

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.qt.app.core.data.dto.SubCommentListParamDTO
import com.qt.app.core.data.service.AcfunArticleCommentsService
import com.qt.app.core.data.vo.SubCommentPageVO
import com.qt.app.core.network.utils.toMap

class ArticleSubCommentDataSource(
    private val service: AcfunArticleCommentsService,
    private val sourceId: Int,
    private val rootCommentId: Int,
) : PagingSource<Int, SubCommentPageVO.SubComment>() {
    override fun getRefreshKey(state: PagingState<Int, SubCommentPageVO.SubComment>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SubCommentPageVO.SubComment> {
        val currPage = params.key ?: 1
        return try {
            val query = SubCommentListParamDTO(sourceId, rootCommentId).apply {
                this.page = currPage
            }.toMap()
            val data = service.getArticleSubCommentList(query)
            LoadResult.Page(
                data = data.subComments,
                prevKey = if (currPage == 1) null else currPage - 1,
                nextKey = data.nextPage
            )
        }catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }
}