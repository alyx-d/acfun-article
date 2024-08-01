package com.qt.app.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.google.gson.Gson
import com.qt.app.api.Api
import com.qt.app.api.ds.Repo
import com.qt.app.api.dto.ArticleParamDTO
import com.qt.app.api.vo.ArticleVO
import com.qt.app.util.Util.toMap
import org.jsoup.Jsoup

class ArticleViewModel : ViewModel() {
    private val service = Api.acfunArticleService
    private val detailService = Api.acfunArticleDetailService

    private var _articleList = MutableLiveData<MutableList<ArticleVO>>()
    val articleList: LiveData<MutableList<ArticleVO>> = _articleList


    val list0 = Repo.getArticleList(0).cachedIn(viewModelScope)
    val list1 = Repo.getArticleList(1).cachedIn(viewModelScope)
    val list2 = Repo.getArticleList(2).cachedIn(viewModelScope)
    val list3 = Repo.getArticleList(3).cachedIn(viewModelScope)

    val dataList = arrayOf(list0, list1, list2, list3)

    private val _articleContent = MutableLiveData<ArticleDetail>()
    val articleContent: LiveData<ArticleDetail> = _articleContent

    suspend fun getArticleList(next: Boolean = false) {
        val dto = ArticleParamDTO()
        val data = service.getArticlePage(dto.toMap()).data
        _articleList.postValue(data!!)
    }

    private val gson = Gson()

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
            val detail = gson.fromJson(content, ArticleDetail::class.java)
            _articleContent.postValue(detail)
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




