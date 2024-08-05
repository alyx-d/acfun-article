package com.qt.app.api.vo

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

data class Comment(
    var commentId: Int,
    var userId: Int,
    var userName: String,
    var content: String,
    var subCommentCount: Int?,
    var subCommentCountFormat: String?,
    var postDate: String,
    var nameRed: Int,
    var sourceId: Int,
    var sourceType: Int,
    var userHeadImgInfo: UserHeadImgInfo,
    var replyTo: Int,
    var replyToUserName: String,
    var floor: Int,
    var deviceModel: String,
    var headUrl: List<HeadUrl>,
    var info: CommentPageVO, // 保留信息
)

data class HeadUrl(
    var url: String,
)

data class UserHeadImgInfo (
    var thumbnailImageCdnUrl: String,
    var height: Int,
    var width: Int,
)

data class SubCommentList(
    var pcursor: String,
    var subComments: List<Comment>
)
