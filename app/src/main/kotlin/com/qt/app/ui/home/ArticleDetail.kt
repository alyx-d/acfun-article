package com.qt.app.ui.home

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.qt.app.vm.ArticleViewModel
import org.jsoup.Jsoup

@Composable
fun ArticleDetail(navController: NavHostController, backStackEntry: NavBackStackEntry) {
    val articleId = backStackEntry.arguments?.getString("articleId")
    val vm = viewModel(ArticleViewModel::class.java)
    val articleDetail by vm.articleContent.observeAsState()
    LaunchedEffect("key") {
        articleId?.let {
            vm.getArticleDetail(it)
        }
    }
    articleDetail?.let {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = it.title, fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )
            val html = Jsoup.parseBodyFragment(it.parts[0].content)
            html.allElements.forEach { el ->
                if (el.nameIs("p") && el.text().isNotBlank()) {
                    Text(text = el.text())
                }else if (el.nameIs("div") && el.text().isNotBlank()){
                    Text(text = el.text())
                }
                else if(el.nameIs("img")) {
                    AsyncImage(
                        modifier = Modifier.fillMaxWidth(),
                        model = el.attr("src"), contentDescription = "",
                        contentScale = ContentScale.FillWidth)
                }
            }
        }
    }
}