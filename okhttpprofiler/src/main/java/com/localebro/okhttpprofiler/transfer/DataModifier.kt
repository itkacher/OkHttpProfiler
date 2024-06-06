package com.localebro.okhttpprofiler.transfer

import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class DataModifier {

    fun modifyResponse(response: Response): Response {
        val requestUrl = response.request.url.toString()
        return customResponse[requestUrl]?.let { newResponse ->
            response.newBuilder().apply {
                body(newResponse.toResponseBody(response.body?.contentType()))
            }.build()
        } ?: response
    }
}

private val customResponse = mutableMapOf(
    ("https://raw.githubusercontent.com/itkacher/OkHttpProfiler/master/colors.json" to "{\"first\": \"Hello\", \"second\": \"it\", \"third\": \"Hanna\"}"),
    ("raw.githubusercontent.com/itkacher/OkHttpProfiler/master/colors.json" to "{\"first\":\"Hi!\"}"),
    ("raw.githubusercontent.com" to "{\"first\":\"Hello\"}"),
)

fun Response.modifyResponse() = customResponse[request.url.toString()]?.let { newResponse ->
    newBuilder().apply {
        body(newResponse.toResponseBody(body?.contentType()))
    }.build()
} ?: this