package com.web.controller

import com.nimbusds.jose.jwk.JWKMatcher
import com.nimbusds.jose.jwk.JWKSelector
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class JwkSetController(
    private val jwkSource: JWKSource<SecurityContext>,
) {
    @GetMapping("/.well-known/jwks.json")
    fun getKeys(): Map<String, Any> {
        val matcher = JWKMatcher.Builder().build()
        val jwkSet = JWKSet(jwkSource.get(JWKSelector(matcher), null))
        return jwkSet.toJSONObject()
    }
}
