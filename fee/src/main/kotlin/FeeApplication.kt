package com

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FeeApplication

fun main(args: Array<String>) {
    runApplication<FeeApplication>(*args)
}
