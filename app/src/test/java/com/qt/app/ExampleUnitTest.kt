package com.qt.app

import org.jsoup.Jsoup
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val html = Jsoup.parseBodyFragment("<div><div>如上</div></div>")
        html.body().allElements.forEach {
            if ((it.nameIs("div") || it.nameIs("p"))) {
                println(it.ownText())
            }
        }
    }
}