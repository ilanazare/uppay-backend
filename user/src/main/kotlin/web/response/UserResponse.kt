package com.web.response

import com.domain.entity.User

data class UserResponse(
    var username: String,
    var password: String,
    val authorities: String,
)

fun User.toUserResponse() =
    UserResponse(
        username = this.username,
        password = this.password,
        authorities = this.authorities,
    )
