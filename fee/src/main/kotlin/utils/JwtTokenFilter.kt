package com.utils

import io.jsonwebtoken.Jwts
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtTokenFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val keys by lazy { KeyGeneratorUtility().generateRSAKey() }

        val authHeader = request.getHeader("Authorization")
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring(7)
            try {
                val claims =
                    Jwts
                        .parser()
                        .verifyWith(keys.public)
                        .build()
                        .parseSignedClaims(token)
                        .payload

                val username = claims.subject
                val authorities =
                    claims["authorities"]
                        .toString()
                        .split(",")
                        .map { it.trim() }
                        .map { SimpleGrantedAuthority(it) }

                if (username != null) {
                    val authentication =
                        UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            authorities,
                        )
                    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authentication
                }
            } catch (ex: Exception) {
                SecurityContextHolder.clearContext()
            }
        }
        filterChain.doFilter(request, response)
    }
}
