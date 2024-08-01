package com.qt.app.api.ds

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.qt.app.api.AcfunArticleService
import com.qt.app.api.Api
import com.qt.app.api.dto.ArticleParamDTO
import com.qt.app.api.realmIdList
import com.qt.app.api.vo.ArticleVO
import com.qt.app.util.Util.toMap
import kotlinx.coroutines.flow.Flow

class DataSource(val service: AcfunArticleService, var tabId: Int = 0) : PagingSource<String, ArticleVO>() {
    override fun getRefreshKey(state: PagingState<String, ArticleVO>): String? {
        return null
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, ArticleVO> {
        val currentKey = params.key ?: "first_page"
        return try {
            val formData = ArticleParamDTO().apply {
                this.cursor = currentKey
            }.toMap()
            val response = service.getArticlePage(formData, realmIdList[tabId])
            LoadResult.Page(
                data = response.data!!,
                prevKey = if (currentKey == "first_page") null else currentKey,
                nextKey = response.cursor
            )
        } catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }
}

object Repo {
    fun getArticleList(tabId: Int): Flow<PagingData<ArticleVO>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { DataSource(Api.acfunArticleService, tabId) },
        ).flow
    }
}