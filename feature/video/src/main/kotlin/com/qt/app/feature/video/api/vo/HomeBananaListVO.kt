package com.qt.app.feature.video.api.vo

import kotlinx.serialization.Serializable

@Serializable
data class HomeBananaListVO(
    var id: String,
    var html: String,
) {
    @Serializable
    data class VideoInfo(
        var title: String,
        var id: String,
        var coverImage: String,
        var upName: String,
        var clickCount: String,
        var commentCont: String,
        var videoTime: String,
    )
}
