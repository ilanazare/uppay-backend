package com.utils

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.security.oauth2.jwt.JwtException
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.stream.Collectors

@Component
class JwtTokenUtil(
    @Autowired
    private val jwtEncoder: JwtEncoder,
    @Autowired
    private val jwtDecoder: JwtDecoder,
) {
    fun generateJwt(auth: Authentication): String {
        val now = Instant.now()
        val scope: String =
            auth.authorities
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "))
        val claims: JwtClaimsSet =
            JwtClaimsSet
                .builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(24, ChronoUnit.HOURS))
                .subject(auth.name)
                .claim("roles", scope)
                .build()
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).tokenValue
    }

    fun getUsernameFromToken(token: String): String =
        try {
            val jwt: Jwt = jwtDecoder.decode(token)
            jwt.subject ?: throw JwtException("Token doesn't contain a subject")
        } catch (ex: JwtException) {
            throw JwtException("Failed to extract username from token: ${ex.message}")
        }

    fun validateToken(
        token: String,
        userDetails: UserDetails,
    ): Boolean =
        try {
            val jwt = jwtDecoder.decode(token)
            val username = jwt.subject
            !isTokenExpired(jwt) && username == userDetails.username
        } catch (ex: JwtException) {
            false
        }

    private fun isTokenExpired(jwt: Jwt): Boolean = jwt.expiresAt?.isBefore(Instant.now()) ?: true
}
