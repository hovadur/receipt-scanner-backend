package ru.hovadur.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.response.respond
import org.koin.ktor.ext.get
import ru.hovadur.route.v1.auth.data.JwtConfig

fun Application.configureAuthentication() {
    val jwtConfig: JwtConfig = get()
    install(Authentication) {
        jwt("auth-jwt") {
            realm = jwtConfig.getRealm()
            jwtConfig.buildJwtVerifier(this)
            validate { credential ->
                if (credential.payload.getClaim("login").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}
