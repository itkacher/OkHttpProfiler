package io.nerdythings.modifier.data

internal data class CustomResponse(
    val id: String,
    val url: String,
    val response: String,
    val code: Int,
    val isEnabled: Boolean,
)
