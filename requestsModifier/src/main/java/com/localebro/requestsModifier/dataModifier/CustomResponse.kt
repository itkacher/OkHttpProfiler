package com.localebro.requestsModifier.dataModifier

internal data class CustomResponse(
    val id: String,
    val url: String,
    val response: String,
    val code: Int,
    val isEnabled: Boolean,
)
