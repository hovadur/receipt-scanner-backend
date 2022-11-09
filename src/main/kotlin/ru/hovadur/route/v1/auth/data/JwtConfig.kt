package ru.hovadur.route.v1.auth.data

import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.server.auth.jwt.JWTAuthenticationProvider
import io.ktor.util.hex
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*
import java.util.concurrent.TimeUnit
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class JwtConfig(environment: ApplicationEnvironment) {
    private val privateKeyString = environment.config.config("jwt").property("privateKey").getString()
    private val issuer = environment.config.config("jwt").property("issuer").getString()
    private val audience = environment.config.config("jwt").property("audience").getString()
    private val myRealm = environment.config.config("jwt").property("realm").getString()
    private val hashKey = hex(environment.config.config("jwt").property("secretKey").getString())
    private val hmacKey = SecretKeySpec(hashKey, "HmacSHA1")

    fun buildJwtVerifier(config: JWTAuthenticationProvider.Config) {
        config.verifier(getJwkProvider(), issuer) {
            acceptLeeway(3)
        }
    }

    fun getRealm(): String = myRealm
    fun getToken(login: String): String {
        val publicKey = getJwkProvider().get("nsHJ5w24QTJbMUNuTa6D3-31yb1-gzQ0hhgJzcE6WHA").publicKey
        val keySpecPKCS8 = PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString))
        val privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpecPKCS8)
        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("login", login)
            .withExpiresAt(Date(System.currentTimeMillis() + 60000))
            .sign(Algorithm.RSA256(publicKey as RSAPublicKey, privateKey as RSAPrivateKey))
    }

    fun hash(password: String): String {
        val hmac = Mac.getInstance("HmacSHA1")
        hmac.init(hmacKey)
        return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
    }

    private fun getJwkProvider() = JwkProviderBuilder(issuer)
        .cached(10, 24, TimeUnit.HOURS)
        .rateLimited(10, 1, TimeUnit.MINUTES)
        .build()
}