package com.config

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import com.utils.KeyGeneratorUtility
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.security.KeyPair
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val userDetailsService: UserDetailsService,
) {
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    val keys: KeyPair = KeyGeneratorUtility().generateRSAKey()

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it
                    .requestMatchers("/api/auth/login")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            }.sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }.oauth2ResourceServer { oauth2 ->
                oauth2
                    .jwt { jwt ->
                        jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                    }
            }
        return http.build()
    }

    @Bean
    fun authenticationManager(): AuthenticationManager {
        val daoAuthenticationProvider = DaoAuthenticationProvider()
        daoAuthenticationProvider.run {
            setUserDetailsService(userDetailsService)
            setPasswordEncoder(passwordEncoder())
        }
        return ProviderManager(daoAuthenticationProvider)
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins
        configuration.allowedMethods
        configuration.allowCredentials
        configuration.allowedHeaders
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Bean
    fun jwtDecoder(): JwtDecoder = NimbusJwtDecoder.withPublicKey(keys.public as RSAPublicKey).build()

    @Bean
    fun jwtEncoder(): JwtEncoder {
        val jwk = RSAKey.Builder(keys.public as RSAPublicKey).privateKey(keys.private as RSAPrivateKey).build()
        val jwks: JWKSource<SecurityContext> = ImmutableJWKSet(JWKSet(jwk))
        return NimbusJwtEncoder(jwks)
    }

    @Bean
    fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        val jwtGrantedAuthoritiesConverter = JwtGrantedAuthoritiesConverter()
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles")
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_")
        val jwtConverter = JwtAuthenticationConverter()
        jwtConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter)
        return jwtConverter
    }
}
