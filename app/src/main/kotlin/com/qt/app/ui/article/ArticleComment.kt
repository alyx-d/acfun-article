package com.qt.app.ui.article

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.qt.app.R
import com.qt.app.api.vo.Comment

@Composable
fun ArticleComment(comment: Comment?) {
    val c = comment ?: return
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10))
            .background(MaterialTheme.colorScheme.background)
            .padding(5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.padding(horizontal = 5.dp)) {
                AsyncImage(
                    model = c.userHeadImgInfo.thumbnailImageCdnUrl,
                    contentDescription = "",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(50)),
                )
            }
            Text(
                text = c.userName,
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = if (c.nameRed == 1) Color.Red else Color.Unspecified
            )
        }
        Column(
            modifier = Modifier
                .padding(start = 60.dp)
                .fillMaxWidth()
        ) {
            CommentContent(c.content)
            Box(
                modifier = Modifier.padding(top = 5.dp)
            ) {
                Row {
                    arrayOf(
                        "#${c.floor}",
                        c.postDate,
                        "来自${c.deviceModel}",
                    ).forEach {
                        Text(
                            modifier = Modifier
                                .alignByBaseline()
                                .padding(end = 10.dp),
                            text = it,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            if (c.info.subCommentsMap.isNotEmpty()) {
                c.info.subCommentsMap.let { map ->
                    val subComment = map[c.commentId] ?: return
                    ArticleSubComment(subComment, c.subCommentCountFormat)
                }
            }
        }
    }
}

@Composable
fun CommentContent(content: String) {
    val rex = Regex(pattern = "\\[img=图片].+\\[/img]")
    val imgs = rex.findAll(content).mapTo(mutableListOf()) {
        it.value.substring(8, it.value.length - 6)
    }
    if (imgs.isNotEmpty()) {
        val str = "||-=-=-||"
        val c = rex.replace(content, str)
        var idx = 0
        c.split(str).forEach { text ->
            if (text.isNotBlank()) {
                Text(
                    text = text,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                )
            }
            if (idx < imgs.size) {
                AsyncImage(model = imgs[idx++], contentDescription = "",
                    contentScale = ContentScale.Inside)
            }
        }
    }else {
        Text(
            text = content,
            fontSize = 12.sp
        )
    }
}

@Preview
@Composable
private fun Test() {
    val nameRed = 1
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10))
            .background(Color.White)
            .padding(5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.loading_ac),
                contentDescription = "",
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(50)),
            )
            Text(
                text = "用户名",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = if (nameRed == 1) Color.Red else Color.Unspecified
            )
        }
        Column(
            modifier = Modifier
                .padding(start = 50.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "好耶好耶好耶好耶好耶好耶好耶好耶好耶好耶好耶好耶好耶好耶好耶好耶好耶好耶好耶好耶好耶好耶好耶好耶好耶好耶好耶好耶好耶好耶",
                fontSize = 12.sp,
                lineHeight = 15.sp,
            )
            Box(
                modifier = Modifier.padding(top = 5.dp)
            ) {
                Row {
                    Text(
                        modifier = Modifier
                            .alignByBaseline()
                            .padding(end = 10.dp),
                        text = "#1",
                        color = MaterialTheme.colorScheme.primary
                    )

                }

            }
            ArticleSubCommentTest()
        }
    }
}