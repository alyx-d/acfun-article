package com.qt.app.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.google.gson.GsonBuilder
import com.qt.app.api.Api
import com.qt.app.api.ds.Repo
import com.qt.app.api.dto.ArticleParamDTO
import com.qt.app.api.vo.ArticleVO
import com.qt.app.util.Util
import com.qt.app.util.Util.toMap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.jsoup.Jsoup
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    repo: Repo
) : ViewModel() {
    private val service = Api.acfunArticleService
    private val detailService = Api.acfunArticleDetailService

    private val _articleList = MutableStateFlow<MutableList<ArticleVO>>(mutableListOf())
    val articleList = _articleList.asStateFlow()


    private val list0 = repo.getArticleList(0).cachedIn(viewModelScope)
    private val list1 = repo.getArticleList(1).cachedIn(viewModelScope)
    private val list2 = repo.getArticleList(2).cachedIn(viewModelScope)
    private val list3 = repo.getArticleList(3).cachedIn(viewModelScope)

    val dataList = arrayOf(list0, list1, list2, list3)

    private val _articleContent = MutableStateFlow<ArticleDetail?>(null)
    val articleContent = _articleContent.asStateFlow()

    suspend fun getArticleList(next: Boolean = false) {
        val dto = ArticleParamDTO()
        val data = service.getArticlePage(dto.toMap()).data
        _articleList.emit(data!!)
    }


    suspend fun getArticleDetail(articleId: String) {
        val data = detailService.getArticleDetail(articleId)
        // 解析
        val document = Jsoup.parse(data)
        val script = document.select("#main > script")
        if (script.isNotEmpty()) {
            val html = script[0].html()
            val len = html.length
            var content = html.substring(21, len - 45)
            content = content.trim()
            if (content[content.lastIndex] == ';') {
                content = content.substring(0, content.length - 1)
            }
            val detail = Util.gson.fromJson(content, ArticleDetail::class.java)
            _articleContent.emit(detail)
        }
    }

    data class Part(
        var title: String,
        var content: String,
    )

    data class ArticleDetail(
        var title: String,
        var parts: List<Part>,
    )
}




