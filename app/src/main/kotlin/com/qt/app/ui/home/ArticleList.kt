package com.qt.app.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.qt.app.ui.Route
import com.qt.app.util.Util
import com.qt.app.vm.ArticleViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ArticleList(navController: NavHostController, backStackEntry: NavBackStackEntry) {
    val vm = hiltViewModel<ArticleViewModel>()
    val arguments = backStackEntry.arguments!!
    val tabId = arguments.getInt("tabId")
    val articleList = vm.dataList[tabId].collectAsLazyPagingItems()
    LazyColumn(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.surface),
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(articleList.itemCount, { idx -> articleList[idx]?.articleId!! }) { idx ->
            val it = articleList[idx] ?: return@items
            Column(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background, shape = RoundedCornerShape(10.dp))
                    .padding(10.dp)
                    .clickable {
                        navController.navigate("${Route.ArticleDetail.name}/${it.articleId}")
                    }
            ) {
                Row {
                    Text(
                        text = it.title,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                AsyncImage(
                    model = it.coverImgInfo.thumbnailImageCdnUrl,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.FillWidth
                )
                Row {
                    Text(
                        text = it.description,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Text(
                            text = "UP:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = it.userName,
                            fontSize = 14.sp,
                            modifier = Modifier.widthIn(max = 150.dp),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )
                    }
                    Text(
                        text = Util.dateFormat(it.createTime),
                        fontSize = 12.sp,
                        modifier = Modifier
                            .padding(start = 5.dp, top = 2.dp)
                    )
                }
            }
        }
    }
}