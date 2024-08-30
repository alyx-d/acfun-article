package com.qt.app.feature.article.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.qt.app.core.data.vo.CommentPageVO
import com.qt.app.core.navigation.AcfunScreens
import com.qt.app.core.ui.components.CommentComponent
import com.qt.app.feature.article.vm.ArticleCommentViewModel
import com.qt.app.feature.article.vm.ArticleSubCommentViewModel
import kotlinx.coroutines.launch

@Composable
fun ArticleComment(
    navController: NavHostController,
    comment: CommentPageVO.Comment?,
    cvm: ArticleCommentViewModel = hiltViewModel(),
    subCommentVm: ArticleSubCommentViewModel = hiltViewModel(),
) {
    if (comment == null) return
    val scope = rememberCoroutineScope()
    val emotionMap by cvm.userEmotion.collectAsState()
    val subCommentList = subCommentVm.subComment.collectAsLazyPagingItems()
    CommentComponent(
        comment,
        subCommentList,
        emotionMap,
        onMoreCommentClick = { _ ->
            scope.launch {
                subCommentVm.getSubCommentList(comment.sourceId, comment.commentId)
            }
        },
        onResourceClick = { id ->
            navController.navigate(AcfunScreens.ArticleDetail.createRoute(id.toInt()))
        }
    )

}