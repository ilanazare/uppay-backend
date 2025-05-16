package org.example.web.request

import org.example.domain.enums.CreditCardFlagEnum

data class LoanRequest(
    val customer: String,
    val flag: CreditCardFlagEnum,
    val numberOfInstallments: Int,
    val purchaseValue: Double,
)
