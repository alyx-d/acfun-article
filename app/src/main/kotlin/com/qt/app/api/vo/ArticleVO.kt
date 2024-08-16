package com.qt.app.api.vo

import kotlinx.serialization.Serializable

@Serializable
data class ArticleVO (
    var articleId: Int,
    var commentCount: Int,
    var createTime: Long,
    var description: String,
    var formatCommentCount: String,
    var formatViewCount: String,
    var isOriginal: Boolean,
    var realmId: Int,
    var realmName: String,
    var title: String,
    var userId: Int,
    var userName: String,
    var viewCount: Int,
    var coverImgInfo: CoverImgInfo
) {
    @Serializable
    data class CoverImgInfo(
        var animated: Boolean,
        var thumbnailImageCdnUrl: String,
    )
}
