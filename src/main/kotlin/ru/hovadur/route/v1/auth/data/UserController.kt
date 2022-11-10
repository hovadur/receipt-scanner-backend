package ru.hovadur.route.v1.auth.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import ru.hovadur.data.AppConfig
import ru.hovadur.database.dao.UsersDao
import ru.hovadur.route.v1.auth.data.dto.request.IrkktRequest
import ru.hovadur.route.v1.auth.data.dto.request.RequestResp
import ru.hovadur.route.v1.auth.data.dto.request.RequestResult
import ru.hovadur.route.v1.auth.data.dto.verify.IrkktVerifyReq
import ru.hovadur.route.v1.auth.data.dto.verify.IrkktVerifyResp
import ru.hovadur.route.v1.auth.data.dto.verify.VerifyResp
import ru.hovadur.route.v1.auth.data.dto.verify.VerifyResult
import ru.hovadur.route.v1.auth.domain.model.User

class UserController(
    private val jwtConfig: JwtConfig, private val client: HttpClient, private val appConfig: AppConfig
) {
    suspend fun request(value: RequestResp): RequestResult {
        val phone = value.phone.onlyDigitPhonePlus7() ?: return RequestResult.BadPhone
        val irkktHost = appConfig.irkktHost
        val response = client.post("$irkktHost/v2/auth/phone/request") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Any)
            setBody(IrkktRequest(phone))
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> RequestResult.Success
            HttpStatusCode.TooManyRequests -> RequestResult.TooManyRequests(response.headers["Retry-After"].orEmpty())
            else -> RequestResult.BadRequest(response.status, response.bodyAsText())
        }
    }

    suspend fun verify(value: VerifyResp): VerifyResult {
        val phone = value.phone.onlyDigitPhonePlus7() ?: return VerifyResult.BadPhone
        val irkktHost = appConfig.irkktHost
        val response = client.post("$irkktHost/v2/auth/phone/verify") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Any)
            setBody(IrkktVerifyReq(phone = phone, code = value.code))
        }
        return if (response.status == HttpStatusCode.OK) {
            val resp = response.body<IrkktVerifyResp>()
            val user = UsersDao.fetch(phone)
            val token = jwtConfig.getToken(phone)
            if (user == null) {
                UsersDao.insert(
                    User(
                        phone = resp.phone,
                        email = resp.email,
                        name = resp.name,
                        irkktSessionId = resp.sessionId,
                        irkktRefreshToken = resp.refreshToken,
                    )
                )
                VerifyResult.Success(token)
            } else {
                UsersDao.update(phone, resp.sessionId, resp.refreshToken)
                VerifyResult.Success(token)
            }
        } else VerifyResult.Error(response.status, response.bodyAsText())
    }

    private fun String?.onlyDigitPhonePlus7(): String? {
        fun stripNonDigits(input: CharSequence): String? {
            val sb = StringBuilder(input.length)
            for (element in input) {
                if (element.code in 48..57) {
                    sb.append(element)
                }
            }
            return if (sb.isEmpty()) null else sb.toString()
        }

        this ?: return null
        val parts = this.split("[;,]")
        if (parts.isEmpty()) return null
        var phone = stripNonDigits(parts[0])
        phone ?: return null
        var len = phone.length
        if (len < 1) return null
        var firstChar: Char? = phone[0]
        if ((firstChar == '8' || firstChar == '7') && len == 11) {
            firstChar = phone[1]
            phone = phone.substring(1)
            len = 10
        }
        return if ((firstChar == '3' || firstChar == '4' || firstChar == '8' || firstChar == '9') && len == 10) {
            "+7$phone"
        } else null
    }
}