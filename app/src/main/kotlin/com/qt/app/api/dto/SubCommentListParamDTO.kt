package com.qt.app.api.dto

import java.time.Instant

data class SubCommentListParamDTO(
    var sourceId: Int,
    var rootCommentId: Int,
    var sourceType: Int = 3,
    var page: Int = 1,
    var limit: Int = 20,
    var t: Long = Instant.now().toEpochMilli(),
    var supportZtEmot: Boolean = true
)
