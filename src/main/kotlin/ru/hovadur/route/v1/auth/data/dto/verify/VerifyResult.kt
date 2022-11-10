package ru.hovadur.route.v1.auth.data.dto.verify

import io.ktor.http.HttpStatusCode

sealed interface VerifyResult {
    data class Success(val token: String) : VerifyResult
    object BadPhone : VerifyResult
    data class Error(val status: HttpStatusCode, val message: String) : VerifyResult
}