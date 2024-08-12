package com.qt.app.api.vo

import kotlinx.serialization.Serializable

@Serializable
data class ArticleDetailVO(
    var title: String,
    var articleId: Int,
    var parts: List<Part>,
)

@Serializable
data class Part(
    var title: String,
    var content: String,
)
