package ru.hovadur.route.v1.login

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import ru.hovadur.JwtConfig
import ru.hovadur.route.v1.model.LoginResp
import ru.hovadur.route.v1.model.RegisterResp
import ru.hovadur.route.v1.model.RegisterResult

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