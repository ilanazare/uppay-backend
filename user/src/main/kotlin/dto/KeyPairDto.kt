package com.dto

import java.util.Base64

data class KeyPairDto(
    val publicKey: String,
    val privateKey: String,
) {
    companion object {
        fun fromKeyPair(keyPair: java.security.KeyPair): KeyPairDto {
            val publicKey = Base64.getEncoder().encodeToString(keyPair.public.encoded)
            val privateKey = Base64.getEncoder().encodeToString(keyPair.private.encoded)
            return KeyPairDto(publicKey, privateKey)
        }
    }
}
