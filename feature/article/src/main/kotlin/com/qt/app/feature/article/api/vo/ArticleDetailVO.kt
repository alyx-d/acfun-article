package com.qt.app.feature.article.api.vo

import kotlinx.serialization.Serializable

@Serializable
data class ArticleDetailVO(
    var title: String,
    var articleId: Int,
    var parts: List<Part>,
    var tagList: List<Tag> = listOf(),
    var user: User,
    var createTime: String,
    var formatViewCount: String,
) {
    @Serializable
    data class Part(
        var title: String,
        var content: String,
    )

    @Serializable
    data class Tag(
        var name: String,
        var id: Int,
    )

    @Serializable
    data class User(
        var name: String,
        var nameColor: Int,
        var userHeadImgInfo: CommentPageVO.UserHeadImgInfo
    )
}


