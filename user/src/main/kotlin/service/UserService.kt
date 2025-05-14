package com.service

import com.domain.entity.User
import com.exceptions.UserDoesExistException
import com.repository.UserRepository
import com.web.response.UserResponse
import com.web.response.toUserResponse
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    fun save(request: User): UserResponse? {
        val passwordEncoder = passwordEncoder.encode(request.password)
        request.password = passwordEncoder
        val user = userRepository.findUserByUsername(request.username)
        return userRepository.save(request).toUserResponse().takeIf { user == null }
            ?: throw UserDoesExistException("User does exist in database")
    }

    fun findUserByUsername(username: String): UserResponse? {
        val userResponse = userRepository.findUserByUsername(username)
        return userResponse?.toUserResponse().takeIf {
            userResponse?.username == username
        }
            ?: throw UsernameNotFoundException("Username doesn't exist in the database")
    }

    fun update(request: User): String? {
        val username = userRepository.findUserByUsername(request.username)?.username
        val passwordEncoder = passwordEncoder.encode(request.password)
        request.password = passwordEncoder
        userRepository
            .save(request)
            .takeIf { username == request.username }
            ?: throw UsernameNotFoundException("username not found for user")
        return username
    }
}
