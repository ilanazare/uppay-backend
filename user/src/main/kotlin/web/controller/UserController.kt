package com.web.controller

import com.domain.entity.User
import com.service.UserService
import com.web.response.UserResponse
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
) {
    @PostMapping("/register")
    fun save(
        @RequestBody request: User,
    ): ResponseEntity<UserResponse> {
        val newUser = userService.save(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser)
    }

    @GetMapping("/{username}")
    fun findUserByUsername(
        @PathVariable username: String,
    ): ResponseEntity<UserResponse?> =
        ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.findUserByUsername(username))

    @PutMapping
    fun update(
        @RequestBody request: User,
    ): ResponseEntity<String?> =
        ResponseEntity
            .status(HttpStatus.CREATED)
            .body(userService.update(request))
}
