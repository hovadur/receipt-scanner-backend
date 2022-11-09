package ru.hovadur.route.v1.model

import kotlinx.serialization.Serializable

@Serializable
class LoginResp(val login: String, val password: String)