package com.qt.app.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.ImageDecoderDecoder
import com.qt.app.R
import com.qt.app.vm.ArticleViewModel
import org.jsoup.Jsoup

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun ArticleDetail(navHostController: NavHostController, backStackEntry: NavBackStackEntry) {
    val articleId = backStackEntry.arguments?.getString("articleId")
    val vm = hiltViewModel<ArticleViewModel>()
    val articleDetail by vm.articleContent.collectAsState()
    LaunchedEffect(articleDetail) {
        articleId?.let {
            vm.getArticleDetail(it)
        }
    }
    if (articleDetail == null) {
        val context = LocalContext.current
        val imageLoader = ImageLoader.Builder(context)
            .components { add(ImageDecoderDecoder.Factory()) }
            .build()
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.Center),
                painter = rememberAsyncImagePainter(R.drawable.loading_ac, imageLoader),
                contentDescription = "loading",
                contentScale = ContentScale.Fit,
            )
        }
    } else {
        val it = articleDetail!!
        Column(
            modifier = Modifier
                .padding(10.dp)
                .verticalScroll(rememberScrollState())
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
                    AsyncImage(
                        modifier = Modifier.fillMaxWidth(),
                        model = el.attr("src"), contentDescription = "",
                        contentScale = ContentScale.FillWidth
                    )
                }
            }
        }
    }
}


