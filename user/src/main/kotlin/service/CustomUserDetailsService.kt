package com.service

import com.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user =
            userRepository
                .findById(username)
                .orElseThrow { UsernameNotFoundException("User not found with username: $username") }

        val authorities =
            user.authorities
                .split(",")
                .map { SimpleGrantedAuthority(it.trim()) }

        return User(user.username, user.password, authorities)
    }
}
