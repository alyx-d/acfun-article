package com.qt.app.feature.article.ui.common

import androidx.compose.ui.graphics.Color

internal fun usernameColor(nameColor: Int) = when(nameColor) {
    1 -> Color.Red
    2 -> Color(0xFF984ffd)
    else -> Color.Unspecified
}