package ru.hovadur.data

import io.ktor.server.application.ApplicationEnvironment

class AppConfig {
    var privateKey: String = ""
        internal set
    var puplicKey: String = ""
        internal set
    var issuer: String = ""
        internal set
    var audience: String = ""
        internal set
    var myRealm: String = ""
        internal set
    val irkktHost = "https://irkkt-mobile.nalog.ru:8888"

    fun init(environment: ApplicationEnvironment) {
        privateKey = environment.config.config("jwt").property("privateKey").getString()
        puplicKey = environment.config.config("jwt").property("publicKey").getString()
        issuer = environment.config.config("jwt").property("issuer")
            .getString() + environment.config.config("ktor.deployment").property("port").getString()
        audience = issuer
        myRealm = environment.config.config("jwt").property("realm").getString()
    }
}