package com.qt.app.feature.article.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.qt.app.feature.article.api.Repository
import com.qt.app.feature.article.api.vo.CommentPageVO
import com.qt.app.feature.article.api.vo.UserEmotionVO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ArticleCommentViewModel @Inject constructor(
    private val repo: Repository,
) : ViewModel() {

    private val _commentList: MutableStateFlow<PagingData<CommentPageVO.Comment>> = MutableStateFlow(PagingData.empty())
    val commentList = _commentList.cachedIn(viewModelScope)

    suspend fun getCommentList(sourceId: Int) {
        repo.getArticleCommentList(sourceId).collect { data ->
            _commentList.emit(data)
        }
    }

    private val _userEmotion = MutableStateFlow<Map<String, UserEmotionVO.Emotion>>(mapOf())
    val userEmotion = _userEmotion.asStateFlow()

    suspend fun getUserEmotion() {
        val data = repo.getUserEmotion()
        if (data.result == 0) {
            val map = mutableMapOf<String, UserEmotionVO.Emotion>()
            data.emotionPackageList.forEach { pack ->
                pack.emotions.forEach { emo ->
                    map[emo.id.toString()] = emo
                }
            }
            _userEmotion.emit(map)
        }
    }
}