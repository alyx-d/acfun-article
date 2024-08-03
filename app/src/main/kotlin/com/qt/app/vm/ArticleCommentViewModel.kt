package com.qt.app.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.qt.app.api.Repo
import com.qt.app.api.vo.Comment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ArticleCommentViewModel @Inject constructor(
    private val repo: Repo,
) : ViewModel() {

    private val _commentList: MutableStateFlow<PagingData<Comment>> = MutableStateFlow(PagingData.empty())
    val commentList = _commentList.asStateFlow()

    suspend fun getCommentList(sourceId: String) {
        repo.getArticleCommentList(sourceId).cachedIn(viewModelScope).collect { data ->
            _commentList.emit(data)
        }
    }
}