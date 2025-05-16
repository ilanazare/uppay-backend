package com.web.controller

import com.domain.enums.CreditCardFlagEnum
import com.domain.enums.TableEnum
import com.exceptions.FeeDoesExistException
import com.services.FeeService
import com.web.request.RequestFee
import com.web.request.RequestFeeUpdate
import com.web.response.FeeResponse
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
    fun createFee(
        @RequestBody request: RequestFee,
    ): ResponseEntity<Any> =
        try {
            feeService.saveFee(request)
            ResponseEntity.status(HttpStatus.CREATED).build()
        } catch (e: FeeDoesExistException) {
            ResponseEntity.status(HttpStatus.CONFLICT).body(e.message)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request")
        }

    @PutMapping
    fun updateFee(
        @RequestBody request: RequestFeeUpdate,
    ): ResponseEntity<String> =
        try {
            feeService.updateFee(request)
            ResponseEntity("Fee updated successfully", HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        }

    @GetMapping
    fun findCardFeeByNumberOfInstallmentsAndFlag(
        @RequestParam numberTable: TableEnum,
        @RequestParam numberOfInstallments: Int,
        @RequestParam flag: CreditCardFlagEnum,
    ): ResponseEntity<FeeResponse> =
        try {
            val feeResponse =
                feeService.findCardFeeByNumberOfInstallmentsAndFlag(
                    numberTable,
                    numberOfInstallments,
                    flag,
                )
            ResponseEntity(feeResponse, HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
}
