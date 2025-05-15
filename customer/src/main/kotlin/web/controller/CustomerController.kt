package org.example.web.controller

import org.example.exceptions.CustomerDoesExistException
import org.example.exceptions.CustomerNotFoundException
import org.example.services.CustomerService
import org.example.web.request.CustomerRequest
import org.example.web.response.CustomerResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/customer")
class CustomerController(
    private val customerService: CustomerService,
) {
    @PostMapping
    fun saveCustomer(
        @RequestBody request: CustomerRequest,
    ): ResponseEntity<String> =
        try {
            customerService.saveCustomer(request)
            ResponseEntity.ok("Customer saved successfully")
        } catch (e: CustomerDoesExistException) {
            ResponseEntity.status(HttpStatus.CONFLICT).body(e.message)
        }

    @PutMapping
    fun updateFee(
        @RequestBody request: CustomerRequest,
    ): ResponseEntity<String> =
        try {
            customerService.updateFee(request)
            ResponseEntity.ok("Customer fee updated successfully")
        } catch (e: CustomerNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        }

    @GetMapping("/{customer}")
    fun findCardFeeByNumberOfInstallmentsAndFlag(
        @PathVariable customer: String,
    ): ResponseEntity<CustomerResponse> =
        try {
            val response = customerService.findCardFeeByNumberOfInstallmentsAndFlag(customer)
            ResponseEntity.ok(response)
        } catch (e: CustomerNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
}
