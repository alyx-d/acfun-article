package com.qt.app.api.vo

data class UserEmotionVO(
    var result: Int,
    var emotionPackageList: List<EmotionPackage>
)

data class EmotionPackage(
    var authorId: Int,
    var name: String,
    var id: Int,
    var iconImageInfo: ImageInfo,
    var emotions: List<Emotion>,
)

data class ImageInfo(
    var thumbnailImageCdnUrl: String,
    var size: Int,
)

data class Emotion(
    var id: Int,
    var packageId: Int,
    var name: String,
    var smallImageInfo: ImageInfo,
    var bigImageInfo: ImageInfo,
)

