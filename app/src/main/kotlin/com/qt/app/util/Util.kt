package com.qt.app.util

import android.content.Context
import android.os.Build.VERSION.SDK_INT
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.google.gson.GsonBuilder
import com.google.gson.ToNumberPolicy
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.reflect.KProperty1

fun main() {
    val s = "[img=图片]垃圾[/img]123[emot=acfun,1111/]"
    val rex = Regex(pattern = "\\[img=图片].+\\[/img]|\\[emot=acfun,\\d+/]")
    rex.findAll(s).forEach { println(it.value) }
//    println(rex.replace(s, "-=-=-"))
}

object Util {
    private val dateFormater = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    fun imageLoader(context: Context) = ImageLoader.Builder(context)
        .components { add(if (SDK_INT >= 28) ImageDecoderDecoder.Factory() else GifDecoder.Factory()) }
        .build()

    fun dateFormat(value: Long): String {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneId.of("UTC+8"))
            .format(dateFormater)
    }
    val gson = GsonBuilder()
        .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
        .create()

    // 备用
    private fun <T> T.ToMap(): Map<String, String> {
        val map = mutableMapOf<String, String>()
        return gson.fromJson(gson.toJson(this), map::class.java)
    }

    fun <T> T.toMap(): Map<String, String> {
        return mutableMapOf<String, String>().apply {
            this@toMap!!::class.members.filterIsInstance<KProperty1<T, *>>().forEach {
                this[it.name] = it.get(this@toMap).toString()
            }
        }
    }
}