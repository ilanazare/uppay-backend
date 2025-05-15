package org.example.services

import org.example.exceptions.CustomerDoesExistException
import org.example.exceptions.CustomerNotFoundException
import org.example.repository.CustomerRepository
import org.example.web.request.CustomerRequest
import org.example.web.request.toCustomer
import org.example.web.response.CustomerResponse
import org.example.web.response.toCustomerResponse
import org.springframework.stereotype.Service

@Service
class CustomerService(
    val customerRepository: CustomerRepository,
) {
    fun saveCustomer(request: CustomerRequest) {
        val customer = customerRepository.findCustomerByCustomer(request.customer)
        if (customer == null) {
            customerRepository
                .save(request.toCustomer())
        } else {
            throw CustomerDoesExistException("Customer does exist in the database")
        }
    }

    fun updateFee(request: CustomerRequest) {
        val customer = customerRepository.findCustomerByCustomer(request.customer)?.customer
        customerRepository
            .save(request.toCustomer())
            .takeIf { request.customer == customer }
            ?: throw CustomerNotFoundException("Customer not found in the database")
    }

    fun findCardFeeByNumberOfInstallmentsAndFlag(customer: String): CustomerResponse =
        customerRepository
            .findCustomerByCustomer(customer)
            ?.toCustomerResponse() ?: throw CustomerNotFoundException("Customer not found in the database")
}
