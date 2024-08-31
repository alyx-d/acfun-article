package com.qt.app.feature.video.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.qt.app.core.data.repo.ArticleRepository
import com.qt.app.core.data.service.VideoService
import com.qt.app.core.data.vo.CommentPageVO
import com.qt.app.core.data.vo.HomeBananaListVO
import com.qt.app.core.data.vo.KsPlayJson
import com.qt.app.core.data.vo.SubCommentPageVO
import com.qt.app.core.data.vo.UserEmotionVO
import com.qt.app.core.data.vo.VideoInfoVO
import com.qt.app.core.ui.state.UiState
import com.qt.app.core.utils.SnackBarHostStateHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
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
    private val articleRepository: ArticleRepository,
) : ViewModel(){

    private val _videoUiState = MutableStateFlow<UiState>(UiState.Loading)
    val videoUiState = _videoUiState.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        SnackBarHostStateHolder.showMessage(exception.message ?: "error")
        viewModelScope.launch { _videoUiState.emit(UiState.Error(exception)) }
    }

    fun getHomePage(isRefresh: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            if (isRefresh) _refreshState.emit(true)
            val response = videoService.acfunHome()
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                val html = Jsoup.parse(body)
                val scripts = html.select("script")
                val videos =
                    mutableListOf<List<HomeBananaListVO.VideoInfo>>()
                if (scripts.isNotEmpty()) {
                    scripts.forEach {
                        val scriptText = it.html()
                        if (scriptText.contains("bigPipe.onPageletArrive")) {
                            val str = scriptText.substring(scriptText.indexOf("{"), scriptText.lastIndexOf("}") + 1)
                            val data =
                                json.decodeFromString<HomeBananaListVO>(str)
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
            if (isRefresh) _refreshState.emit(false)
        }
    }

    private fun parseVideoInfoFromHtml(
        html: String,
        type: String
    ): List<HomeBananaListVO.VideoInfo> {
        val data = mutableListOf<HomeBananaListVO.VideoInfo>()
        val part = Jsoup.parseBodyFragment(html)
        val el = part.body().selectFirst(".banana-list.$type")
        el?.select("div.banana-video")?.forEach { element ->
            val id = element.attr("data-mediaid")
            val cover = element.select("a.banana-video-cover img").attr("src")
            val videoTime = element.select("span.video-time").first()?.ownText() ?: ""
            val titleEl = element.select("a.banana-video-title")
            val info = titleEl.attr("title").split(" / ")
            if (info.size != 3) return@forEach
            val titleAndUp = info[0].split("\r").filter { it.isNotBlank() }
            data.add(
                HomeBananaListVO.VideoInfo(
                title = titleAndUp[0],
                coverImage = cover,
                id = id,
                upName = titleAndUp[1],
                clickCount =  info[1].substring(3),
                commentCont = info[2].substring(3),
                videoTime = videoTime,
            ))
        }
        return data
    }

    private val _videoPlayInfoUiState = MutableStateFlow<UiState>(UiState.Loading)
    val videoPlayInfoUiState = _videoPlayInfoUiState.asStateFlow()

    fun getVideoPlay(id: String) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val response = videoService.acfunVideo(id)
            if (response.isSuccessful.not() || response.body() == null) {
                _videoPlayInfoUiState.emit(UiState.Error())
            }else {
                val body = response.body()!!
                val html = Jsoup.parse(body)
                val scripts = html.select("script")
                scripts.forEach { script ->
                    val innerHtml = script.html()
                    if (innerHtml.contains("window.pageInfo")) {
                        var content = innerHtml.substring(innerHtml.indexOf("{"), innerHtml.lastIndexOf("window.videoResource")).trim()
                        if (content.last() == ';') {
                            content = content.substring(0, content.length - 1)
                        }
                        val videoInfo =
                            json.decodeFromString<VideoInfoVO>(content)
                        val ksPlayJson =
                            json.decodeFromString<KsPlayJson>(videoInfo.currentVideoInfo.ksPlayJson)
                    }
                }
            }
        }
    }

    fun getVideoPlayInfo(id: String) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val response = videoService.acfunVideoInfo(id + "_1")
            if (response.isSuccessful.not() || response.body() == null) {
                _videoPlayInfoUiState.emit(UiState.Error())
            }else {
                val body = response.body()!!
                val (videoInfo, ksPlayJson) = parseKsPlayJson(body)
                val infos = mutableListOf(ksPlayJson)
                if (videoInfo.videoList.size > 1) {
                    repeat(videoInfo.videoList.size - 1) {
                        val pInfo = videoService.acfunVideoInfo(id + "_${it + 2}")
                        if (pInfo.isSuccessful && pInfo.body() != null) {
                            val (_, pk) = parseKsPlayJson(pInfo.body()!!)
                            infos.add(pk)
                        }
                    }
                }
                _videoPlayInfoUiState.emit(UiState.Success(videoInfo to infos))
            }
        }
    }

    private fun parseKsPlayJson(body: String): Pair<VideoInfoVO, KsPlayJson> {
        val htmlData = json.decodeFromString<VideoInfoVO.HtmlData>(
            body.substring(
                0,
                body.lastIndexOf("}") + 1
            )
        )
        val videoInfoJs =
            Jsoup.parseBodyFragment(htmlData.html).body().select("script.videoInfo").html()
        val content = videoInfoJs.substring(videoInfoJs.indexOf("{"))
        val videoInfo = json.decodeFromString<VideoInfoVO>(content)
        val ksPlayJson = json.decodeFromString<KsPlayJson>(videoInfo.currentVideoInfo.ksPlayJson)
        return videoInfo to ksPlayJson
    }

    private val _refreshState = MutableStateFlow(false)
    val refreshState = _refreshState.asStateFlow()

    fun refresh() {
        getHomePage(true)
    }

    private val _comments = MutableStateFlow<PagingData<CommentPageVO.Comment>>(PagingData.empty())
    val comments = _comments.cachedIn(viewModelScope)
    fun getComments(id: String) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            articleRepository.getArticleCommentList(id.toInt()).collect {
                _comments.emit(it)
            }
        }
    }

    private val _userEmotion = MutableStateFlow<Map<String, UserEmotionVO.Emotion>>(mapOf())
    val userEmotion = _userEmotion.asStateFlow()

    fun getUserEmotion() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val data = articleRepository.getUserEmotion()
            if (data.result == 0) {
                val map = mutableMapOf<String, UserEmotionVO.Emotion>()
                data.emotionPackageList.forEach { pack ->
                    pack.emotions.forEach { emo ->
                        map[emo.id.toString()] = emo
                    }
                }
                _userEmotion.emit(map)
            }
        }
    }

    private val _subComments =
        MutableStateFlow<PagingData<SubCommentPageVO.SubComment>>(PagingData.empty())
    val subComments = _subComments.cachedIn(viewModelScope)

    fun getSubCommentList(sourceId: Int, rootCommentId: Int) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            articleRepository.getArticleSubCommentList(sourceId, rootCommentId).collect {
                _subComments.emit(it)
            }
        }
    }
}