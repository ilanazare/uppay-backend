package com.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Id

data class User(
    @Id
    @Column(name = "username")
    private val username: String,
    internal var password: String,
    val authorities: String,
)
