package ru.hovadur.route.v1.auth.domain.model

class User(
    val phone: String,
    val email: String?,
    val name: String?,
    val irkktSessionId: String?,
    val irkktRefreshToken: String?
)