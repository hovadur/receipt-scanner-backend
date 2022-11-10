package ru.hovadur

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import ru.hovadur.data.AppConfig
import ru.hovadur.route.v1.auth.data.JwtConfig
import ru.hovadur.route.v1.auth.data.UserController

val appModule = module() {
    single { AppConfig() }
    single {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                })
            }
            install(Logging) {
                level = LogLevel.ALL
            }
        }
    }
    single { JwtConfig(get()) }
    factory { UserController(get(), get(), get()) }
}