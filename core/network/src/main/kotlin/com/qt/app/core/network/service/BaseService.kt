package com.qt.app.core.network.service

import jakarta.inject.Qualifier


enum class BaseService(val baseUrl: String) {
    ACFUN("https://www.acfun.cn")
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RetrofitService(val ApiContentType: ApiContentType)

enum class ApiContentType {
    STRING,
    JSON,
}