package com.qt.app.feature.video.api.vo

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
    var user: User
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
        var fileName: String,
    )
    @Serializable
    data class User(
        var id: String,
        var name: String,
        var nameStyle: String,
        var userHeadImgInfo: UserHeadImgInfo,
        var fanCount: String,
    ) {
        @Serializable
        data class UserHeadImgInfo(
            var thumbnailImageCdnUrl: String
        )
    }
}
