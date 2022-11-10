package ru.hovadur.route.v1.auth.data.dto.verify

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.hovadur.common.Constants

@Serializable
class IrkktVerifyReq(
    val phone: String,
    @SerialName("client_secret")
    val clientSecret: String = Constants.clientSecret,
    val os: String = Constants.android,
    val code: String
)