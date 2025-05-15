package org.example.repository

import org.example.domain.entity.Customer
import org.springframework.data.repository.CrudRepository

interface CustomerRepository : CrudRepository<Customer, String> {
    fun save(request: Customer): Customer

    fun findCustomerByCustomer(customer: String): Customer?
}
