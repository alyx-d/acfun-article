package com.qt.app.api.dto


data class ArticleParamDTO (
    var cursor: String = "first_page",
    var onlyOrigin: Boolean = false,
    var limit: Int = 10,
    var sortType: String = "createTime",
    var timeRange: String = "all",
)