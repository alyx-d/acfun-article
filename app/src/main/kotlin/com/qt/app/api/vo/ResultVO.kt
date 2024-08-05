package com.qt.app.api.vo

data class ResultVO <T> (
    var cursor: String = "",
    var result: Int = 0,
    var data: T? = null,
    var `host-name`: String = "",
)