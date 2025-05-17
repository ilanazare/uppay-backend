package com.web.controller

import com.utils.JwtTokenUtil
import com.web.request.LoginRequest
import com.web.response.LoginResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenUtil: JwtTokenUtil,
) {
    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest,
    ): ResponseEntity<LoginResponse> {
        val auth: Authentication =
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(request.username, request.password),
            )
        val token = jwtTokenUtil.generateJwt(auth)
        return ResponseEntity.ok(LoginResponse(token))
    }
}
