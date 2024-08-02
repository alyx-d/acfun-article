package com.qt.app.util

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.GsonBuilder
import com.google.gson.ToNumberPolicy
import com.qt.app.api.dto.ArticleParamDTO
import com.qt.app.util.Util.toMap
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.reflect.KProperty1

@RequiresApi(Build.VERSION_CODES.O)
fun main() {
    println(ArticleParamDTO().toMap())
}

object Util {
    @RequiresApi(Build.VERSION_CODES.O)
    val dateFormater = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    @RequiresApi(Build.VERSION_CODES.O)
    fun dateFormat(value: Long): String {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneId.of("UTC+8"))
            .format(dateFormater)
    }
    val gson = GsonBuilder()
        .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
        .create()

    fun <T> T.toMap(): Map<String, String> {
        val map = mutableMapOf<String, String>()
        return gson.fromJson(gson.toJson(this), map::class.java)
    }

    // 开启压缩的情况下 无法使用反射
    fun <T> T.ToMap(): Map<String, String> {
        return mutableMapOf<String, String>().apply {
            this@ToMap!!::class.members.filterIsInstance<KProperty1<T, *>>().forEach {
                this[it.name] = it.get(this@ToMap).toString()
            }
        }
    }
}