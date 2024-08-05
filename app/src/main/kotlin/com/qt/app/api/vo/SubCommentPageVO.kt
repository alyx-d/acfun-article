package com.qt.app.api.vo

data class SubCommentPageVO(
    var contentTitle: String,
    var curPage: Int,
    var totalPage: Int,
    var subCommentCount: Int,
    var subComments: List<SubComment>
)

data class SubComment(
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
)