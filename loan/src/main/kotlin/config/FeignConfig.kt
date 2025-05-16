package org.example.config

import feign.Logger
import feign.RequestInterceptor
import feign.codec.Encoder
import feign.codec.ErrorDecoder
import feign.form.spring.SpringFormEncoder
import org.example.exceptions.FeignErrorDecoder
import org.springframework.beans.factory.ObjectFactory
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.cloud.openfeign.support.SpringEncoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.client.RestTemplate

@Configuration
class FeignConfig {
    @Bean
    fun feignLoggerLeve(): Logger.Level = Logger.Level.FULL

    @Bean
    fun errorDecoder(): ErrorDecoder = FeignErrorDecoder()

    @Bean
    fun restTemplate(): RestTemplate = RestTemplate()

    @Bean
    fun requestInterceptor(): RequestInterceptor =
        RequestInterceptor { template ->
            val authentication = SecurityContextHolder.getContext().authentication
            if (authentication != null && authentication is JwtAuthenticationToken) {
                val tokenValue = authentication.token.tokenValue
                template.header("Authorization", "Bearer $tokenValue")
            }
        }

    @Bean
    fun encoder(messageConverters: ObjectFactory<HttpMessageConverters>): Encoder = SpringFormEncoder(SpringEncoder(messageConverters))
}
