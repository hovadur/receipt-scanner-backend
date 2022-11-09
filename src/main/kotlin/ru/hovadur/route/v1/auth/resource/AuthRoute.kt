package ru.hovadur.route.v1.auth

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import ru.hovadur.route.v1.auth.data.JwtConfig
import ru.hovadur.route.v1.auth.data.UserController
import ru.hovadur.route.v1.auth.data.dto.LoginResp
import ru.hovadur.route.v1.auth.data.dto.RegisterResp
import ru.hovadur.route.v1.auth.data.dto.RegisterResult

fun Route.authRoute(jwtConfig: JwtConfig) {
    val userController = UserController(jwtConfig)
    route("/register") {
        post {
            val register = call.receive<RegisterResp>()
            val result = userController.insert(register)
            if (result == RegisterResult.Duplicate) {
                call.respond(HttpStatusCode.BadRequest, "login already exists")
            } else {
                call.respond(HttpStatusCode.Created)
            }
        }
    }
    route("/login") {
        post {
            val user = call.receive<LoginResp>()
            if (userController.isValid(user)) {
                call.respond(hashMapOf("token" to jwtConfig.getToken(user.login)))
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}