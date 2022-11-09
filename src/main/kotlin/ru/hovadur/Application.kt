package ru.hovadur

import io.ktor.server.application.Application
import io.ktor.server.http.content.file
import io.ktor.server.http.content.static
import io.ktor.server.http.content.staticRootFolder
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.koin.core.context.startKoin
import ru.hovadur.plugins.configureAuthentication
import ru.hovadur.plugins.configureRouting
import ru.hovadur.plugins.configureSerialization
import ru.hovadur.route.v1.login.authRoute
import java.io.File

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.module() {
    startKoin {
        modules(appModule)
    }
    val jwtConfig = JwtConfig(environment)
    DatabaseFactory.init()
    configureSerialization()
    configureRouting()
    configureAuthentication(jwtConfig)
    routing {
        route("api/v1") {
            authRoute(jwtConfig)
        }
        static(".well-known") {
            staticRootFolder = File("certs")
            file("jwks.json")
        }
    }
}
