package com.web.controller

import com.domain.entity.Fee
import com.domain.enums.CreditCardFlagEnum
import com.domain.enums.TableEnum
import com.exceptions.FeeDoesExistException
import com.exceptions.FeeNotFoundException
import com.services.FeeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/fees")
class FeeController(
    private val feeService: FeeService,
) {
    @PostMapping
    fun saveFee(
        @RequestBody fee: Fee,
    ): ResponseEntity<Any> =
        try {
            feeService.saveFee(fee)
            ResponseEntity.status(HttpStatus.CREATED).build()
        } catch (e: FeeDoesExistException) {
            ResponseEntity.status(HttpStatus.CONFLICT).body(e.message)
        }

    @PutMapping
    fun updateFee(
        @RequestBody fee: Fee,
    ): ResponseEntity<Any> =
        try {
            feeService.updateFee(fee)
            ResponseEntity.ok().build()
        } catch (e: FeeNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        }

    @GetMapping
    fun findCardFee(
        @RequestParam numberTable: TableEnum,
        @RequestParam numberOfInstallments: Int,
        @RequestParam flag: CreditCardFlagEnum,
    ): ResponseEntity<Any> =
        try {
            val fee =
                feeService.findCardFeeByNumberOfInstallmentsAndFlag(
                    numberTable,
                    numberOfInstallments,
                    flag,
                )
            fee?.let {
                ResponseEntity.ok(it)
            } ?: ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fee not found")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)
        }
}
