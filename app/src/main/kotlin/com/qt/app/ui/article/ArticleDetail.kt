package com.qt.app.ui.article

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.qt.app.R
import com.qt.app.ui.common.ImageViewer
import com.qt.app.util.Util.imageLoader
import com.qt.app.vm.ArticleCommentViewModel
import com.qt.app.vm.ArticleViewModel
import org.jsoup.Jsoup

@Composable
fun ArticleDetail(navController: NavHostController, backStackEntry: NavBackStackEntry) {
    val articleId = backStackEntry.arguments?.getInt("articleId")
    val vm = hiltViewModel<ArticleViewModel>()
    val cvm = hiltViewModel<ArticleCommentViewModel>()
    val articleDetail by vm.articleContent.collectAsState()
    val comments = cvm.commentList.collectAsLazyPagingItems()
    LaunchedEffect(articleId) {
        articleId?.let {
            vm.getArticleDetail(it)
            cvm.getUserEmotion()
            cvm.getCommentList(it)
        }
    }
    val context = LocalContext.current
    if (articleDetail == null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.Center),
                painter = rememberAsyncImagePainter(R.drawable.loading_ac, imageLoader(context)),
                contentDescription = "loading",
                contentScale = ContentScale.Fit,
            )
        }
    } else {
        val it = articleDetail ?: return
        val imageSet = remember { mutableSetOf<String>() }
        val imageViewerState = remember {
            mutableStateOf(false)
        }
        val currImage = remember {
            mutableStateOf<String?>(null)
        }
        Box {
            LazyColumn(
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .padding(10.dp)
                    ) {
                        Text(
                            text = it.title, fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                        val html = Jsoup.parseBodyFragment(it.parts[0].content)
                        html.allElements.forEach { el ->
                            if ((el.nameIs("p") || el.nameIs("div")) && el.text().isNotBlank()) {
                                Text(text = el.text())
                            } else if (el.nameIs("img")) {
                                val src = el.attr("src")
                                imageSet.add(src)
                                AsyncImage(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            imageViewerState.value = true
                                            currImage.value = src
                                        },
                                    model = src,
                                    imageLoader = imageLoader(context),
                                    contentDescription = "",
                                    contentScale = ContentScale.FillWidth
                                )
                            }
                        }
                    }
                }
                if (comments.itemCount == 0) {
                    item {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            textAlign = TextAlign.Center,
                            text = "暂无评论",
                            color = MaterialTheme.colorScheme.secondary
                        )
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
                }
                items(comments.itemCount) {
                    ArticleComment(comments[it])
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


