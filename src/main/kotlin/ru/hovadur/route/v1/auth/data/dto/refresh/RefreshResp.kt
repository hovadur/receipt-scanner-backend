package ru.hovadur.route.v1.auth.data.dto.refresh

import kotlinx.serialization.Serializable

@Serializable
class RefreshResp(
    val os: String,
    val deviceId: String
)