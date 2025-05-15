package org.example.web.response

import org.example.domain.entity.Customer

data class CustomerResponse(
    val customer: String,
    val email: String,
)

fun Customer.toCustomerResponse() =
    CustomerResponse(
        customer = this.customer,
        email = this.email,
    )
