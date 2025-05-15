package org.example.web.request

import org.example.domain.entity.Customer

data class CustomerRequest(
    val customer: String,
    val email: String,
)

fun CustomerRequest.toCustomer() =
    Customer(
        customer = this.customer,
        email = this.email,
    )
