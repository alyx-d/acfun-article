package com.qt.app.core.data.vo

import kotlinx.serialization.Serializable

@Serializable
data class UserEmotionVO(
    var result: Int,
    var emotionPackageList: List<EmotionPackage>
) {

    @Serializable
    data class EmotionPackage(
        var authorId: Int,
        var name: String,
        var id: Int,
        var iconImageInfo: ImageInfo,
        var emotions: List<Emotion>,
    )

    @Serializable
    data class ImageInfo(
        var thumbnailImageCdnUrl: String,
        var size: Int,
    )

    @Serializable
    data class Emotion(
        var id: Int,
        var packageId: Int,
        var name: String,
        var smallImageInfo: ImageInfo,
        var bigImageInfo: ImageInfo,
    )
}
