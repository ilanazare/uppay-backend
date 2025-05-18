package org.example.web.controller

import org.example.domain.enums.TableEnum
import org.example.exceptions.CustomerNotFoundException
import org.example.services.LoanService
import org.example.web.LoanResponse
import org.example.web.request.LoanRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/loan")
class LoanController(
    private val loanService: LoanService,
) {
    @PostMapping("/{tableNumber}")
    fun savePurchase(
        @PathVariable tableNumber: TableEnum,
        @RequestBody request: LoanRequest,
    ): ResponseEntity<Any> =
        try {
            loanService.saveLoan(tableNumber, request)
            ResponseEntity.status(HttpStatus.CREATED).build()
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        }

    @GetMapping("/{customer}")
    fun findListAmountByCustomer(
        @PathVariable customer: String,
    ): ResponseEntity<List<LoanResponse>> =
        try {
            val loans = loanService.findListAmountByCustomer(customer)
            ResponseEntity.ok(loans)
        } catch (e: CustomerNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
}
