package com.qt.app.ui.article

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.qt.app.api.vo.Comment

@Composable
fun ArticleComment(comment: Comment?) {
    val c = comment ?: return
    Row {
        Box {
            AsyncImage(
                model = c.userHeadImgInfo.thumbnailImageCdnUrl, contentDescription = "",
                modifier = Modifier
                    .width(c.userHeadImgInfo.width.dp)
                    .height(c.userHeadImgInfo.height.dp)
                    .clip(RoundedCornerShape(50))
            )
        }
        Column {
            Text(text = c.userName)
            Text(text = c.content)
        }
    }
}