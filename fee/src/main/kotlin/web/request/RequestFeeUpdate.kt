package com.web.request

import com.domain.entity.Fee
import com.domain.enums.CreditCardFlagEnum
import com.domain.enums.TableEnum

data class RequestFeeUpdate(
    val id: Long,
    val numberTable: TableEnum,
    val numberOfInstallments: Int,
    val flag: CreditCardFlagEnum,
    val machineFee: Double,
    val clientFee: Double,
)

fun RequestFeeUpdate.toFee() =
    Fee(
        id = this.id,
        numberTable = this.numberTable,
        numberOfInstallments = this.numberOfInstallments,
        flag = this.flag,
        machineFee = this.machineFee,
        clientFee = this.clientFee,
    )
