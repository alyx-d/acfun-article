package com.qt.app.core.network.service

import javax.inject.Qualifier


enum class BaseService(val baseUrl: String) {
    ACFUN("https://www.acfun.cn")
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RetrofitService(val apiContentType: ApiContentType)

enum class ApiContentType {
    STRING,
    JSON,
}