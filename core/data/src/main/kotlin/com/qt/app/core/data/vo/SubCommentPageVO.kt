package com.qt.app.core.data.vo

import kotlinx.serialization.Serializable

@Serializable
data class SubCommentPageVO(
    var contentTitle: String,
    var curPage: Int,
    var totalPage: Int,
    var subCommentCount: Int,
    var subComments: List<SubComment>
) {
    val nextPage: Int?
        get() = if (curPage <= totalPage) curPage + 1 else null

    @Serializable
    data class SubComment(
        var commentId: Int,
        var userId: Int,
        var userName: String,
        var content: String,
        var subCommentCount: Int = 0,
        var subCommentCountFormat: String = "",
        var postDate: String,
        var nameRed: Int,
        var sourceId: Int,
        var sourceType: Int,
        var userHeadImgInfo: CommentPageVO.UserHeadImgInfo,
        var replyTo: Int,
        var replyToUserName: String,
        var floor: Int,
        var deviceModel: String,
        var headUrl: List<CommentPageVO.HeadUrl>,
    )
}