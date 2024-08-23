package com.qt.app.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import com.qt.app.core.data.vo.CommentPageVO
import com.qt.app.core.data.vo.SubCommentPageVO
import com.qt.app.core.data.vo.UserEmotionVO
import com.qt.app.core.ui.common.usernameColor
import com.qt.app.core.utils.Util

@Composable
fun CommentComment(
    comment: CommentPageVO.Comment,
    subCommentList: LazyPagingItems<SubCommentPageVO.SubComment>,
    emotionMap: Map<String, UserEmotionVO.Emotion>,
    onClick: (MutableState<Boolean>) -> Unit,
) {
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
                    model = comment.userHeadImgInfo.thumbnailImageCdnUrl,
                    imageLoader = Util.imageLoader(context),
                    contentDescription = "",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(50))
                        .align(Alignment.Center),
                )
                AsyncImage(
                    model = comment.avatarImage,
                    contentDescription = null,
                    imageLoader = Util.imageLoader(context),
                    modifier = Modifier
                        .clip(RoundedCornerShape(7.dp))
                        .size(50.dp)
                        .align(Alignment.Center)
                        .graphicsLayer {
                            scaleX = 1.2f
                            scaleY = 1.2f
                            translationY = -16f
                        }
                )
            }
            Text(
                text = comment.userName,
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = usernameColor(comment.nameColor)
            )
        }
        Column(
            modifier = Modifier
                .padding(start = 60.dp)
                .fillMaxWidth()
        ) {
            ContentImageParse(comment.content, emotionMap, lineHeight = 24.sp)
            Box(
                modifier = Modifier.padding(top = 5.dp)
            ) {
                Row {
                    arrayOf(
                        "#${comment.floor}",
                        comment.postDate,
                        "来自${comment.deviceModel}",
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
            if (comment.info?.subCommentsMap?.isNotEmpty() == true) {
                SubCommentComment(comment, subCommentList, emotionMap) { state -> onClick(state) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubCommentComment(
    comment: CommentPageVO.Comment,
    subCommentList: LazyPagingItems<SubCommentPageVO.SubComment>,
    emotionMap: Map<String, UserEmotionVO.Emotion>,
    onClick: (MutableState<Boolean>) -> Unit,
) {
    val subComment = comment.info?.subCommentsMap?.get(comment.commentId) ?: return
    val sheetState = rememberModalBottomSheetState()
    val showBottomSheet = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onClick(showBottomSheet) },
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
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.primary
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
                    lineHeight = 18.sp
                )
            }
            comment.subCommentCountFormat.let {
                if (it.isNotBlank()) {
                    Text(text = "共有${it}条回复 >", fontSize = 11.sp)
                }
            }
        }
    }
    if (showBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet.value = false
            },
            sheetState = sheetState,
            scrimColor = Color(0x00FFFFFF)
        ) {
            LazyColumn {
                items(subCommentList.itemCount) {
                    if (subCommentList[it] == null) return@items
                    SubCommentItemComment(subCommentList[it]!!, emotionMap)
                }
            }
        }
    }
}

@Composable
fun SubCommentItemComment(
    comment: SubCommentPageVO.SubComment,
    emotionMap: Map<String, UserEmotionVO.Emotion>,
) {
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
                    model = comment.userHeadImgInfo.thumbnailImageCdnUrl,
                    imageLoader = Util.imageLoader(LocalContext.current),
                    contentDescription = "",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(50)),
                )
            }
            Text(
                text = comment.userName,
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = if (comment.nameRed == 1) Color.Red else Color.Unspecified
            )
        }
        Column(
            modifier = Modifier
                .padding(start = 60.dp)
                .fillMaxWidth()
        ) {
            if (comment.replyToUserName.isNotBlank() && comment.content.contains("回复 @").not()) {
                comment.content = "回复 @${comment.replyToUserName}: ${comment.content}"
            }
            ContentImageParse(
                content = comment.content,
                emotionMap = emotionMap,
                lineHeight = 24.sp
            )
            Box(
                modifier = Modifier.padding(top = 5.dp)
            ) {
                Row {
                    arrayOf(
                        comment.postDate,
                        "来自${comment.deviceModel}",
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

@Composable
fun ContentImageParse(
    content: String,
    emotionMap: Map<String, UserEmotionVO.Emotion>,
    fontSize: Int = 12,
    lineHeight: TextUnit = TextUnit.Unspecified,
) {
    val rex = Regex(pattern = "\\[img=图片].+\\[/img]|\\[img].+\\[/img]|\\[emot=acfun,\\d+/]")
    val imgs = rex.findAll(content).mapTo(mutableListOf()) {
        if (it.value.contains("img")) {
            it.value.substring(8, it.value.length - 6)
        } else {
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
                AsyncImage(
                    model = imgs[idx++], contentDescription = "",
                    contentScale = ContentScale.Inside
                )
            }
        }
    } else {
        Text(
            text = content,
            fontSize = fontSize.sp,
            lineHeight = lineHeight
        )
    }
}