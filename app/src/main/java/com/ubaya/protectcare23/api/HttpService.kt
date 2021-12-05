package com.ubaya.protectcare23.api

abstract class HttpService {
    abstract suspend fun get(
        url: String,
        params: Map<String, Any?> = emptyMap()
    ): Response

    abstract suspend fun post(
        url: String,
        params: Map<String, Any?> = emptyMap(),
        body: Map<String, Any?> = emptyMap()
    ): Response
}