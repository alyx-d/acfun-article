package com.qt.app.core.data.dto

import java.time.Instant

data class VideoInfoDTO(
    var quickViewId: String = "videoInfo_new",
    var ajaxpipe: String = "1",
    var t: Long = Instant.now().toEpochMilli()
)
