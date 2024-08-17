package com.qt.app.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.qt.app.feature.article.api.Repository
import com.qt.app.feature.article.api.vo.ArticleDetailVO
import com.qt.app.core.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

    private val _articleDetailUiState = MutableStateFlow<UiState>(UiState.Loading)
    val articleDetailUiState = _articleDetailUiState.asStateFlow()

    fun getArticleDetail(articleId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getArticleDetail(articleId)
            if (response.isSuccessful && response.body() != null) {
                // 解析
                val document = Jsoup.parse(response.body()!!)
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
                    _articleDetailUiState.emit(UiState.Success(detail))
                }
            }else {
                _articleDetailUiState.emit(UiState.Error())
            }
        }
    }
}




