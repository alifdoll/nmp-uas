package com.ubaya.protectcare23.api

import android.content.Context
import android.net.Uri
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class VolleyService private constructor(
    context: Context
): HttpService() {

    companion object {
        @Volatile
        private var instance: VolleyService? = null

        fun getInstance(context: Context): VolleyService =
            instance ?: synchronized(this) {
                instance ?: VolleyService(context).apply {
                    instance = this
                }
            }
    }

    private val queue = Volley.newRequestQueue(context)

    private suspend fun request (
        method: Int,
        url: String,
        params: Map<String, Any?> = emptyMap(),
        body: Map<String, Any?> = emptyMap()
    ) = suspendCoroutine<Response> { continuation ->
        val urlBuild = Uri.parse(url)
            .buildUpon()
            .apply {
                params.forEach {
                    appendQueryParameter(it.key, it.value.toString())
                }
            }
            .build()
            .toString()

        val request = object : StringRequest(method, urlBuild, null, null) {
            override fun getParams() = body.mapValues { it.value.toString() }

            override fun parseNetworkResponse(response: NetworkResponse?) =
                super.parseNetworkResponse(response).also {
                    continuation.resume(
                        Response(
                            response!!.statusCode,
                            if (it.result!!.isEmpty()) JSONObject()
                            else JSONObject(it.result!!)
                        )
                    )
                }

            override fun parseNetworkError(volleyError: VolleyError?) =
                super.parseNetworkError(volleyError).also {
                    when (volleyError) {
                        is ServerError -> parseNetworkResponse(volleyError.networkResponse)
                        else -> continuation.resumeWithException(volleyError!!)
                    }
                }
        }

        queue.add(request)
    }



    override suspend fun get(
        url: String,
        params: Map<String, Any?>
    ) = request(Request.Method.GET, url, params)

    override suspend fun post(
        url: String,
        params: Map<String, Any?>,
        body: Map<String, Any?>
    ) = request(Request.Method.POST, url, params, body)
}