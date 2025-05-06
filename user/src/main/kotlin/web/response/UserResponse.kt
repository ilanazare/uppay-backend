package com.web.response

import com.domain.entity.User

data class UserResponse(
    var username: String,
)

fun User.toUserResponse() =
    UserResponse(
        username = this.username,
    )
