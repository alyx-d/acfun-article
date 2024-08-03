package com.qt.app.api.dto

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant

data class CommentListParamDTO(
    var sourceId: String, // 评论ID
    var sourceType: Int = 3,
    var page: Int = 1,
    var pivotCommentId: Int = 0,
//    var newPivotCommentId: Int
    var t: Long = Instant.now().toEpochMilli(),
    val supportZtEmot: Boolean = true,
)