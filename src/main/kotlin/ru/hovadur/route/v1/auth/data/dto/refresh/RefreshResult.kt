package ru.hovadur.route.v1.auth.data.dto.refresh

import io.ktor.http.HttpStatusCode

sealed interface RefreshResult {
    object Success : RefreshResult
    object BadUser : RefreshResult
    data class Error(val status: HttpStatusCode, val message: String) : RefreshResult
}