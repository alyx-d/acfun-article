package com.qt.app.api.vo

data class CommentPage(
    var commentCount: Int,
    var curPage: Int,
    var totalPage: Int,
    var hotComments: List<Comment>,
    var rootComments: List<Comment>,
    var subCommentsMap: Map<Int, SubComment>,
    var pageSize: Int,
    var result: Int,
)

data class Comment(
    var commentId: Int,
    var userId: Int,
    var userName: String,
    var content: String,
    var subCommentCount: Int,
    var postDate: String,
    var nameRed: Int,
    var sourceId: Int,
    var sourceType: Int,
    var userHeadImgInfo: UserHeadImgInfo,
    var replyTo: Int,
    var replyToUserName: String,
)

data class UserHeadImgInfo (
    var thumbnailImageCdnUrl: String,
    var height: Int,
    var width: Int,
)

data class SubComment(
    var pcursor: String,
    var subComments: List<Comment>
)
