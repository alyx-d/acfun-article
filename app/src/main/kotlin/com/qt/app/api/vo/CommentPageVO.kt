package com.qt.app.api.vo

import kotlinx.serialization.Serializable

@Serializable
data class CommentPageVO(
    var commentCount: Int,
    var curPage: Int,
    var totalPage: Int,
    var hotComments: List<Comment>,
    var rootComments: List<Comment>,
    var subCommentsMap: Map<Int, SubCommentList>,
    var pageSize: Int,
    var result: Int,
)

@Serializable
data class Comment(
    var commentId: Int,
    var userId: Int,
    var userName: String,
    var content: String,
    var subCommentCount: Int = 0,
    var subCommentCountFormat: String = "",
    var postDate: String,
    var nameRed: Int,
    var nameColor: Int,
    var sourceId: Int,
    var sourceType: Int,
    var userHeadImgInfo: UserHeadImgInfo,
    var replyTo: Int,
    var replyToUserName: String,
    var floor: Int,
    var deviceModel: String,
    var headUrl: List<HeadUrl>,
    var info: CommentPageVO? = null, // 保留信息
)

@Serializable
data class HeadUrl(
    var url: String,
)

@Serializable
data class UserHeadImgInfo (
    var thumbnailImageCdnUrl: String,
    var height: Int,
    var width: Int,
)

@Serializable
data class SubCommentList(
    var pcursor: String,
    var subComments: List<Comment>
)
