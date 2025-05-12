package com.utils

import org.springframework.stereotype.Component
import java.security.KeyPair
import java.security.KeyPairGenerator

@Component
class KeyGeneratorUtility {
    fun generateRSAKey(): KeyPair {
        try {
            val keyPairGenerator: KeyPairGenerator = KeyPairGenerator.getInstance("RSA")
            keyPairGenerator.initialize(2048)
            val keyPair = keyPairGenerator.generateKeyPair()
            return keyPair
        } catch (e: Exception) {
            throw IllegalStateException()
        }
    }
}
