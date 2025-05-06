package com.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.Collections

@Entity
@Table(name = "users")
data class User(
    @Id
    @Column(name = "username")
    internal val username: String,
    internal var password: String,
    val authorities: String,
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        Collections.singleton(
            SimpleGrantedAuthority(authorities),
        )

    override fun getPassword(): String = password

    override fun getUsername(): String = username
}
