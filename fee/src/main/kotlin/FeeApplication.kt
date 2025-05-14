package com

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class FeeApplication

fun main(args: Array<String>) {
    runApplication<FeeApplication>(*args)
}
