package com.client

import com.config.FeignConfig
import com.web.response.UserResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(
    name = "user-service",
    configuration = [FeignConfig::class],
)
interface UserServiceClient {
    @GetMapping("/api/user/{username}")
    fun findUserByUsername(
        @PathVariable username: String,
        @RequestHeader("Authorization") token: String,
    ): UserResponse
}
