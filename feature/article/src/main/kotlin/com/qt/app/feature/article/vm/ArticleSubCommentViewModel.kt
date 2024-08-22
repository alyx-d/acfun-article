package com.qt.app.feature.article.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.qt.app.core.data.repo.ArticleRepository
import com.qt.app.core.data.vo.SubCommentPageVO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ArticleSubCommentViewModel @Inject constructor(
    private val repo: ArticleRepository
) : ViewModel() {

    private val _subCommentList = MutableStateFlow<PagingData<SubCommentPageVO.SubComment>>(PagingData.empty())
    val subComment = _subCommentList.cachedIn(viewModelScope)

    suspend fun getSubCommentList(sourceId: Int, rootCommentId: Int) {
        repo.getArticleSubCommentList(sourceId, rootCommentId).collect {
            _subCommentList.emit(it)
        }
    }
}