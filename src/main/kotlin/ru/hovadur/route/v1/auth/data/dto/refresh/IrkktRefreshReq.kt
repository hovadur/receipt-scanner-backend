package ru.hovadur.route.v1.auth.data.dto.refresh

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.hovadur.common.Constants

@Serializable
class IrkktRefreshReq(
    @SerialName("refresh_token")
    val refreshToken: String,
    @SerialName("client_secret")
    val clientSecret: String = Constants.clientSecret,
)