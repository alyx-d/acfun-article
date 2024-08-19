package com.qt.app.feature.article.ui

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.qt.app.feature.article.api.vo.CommentPageVO
import com.qt.app.feature.article.ui.common.usernameColor
import com.qt.app.core.utils.Util
import com.qt.app.feature.article.vm.ArticleCommentViewModel

@Composable
fun ArticleComment(comment: CommentPageVO.Comment?) {
    val c = comment ?: return
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
            val context = LocalContext.current
            Box(modifier = Modifier.padding(horizontal = 5.dp)) {
                AsyncImage(
                    model = c.userHeadImgInfo.thumbnailImageCdnUrl,
                    imageLoader = Util.imageLoader(context),
                    contentDescription = "",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(50))
                        .align(Alignment.Center)
                    ,
                )
                AsyncImage(model = c.avatarImage, contentDescription = null,
                    imageLoader = Util.imageLoader(context),
                    modifier = Modifier.size(50.dp)
                        .align(Alignment.Center)
                        .graphicsLayer {
                            scaleX = 1.2f
                            scaleY = 1.2f
                            translationY = -16f
                        }
                        .clip(RoundedCornerShape(5))
                )
            }
            Text(
                text = c.userName,
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = usernameColor(c.nameColor)
            )
        }
        Column(
            modifier = Modifier
                .padding(start = 60.dp)
                .fillMaxWidth()
        ) {
            ContentImageParse(c.content, lineHeight = 18.sp)
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
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
            if (c.info?.subCommentsMap?.isNotEmpty() == true) {
                ArticleSubComment(c)
            }
        }
    }
}

@Composable
fun ContentImageParse(content: String, fontSize: Int = 12, lineHeight: TextUnit = TextUnit.Unspecified) {
    val cvm = hiltViewModel<ArticleCommentViewModel>()
    val emotionMap by cvm.userEmotion.collectAsState()
    val rex = Regex(pattern = "\\[img=图片].+\\[/img]|\\[img].+\\[/img]|\\[emot=acfun,\\d+/]")
    val imgs = rex.findAll(content).mapTo(mutableListOf()) {
        if (it.value.contains("img")) {
            it.value.substring(8, it.value.length - 6)
        }else {
            val id = it.value.substring(12, it.value.length - 2)
            emotionMap[id]?.smallImageInfo?.thumbnailImageCdnUrl ?: ""
        }
    }
    if (imgs.isNotEmpty()) {
        val str = "||-=-=-||"
        val c = rex.replace(content, str)
        var idx = 0
        c.split(str).forEach { text ->
            if (text.isNotBlank()) {
                Text(
                    text = text,
                    fontSize = fontSize.sp,
                    lineHeight = lineHeight
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
            fontSize = fontSize.sp,
            lineHeight = lineHeight
        )
    }
}