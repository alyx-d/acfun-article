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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.qt.app.core.data.vo.SubCommentPageVO
import com.qt.app.core.utils.Util

@Composable
fun ArticleSubCommentItem(comment: SubCommentPageVO.SubComment?) {
    val c = comment ?: return
    Column(
        modifier = Modifier
            .padding(5.dp)
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
                    imageLoader = Util.imageLoader(LocalContext.current),
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
            if (c.replyToUserName.isNotBlank() && c.content.contains("回复 @").not()) {
                c.content = "回复 @${c.replyToUserName}: ${c.content}"
            }
            ContentImageParse(c.content, lineHeight = 24.sp)
            Box(
                modifier = Modifier.padding(top = 5.dp)
            ) {
                Row {
                    arrayOf(
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
        }
    }
}