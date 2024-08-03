package com.qt.app.api.ds

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.qt.app.api.AcfunArticleService
import com.qt.app.api.Api
import com.qt.app.api.dto.ArticleListParamDTO
import com.qt.app.api.realmIdList
import com.qt.app.api.vo.ArticleVO
import com.qt.app.util.Util.toMap
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


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
                this.limit = params.loadSize
            }.toMap()
            cursor.add(currentKey)
            val response = service.getArticlePage(formData, realmIdList[tabId])
            LoadResult.Page(
                data = response.data ?: listOf(),
                prevKey = if (currentKey == "first_page") null else cursor[cursor.lastIndex - 1],
                nextKey = if (response.data == null || response.data!!.isEmpty()) null else response.cursor
            )
        } catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }
}
