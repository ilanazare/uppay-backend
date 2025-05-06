package com.service

import com.domain.entity.User
import com.exceptions.UserDoesExistException
import com.repository.UserRepository
import com.web.response.UserResponse
import com.web.response.toUserResponse
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
}
