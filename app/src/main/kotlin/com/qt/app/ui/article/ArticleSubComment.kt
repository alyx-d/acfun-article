package com.qt.app.ui.article

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qt.app.api.vo.SubComment

@Composable
fun ArticleSubComment(subComment: SubComment, subCommentCountFormat: String) {
    Card(
        shape = ShapeDefaults.ExtraSmall,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.padding(5.dp)
        ) {
            subComment.subComments.forEach {
                Text(
                    buildAnnotatedString {
                        withStyle(SpanStyle(fontSize = 10.sp, color = MaterialTheme.colorScheme.secondary)) {
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
            Text(text = "共有${subCommentCountFormat}条回复 >", fontSize = 11.sp)
        }
    }
}


@Preview
@Composable
fun ArticleSubCommentTest() {
    Card(
        shape = ShapeDefaults.ExtraSmall,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.padding(5.dp)
        ) {
            Text(
                buildAnnotatedString {
                    withStyle(SpanStyle(fontSize = 11.sp, color = MaterialTheme.colorScheme.scrim)) {
                        append("用户名: ")
                    }
                    withStyle(SpanStyle(fontSize = 12.sp)) {
                        append("评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容")
                    }
                },
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )
            Text(text = "共有12条回复 >", fontSize = 11.sp)
        }
    }
}