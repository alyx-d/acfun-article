package com.qt.app.core.ui.common

import androidx.compose.ui.graphics.Color

fun usernameColor(nameColor: Int) = when (nameColor) {
    1 -> Color.Red
    2 -> Color(0xFF984ffd)
    else -> Color.Unspecified
}