package org.example.client

import org.example.config.FeignConfig
import org.example.domain.enums.CreditCardFlagEnum
import org.example.domain.enums.TableEnum
import org.example.web.response.FeeResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    name = "fee-service",
    configuration = [FeignConfig::class],
)
interface FeeClient {
    @GetMapping("/api/fees")
    fun findCardFeeByNumberOfInstallmentsAndFlag(
        @RequestParam numberTable: TableEnum,
        @RequestParam numberOfInstallments: Int,
        @RequestParam flag: CreditCardFlagEnum,
    ): ResponseEntity<FeeResponse>
}
