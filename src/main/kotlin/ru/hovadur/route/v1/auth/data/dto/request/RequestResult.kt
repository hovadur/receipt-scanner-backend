package ru.hovadur.route.v1.auth.data.dto.request

import io.ktor.http.HttpStatusCode

sealed interface RequestResult {
    object Success : RequestResult
    object BadPhone : RequestResult
    data class TooManyRequests(val retryAfter: String) : RequestResult
    data class BadRequest(val status: HttpStatusCode, val message: String) : RequestResult
}