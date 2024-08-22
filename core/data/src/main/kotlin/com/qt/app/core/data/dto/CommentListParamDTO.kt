package com.qt.app.core.data.dto

import java.time.Instant

data class CommentListParamDTO(
    var sourceId: Int, // 评论ID
    var sourceType: Int = 3,
    var page: Int = 1,
    var limit: Int = 20,
    var pivotCommentId: Int = 0,
//    var newPivotCommentId: Int
    var t: Long = Instant.now().toEpochMilli(),
    val supportZtEmot: Boolean = true,
)