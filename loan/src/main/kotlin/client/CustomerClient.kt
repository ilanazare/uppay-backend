package org.example.client

import org.example.config.FeignConfig
import org.example.web.response.CustomerResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(
    name = "customer-service",
    configuration = [FeignConfig::class],
)
interface CustomerClient {
    @GetMapping("/api/customer/{customer}")
    fun findCustomerByCustomer(
        @PathVariable customer: String,
        @RequestHeader("Authorization") token: String,
    ): ResponseEntity<CustomerResponse>
}
