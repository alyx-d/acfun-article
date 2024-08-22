package com.qt.app.core.data.vo

import kotlinx.serialization.Serializable

@Serializable
data class VideoInfoVO(
    var currentVideoId: Int,
    var channel: Channel,
    var currentVideoInfo: CurrentVideoInfo,
    var viewCountShow: String,
    var commentCountTenThousandShow: String,
    var title: String,
    var description: String?,
    var videoList: List<VideoPartition>,
    var createTime: String,
    var user: User,
    var coverImgInfo: ImgInfo,
) {
    @Serializable
    data class Channel(
        var id: Int,
        var parentId: Int,
        var name : String,
        var parentName: String,
    )
    @Serializable
    data class CurrentVideoInfo(
        var ksPlayJson: String,
    )
    @Serializable
    data class VideoPartition(
        var priority: Int,
        var durationMillis: Int,
        var uploadTime: Long,
        var title: String,
        var id: Int,
        var fileName: String = "",
    )
    @Serializable
    data class User(
        var id: String,
        var name: String,
        var nameStyle: String,
        var nameColor: Int = 0,
        var userHeadImgInfo: ImgInfo,
        var fanCount: String,
    )

    @Serializable
    data class ImgInfo(
        var thumbnailImageCdnUrl: String
    )

    @Serializable
    data class HtmlData(
        var html: String,
        var js: List<String>,
        var css: List<String>,
    )

}
