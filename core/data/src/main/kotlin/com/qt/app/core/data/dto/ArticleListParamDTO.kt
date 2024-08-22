package com.qt.app.core.data.dto


data class ArticleListParamDTO (
    var cursor: String = "first_page",
    var onlyOrigin: Boolean = false,
    var limit: Int = 20,
//    var sortType: String = "createTime",
    var sortType: String = "lastCommentTime",
//    var sortType: String = "hotScore",
    var timeRange: String = "all",
)