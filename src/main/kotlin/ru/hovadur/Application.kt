package ru.hovadur

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.http.content.file
import io.ktor.server.http.content.static
import io.ktor.server.http.content.staticRootFolder
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.koin.core.context.startKoin
import ru.hovadur.database.DatabaseFactory
import ru.hovadur.plugins.configureAuthentication
import ru.hovadur.route.v1.auth.data.JwtConfig
import ru.hovadur.route.v1.auth.authRoute
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
    install(ContentNegotiation) {
        json()
    }
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
