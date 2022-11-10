package ru.hovadur.route.v1.auth.resource

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import ru.hovadur.route.v1.auth.data.UserController
import ru.hovadur.route.v1.auth.data.dto.request.RequestResp
import ru.hovadur.route.v1.auth.data.dto.request.RequestResult
import ru.hovadur.route.v1.auth.data.dto.verify.VerifyResp
import ru.hovadur.route.v1.auth.data.dto.verify.VerifyResult

fun Route.authRoute() {
    val userController: UserController by inject()
    route("/auth") {
        route("/request") {
            post {
                val user = call.receive<RequestResp>()
                when (val result = userController.request(user)) {
                    is RequestResult.Success -> call.respond(HttpStatusCode.NoContent)
                    is RequestResult.BadPhone -> call.respond(HttpStatusCode.BadRequest, "bad phone")
                    is RequestResult.TooManyRequests -> {
                        call.response.header("Retry-After", result.retryAfter)
                        call.respond(HttpStatusCode.TooManyRequests)
                    }

                    is RequestResult.BadRequest -> call.respond(result.status, result.message)
                }
            }
        }
        route("/verify") {
            post {
                val value = call.receive<VerifyResp>()
                when (val result = userController.verify(value)) {
                    is VerifyResult.Success -> call.respond(HttpStatusCode.OK, hashMapOf("token" to result.token))
                    is VerifyResult.BadPhone -> call.respond(HttpStatusCode.BadRequest, "bad phone")
                    is VerifyResult.Error -> call.respond(result.status, result.message)
                }
            }
        }
    }
}