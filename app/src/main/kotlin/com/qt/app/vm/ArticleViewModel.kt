package com.qt.app.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.qt.app.api.Repo
import com.qt.app.api.vo.ArticleVO
import com.qt.app.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.jsoup.Jsoup
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val repo: Repo
) : ViewModel() {

    private val _articleList = MutableStateFlow<PagingData<ArticleVO>>(PagingData.empty())
    val articleList = _articleList.asStateFlow()

    private val _articleContent = MutableStateFlow<ArticleDetail?>(null)
    val articleContent = _articleContent.asStateFlow()

    suspend fun getArticleList(tabId: Int) {
        repo.getArticleList(tabId).cachedIn(viewModelScope).collect {
            _articleList.emit(it)
        }
    }


    suspend fun getArticleDetail(articleId: String) {
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
        var articleId: Int,
        var parts: List<Part>,
    )
}




