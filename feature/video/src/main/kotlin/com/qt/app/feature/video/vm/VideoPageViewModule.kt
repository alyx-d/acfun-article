package com.qt.app.feature.video.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qt.app.core.ui.state.UiState
import com.qt.app.feature.video.api.service.VideoService
import com.qt.app.feature.video.api.vo.HomeBananaListVO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.jsoup.Jsoup
import javax.inject.Inject

@HiltViewModel
class VideoPageViewModule @Inject constructor(
    private val json: Json,
    private val videoService: VideoService,
) : ViewModel(){

    init {
        getHomePage()
    }
    private val _videoUiState = MutableStateFlow<UiState>(UiState.Loading)
    val videoUiState = _videoUiState.asStateFlow()

    private fun getHomePage() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = videoService.acfunHome()
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                val html = Jsoup.parse(body)
                val scripts = html.select("script")
                val videos = mutableListOf<List<HomeBananaListVO.VideoInfo>>()
                if (scripts.isNotEmpty()) {
                    scripts.forEach {
                        val scriptText = it.html()
                        if (scriptText.contains("bigPipe.onPageletArrive")) {
                            val str = scriptText.substring(scriptText.indexOf("{"), scriptText.lastIndexOf("}") + 1)
                            val data = json.decodeFromString<HomeBananaListVO>(str)
                            when (data.id) {
                                "pagelet_list_banana" -> {
                                    videos.add(parseVideoInfoFromHtml(data.html, "day-list"))
                                    videos.add(parseVideoInfoFromHtml(data.html, "three-day-list"))
                                    videos.add(parseVideoInfoFromHtml(data.html, "week-list"))
                                }
                            }
                        }
                    }
                }
                _videoUiState.emit(UiState.Success(videos))
            }else {
                _videoUiState.emit(UiState.Error())
            }
        }
    }

    private fun parseVideoInfoFromHtml(html: String, type: String): List<HomeBananaListVO.VideoInfo> {
        val data = mutableListOf<HomeBananaListVO.VideoInfo>()
        val part = Jsoup.parseBodyFragment(html)
        val el = part.body().selectFirst(".banana-list.$type")
        el?.select("div.banana-video")?.forEach { element ->
            val id = element.attr("data-mediaid")
            val cover = element.select("a.banana-video-cover img").attr("src")
            val videoTime = element.select("span.video-time").first()?.ownText() ?: ""
            val titleEl = element.select("a.banana-video-title")
            val info = titleEl.attr("title").split("/")
            if (info.size != 3) return@forEach
            val titleAndUp = info[0].split("\r").filter { it.isNotBlank() }
            data.add(HomeBananaListVO.VideoInfo(
                title = titleAndUp[0],
                coverImage = cover,
                id = id,
                upName = titleAndUp[1],
                clickCount =  info[1].substring(4),
                commentCont = info[2].substring(4),
                videoTime = videoTime,
            ))
        }
        return data
    }
}