package ru.hovadur.route.v1.auth.data.dto.verify

import kotlinx.serialization.Serializable

@Serializable
class VerifyResp(val phone: String, val code: String)