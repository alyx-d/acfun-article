package com.qt.app.feature.article.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.compose.rememberAsyncImagePainter
import com.qt.app.core.data.vo.ArticleDetailVO
import com.qt.app.core.navigation.AcfunScreens
import com.qt.app.core.ui.common.ImageViewer
import com.qt.app.core.ui.common.PageLoading
import com.qt.app.core.ui.common.usernameColor
import com.qt.app.core.ui.components.ContentImageParse
import com.qt.app.core.ui.components.NoCommentComponent
import com.qt.app.core.ui.components.PagingFooter
import com.qt.app.core.ui.state.UiState
import com.qt.app.core.utils.Util
import com.qt.app.feature.article.R
import com.qt.app.feature.article.vm.ArticleCommentViewModel
import com.qt.app.feature.article.vm.ArticleViewModel
import org.jsoup.Jsoup

@Composable
fun ArticleDetail(navController: NavHostController, backStackEntry: NavBackStackEntry) {
    val articleId = backStackEntry.arguments?.getInt("articleId")
    val vm = hiltViewModel<ArticleViewModel>()
    val cvm = hiltViewModel<ArticleCommentViewModel>()
    val articleDetailUiState by vm.articleDetailUiState.collectAsState()
    val emotionMap by cvm.userEmotion.collectAsState()
    val comments = cvm.commentList.collectAsLazyPagingItems()
    LaunchedEffect(articleId) {
        articleId?.let {
            vm.getArticleDetail(it)
            cvm.getUserEmotion()
            cvm.getCommentList(it)
        }
    }
    val context = LocalContext.current
    when (articleDetailUiState) {
        is UiState.Error -> {}
        UiState.Loading -> PageLoading(context = context)
        is UiState.Success<*> -> {
            val it = articleDetailUiState.success<ArticleDetailVO>()
            val imageSet = remember { mutableSetOf<String>() }
            val imageViewerState = remember {
                mutableStateOf(false)
            }
            val currImage = remember {
                mutableStateOf<String?>(null)
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                LazyColumn {
                    item {
                        Column(
                            modifier = Modifier
                                .padding(10.dp)
                        ) {
                            Text(
                                text = it.title, fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                            )
                            Spacer(modifier = Modifier.padding(vertical = 5.dp))
                            Row {
                                val tips = arrayOf(
                                    it.createTime,
                                    "${it.formatViewCount}人阅读",
                                    "AC${it.articleId}"
                                )
                                tips.forEachIndexed { idx, it ->
                                    Text(
                                        text = it, fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.alignByBaseline()
                                    )
                                    if (idx != tips.lastIndex) {
                                        Spacer(modifier = Modifier.width(10.dp))
                                    }
                                }
                            }
                            Row(
                                modifier = Modifier.padding(vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = it.user.userHeadImgInfo.thumbnailImageCdnUrl,
                                    contentDescription = null,
                                    imageLoader = Util.imageLoader(LocalContext.current),
                                    modifier = Modifier
                                        .padding(horizontal = 5.dp)
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(50))
                                )
                                Text(text = it.user.name, color = usernameColor(it.user.nameColor))
                            }
                            val html = Jsoup.parse(it.parts[0].content)
                            html.body().allElements.forEach { el ->
                                if ((el.nameIs("p") || el.nameIs("div")) && el.ownText()
                                        .isNotBlank()
                                ) {
                                    // 可能存在存在表情包
                                    ContentImageParse(
                                        content = el.ownText(),
                                        emotionMap = emotionMap,
                                        fontSize = 16.sp,
                                        onResourceClick = { id ->
                                            navController.navigate(
                                                AcfunScreens.ArticleDetail.createRoute(id.toInt())
                                            )
                                        },
                                    )
                                } else if (el.nameIs("img")) {
                                    val src = el.attr("src")
                                    imageSet.add(src)
                                    SubcomposeAsyncImage(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                imageViewerState.value = true
                                                currImage.value = src
                                            },
                                        model = src,
                                        imageLoader = Util.imageLoader(context),
                                        contentDescription = "",
                                        contentScale = ContentScale.FillWidth
                                    ) {
                                        when (val state = painter.state) {
                                            AsyncImagePainter.State.Empty -> {
                                            }

                                            is AsyncImagePainter.State.Error -> {
                                                Text(text = "图片加载失败")
                                            }

                                            is AsyncImagePainter.State.Loading -> {
                                                Image(
                                                    painter = rememberAsyncImagePainter(
                                                        R.drawable.image_loading,
                                                        Util.imageLoader(context)
                                                    ),
                                                    contentDescription = null,
                                                    modifier = Modifier.size(40.dp),
                                                    contentScale = ContentScale.Inside
                                                )
                                            }

                                            is AsyncImagePainter.State.Success -> {
                                                SubcomposeAsyncImageContent()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (comments.itemCount == 0) {
                        item {
                            NoCommentComponent()
                        }
                    } else {
                        val commentCount = comments[0]?.info?.commentCount ?: 0
                        item {
                            Text(
                                modifier = Modifier.padding(start = 5.dp),
                                text = "全部评论 $commentCount",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        items(comments.itemCount) {
                            ArticleComment(navController, comments[it])
                        }
                        item {
                            PagingFooter()
                        }
                    }
                }
                if (imageViewerState.value && imageSet.isNotEmpty()) {
                    AnimatedVisibility(visible = imageViewerState.value) {
                        ImageViewer(imageViewerState, imageSet, currImage)
                    }
                }
            }
        }
    }
}


