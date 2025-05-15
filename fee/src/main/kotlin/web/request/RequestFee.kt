package com.web.request

import com.domain.entity.Fee
import com.domain.enums.CreditCardFlagEnum
import com.domain.enums.TableEnum

data class RequestFee(
    val numberTable: TableEnum,
    val numberOfInstallments: Int,
    val flag: CreditCardFlagEnum,
    val machineFee: Double,
    val clientFee: Double,
)

fun RequestFee.toFeeFromRequestFee() =
    Fee(
        id = 0,
        numberTable = this.numberTable,
        numberOfInstallments = this.numberOfInstallments,
        flag = this.flag,
        machineFee = this.machineFee,
        clientFee = this.clientFee,
    )
