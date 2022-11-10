package ru.hovadur.route.v1.auth.data

import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.auth.jwt.JWTAuthenticationProvider
import ru.hovadur.data.AppConfig
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*
import java.util.concurrent.TimeUnit

class JwtConfig(private val appConfig: AppConfig) {
    private val privateKey = appConfig.privateKey
    private val issuer = appConfig.issuer
    private val audience = appConfig.audience
    private val myRealm = appConfig.myRealm

    fun buildJwtVerifier(config: JWTAuthenticationProvider.Config) {
        config.verifier(getJwkProvider(), issuer) {
            acceptLeeway(3)
        }
    }

    fun getRealm(): String = myRealm
    fun getToken(login: String): String {
        val publicKey = getJwkProvider().get(appConfig.puplicKey).publicKey
        val keySpecPKCS8 = PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey))
        val privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpecPKCS8)
        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("login", login)
            .withExpiresAt(Date(System.currentTimeMillis() + 24 * 60 * 60000))
            .sign(Algorithm.RSA256(publicKey as RSAPublicKey, privateKey as RSAPrivateKey))
    }

    private fun getJwkProvider() = JwkProviderBuilder(issuer)
        .cached(10, 24, TimeUnit.HOURS)
        .rateLimited(10, 1, TimeUnit.MINUTES)
        .build()
}