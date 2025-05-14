package com.web.response

data class UserResponse(
    var username: String,
    var password: String,
    val authorities: String,
)
