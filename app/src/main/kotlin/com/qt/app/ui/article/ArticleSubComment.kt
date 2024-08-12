package com.qt.app.ui.article

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.qt.app.api.vo.Comment
import com.qt.app.vm.ArticleSubCommentViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleSubComment(comment: Comment) {
    val subComment = comment.info?.subCommentsMap?.get(comment.commentId) ?: return
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val subCommentVm = hiltViewModel<ArticleSubCommentViewModel>()
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            scope.launch {
                subCommentVm.getSubCommentList(comment.sourceId, comment.commentId)
            }
            showBottomSheet = true
        },
        shape = ShapeDefaults.ExtraSmall,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.padding(5.dp)
        ) {
            subComment.subComments.forEach {
                Text(
                    buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            append("${it.userName}: ")
                        }
                        withStyle(SpanStyle(fontSize = 11.sp)) {
                            append(it.content)
                        }
                    },
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            comment.subCommentCountFormat.let {
                if (it.isNotBlank()) {
                    Text(text = "共有${it}条回复 >", fontSize = 11.sp)
                }
            }
        }
    }
    if (showBottomSheet) {
        val subCommentList = subCommentVm.subComment.collectAsLazyPagingItems()
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState,
            scrimColor = Color(0x5CFFFFFF)
        ) {
            LazyColumn {
                items(subCommentList.itemCount) {
                    ArticleSubCommentSheet(subCommentList[it])
                }
            }
        }
    }
}


@Preview
@Composable
fun ArticleSubCommentTest() {
    Card(
        shape = ShapeDefaults.ExtraSmall,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(5.dp)
        ) {
            Text(
                buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.scrim
                        )
                    ) {
                        append("用户名: ")
                    }
                    withStyle(SpanStyle(fontSize = 12.sp)) {
                        append("评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容")
                    }
                },
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(text = "共有12条回复 >", fontSize = 11.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun BottomSheet() {
    ModalBottomSheet(
        onDismissRequest = {

        },
    ) {
        // Sheet content
        Button(onClick = {
        }) {
            Text("Hide bottom sheet")
        }
    }
}