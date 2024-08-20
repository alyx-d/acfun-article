package com.qt.app.feature.video.api.vo

import kotlinx.serialization.Serializable

@Serializable
data class KsPlayJson(
    var videoId: String,
    var mediaType: Int,
    var adaptationSet: List<Adaptation>
) {
    @Serializable
    data class Adaptation(
        var duration: Int,
        var representation: List<PlayInfo>
    ) {
        @Serializable
        data class PlayInfo(
            var width: Int,
            var height: Int,
            var qualityType: String,
            var qualityLabel: String,
            var url: String,
        )
    }
}
