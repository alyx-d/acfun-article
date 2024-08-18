package com.qt.app.feature.article.util

import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.widget.Toast
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.reflect.KProperty1

fun main() {
    val s = "[img=图片]134[/img]123[emot=acfun,1111/]"
    val rex = Regex(pattern = "\\[img=图片].+\\[/img]|\\[emot=acfun,\\d+/]")
    rex.findAll(s).forEach { println(it.value) }
//    println(rex.replace(s, "-=-=-"))
}

object Util {
    private val dateFormater = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    fun showToast(msg: String, context: Context) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    private var _imageLoader: ImageLoader? = null
    fun imageLoader(context: Context): ImageLoader = _imageLoader
        ?: run {
            _imageLoader = ImageLoader.Builder(context)
                .components {
                    if (SDK_INT >= 28) {
                        add(ImageDecoderDecoder.Factory())
                    } else {
                        add(GifDecoder.Factory())
                    }
                }
                .crossfade(true)
                .build()
            _imageLoader!!
        }

    fun dateFormat(value: Long): String {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneId.of("UTC+8"))
            .format(dateFormater)
    }

    fun <T> T.toMap(): Map<String, String> {
        return mutableMapOf<String, String>().apply {
            this@toMap!!::class.members.filterIsInstance<KProperty1<T, *>>().forEach {
                this[it.name] = it.get(this@toMap).toString()
            }
        }
    }
}