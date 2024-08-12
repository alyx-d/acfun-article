package com.qt.app.api.vo

import kotlinx.serialization.Serializable

@Serializable
data class ResultVO <T> (
    var cursor: String = "",
    var result: Int = 0,
    var data: T? = null,
    var `host-name`: String = "",
)