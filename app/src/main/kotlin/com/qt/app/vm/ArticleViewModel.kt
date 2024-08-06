package com.qt.app.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import com.qt.app.api.Repo
import com.qt.app.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val repo: Repo
) : ViewModel() {

    private val _articleList0 = repo.getArticleList(0).cachedIn(viewModelScope)
    private val _articleList1 = repo.getArticleList(1).cachedIn(viewModelScope)
    private val _articleList2 = repo.getArticleList(2).cachedIn(viewModelScope)
    private val _articleList3 = repo.getArticleList(3).cachedIn(viewModelScope)
    val articleListTab = arrayOf(_articleList0, _articleList1, _articleList2, _articleList3)

    private val _articleContent = MutableStateFlow<ArticleDetail?>(null)
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




