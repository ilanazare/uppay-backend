package org.example.web.response

import org.example.domain.enums.CreditCardFlagEnum
import org.example.domain.enums.TableEnum

data class FeeResponse(
    val id: Long,
    val numberTable: TableEnum,
    val numberOfInstallments: Int,
    val flag: CreditCardFlagEnum,
    val machineFee: Double,
    val clientFee: Double,
)
