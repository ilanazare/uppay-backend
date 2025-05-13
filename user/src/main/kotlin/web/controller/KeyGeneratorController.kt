package com.web.controller

import com.dto.KeyPairDto
import com.utils.KeyGeneratorUtility
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/rsa")
class KeyGeneratorController {
    @GetMapping("/key")
    fun getKey(): KeyPairDto {
        val keyPair = KeyGeneratorUtility().generateRSAKey()
        return KeyPairDto.fromKeyPair(keyPair)
    }
}
