package ru.hovadur.route.v1.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
class RegisterResp(val login: String, val password: String, val email: String? = null, val username: String? = null)