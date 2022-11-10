package ru.hovadur

import org.koin.dsl.module
import ru.hovadur.data.AppConfig
import ru.hovadur.route.v1.auth.data.JwtConfig
import ru.hovadur.route.v1.auth.data.UserController

val appModule = module {
    single { AppConfig() }
    single { JwtConfig(get()) }
    factory { UserController(get()) }
}