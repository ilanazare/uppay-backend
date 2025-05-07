package com.client

import com.config.FeignConfig
import com.web.request.LoginRequest
import com.web.response.LoginResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(
    name = "customerClient",
    url = "https://localhost:8080",
    configuration = [FeignConfig::class],
)
interface UserClient {
    @PostMapping("/api/auth/login")
    fun login(
        @RequestBody request: LoginRequest,
    ): LoginResponse
}
