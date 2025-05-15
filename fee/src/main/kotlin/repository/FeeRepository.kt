package com.repository

import com.domain.entity.Fee
import com.domain.enums.CreditCardFlagEnum
import com.domain.enums.TableEnum
import org.springframework.data.repository.CrudRepository

interface FeeRepository : CrudRepository<Fee, String> {
    fun save(request: Fee): Fee

    fun findCardFeeByNumberTableAndNumberOfInstallmentsAndFlag(
        numberTable: TableEnum,
        numberOfInstallments: Int,
        flag: CreditCardFlagEnum,
    ): Fee?
}
