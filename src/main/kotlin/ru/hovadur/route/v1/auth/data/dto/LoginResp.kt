package ru.hovadur.route.v1.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
class LoginResp(val login: String, val password: String)