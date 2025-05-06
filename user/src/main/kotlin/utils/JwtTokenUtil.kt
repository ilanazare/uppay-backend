package com.utils

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtTokenUtil(
    @Value("\${jwt.secret}") private val secret: String,
    @Value("\${jwt.expiration}") private val expiration: Long,
) {
    private val key: SecretKey = Keys.hmacShaKeyFor(secret.toByteArray())

    fun generateToken(userDetails: UserDetails): String {
        val claims =
            mutableMapOf<String, Any>(
                "authorities" to userDetails.authorities.joinToString(",") { it.authority },
            )

        return Jwts
            .builder()
            .claims(claims)
            .subject(userDetails.username)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + expiration * 1000))
            .signWith(key, Jwts.SIG.HS256)
            .compact()
    }

    fun validateToken(
        token: String,
        userDetails: UserDetails,
    ): Boolean {
        val username = getUsernameFromToken(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    fun getUsernameFromToken(token: String): String =
        Jwts
            .parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
            .subject

    private fun isTokenExpired(token: String): Boolean {
        val expirationDate =
            Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
                .expiration
        return expirationDate.before(Date())
    }
}
