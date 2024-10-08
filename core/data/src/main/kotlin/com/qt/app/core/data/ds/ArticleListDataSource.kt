package com.qt.app.core.data.ds

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.qt.app.core.data.dto.ArticleListParamDTO
import com.qt.app.core.data.service.AcfunArticleService
import com.qt.app.core.data.service.realmIdList
import com.qt.app.core.data.vo.ArticleVO
import com.qt.app.core.network.utils.toMap


class ArticleListDataSource(private val service: AcfunArticleService, private var tabId: Int = 0) : PagingSource<String, ArticleVO>() {
    override fun getRefreshKey(state: PagingState<String, ArticleVO>): String? {
        return null
    }

    private val cursor = mutableListOf<String>()

    override suspend fun load(params: LoadParams<String>): LoadResult<String, ArticleVO> {
        val currentKey = params.key ?: "first_page"
        return try {
            val formData = ArticleListParamDTO().apply {
                this.cursor = currentKey
            }.toMap()
            cursor.add(currentKey)
            val response = service.getArticlePage(formData, realmIdList[tabId])
            LoadResult.Page(
                data = response.data ?: listOf(),
                prevKey = if (currentKey == "first_page") null else cursor[cursor.lastIndex - 1],
                nextKey = response.cursor
            )
        } catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }
}
