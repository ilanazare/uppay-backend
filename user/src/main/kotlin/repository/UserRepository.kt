package com.repository

import com.domain.entity.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, String> {
    fun save(request: User): User

    fun findUserByUsername(username: String): User?
}
