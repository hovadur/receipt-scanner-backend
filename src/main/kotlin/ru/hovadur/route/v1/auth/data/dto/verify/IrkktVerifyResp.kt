package ru.hovadur.route.v1.auth.data.dto.verify

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class IrkktVerifyResp(
    val sessionId: String = "",
    @SerialName("refresh_token")
    val refreshToken: String = "",
    val phone: String = "",
    val name: String = "",
    val email: String = ""
)