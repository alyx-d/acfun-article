package com.qt.app.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.qt.app.api.Repository
import com.qt.app.api.vo.ArticleDetailVO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import org.jsoup.Jsoup
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val repo: Repository,
    private val json: Json,
) : ViewModel() {

    private val _articleList0 = repo.getArticleList(0).cachedIn(viewModelScope)
    private val _articleList1 = repo.getArticleList(1).cachedIn(viewModelScope)
    private val _articleList2 = repo.getArticleList(2).cachedIn(viewModelScope)
    private val _articleList3 = repo.getArticleList(3).cachedIn(viewModelScope)
    val articleListTab = arrayOf(_articleList0, _articleList1, _articleList2, _articleList3)

    private val _articleContent = MutableStateFlow<ArticleDetailVO?>(null)
    val articleContent = _articleContent.asStateFlow()

    suspend fun getArticleDetail(articleId: Int) {
        val data = repo.getArticleDetail(articleId)
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
            val detail = json.decodeFromString<ArticleDetailVO>(content)
            _articleContent.emit(detail)
        }
    }
}




