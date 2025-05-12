package com.services

import com.domain.entity.Fee
import com.domain.enums.CreditCardFlagEnum
import com.domain.enums.TableEnum
import com.exceptions.FeeDoesExistException
import com.exceptions.FeeNotFoundException
import com.repository.FeeRepository
import org.springframework.stereotype.Service

@Service
class FeeService(
    val feeRepository: FeeRepository,
) {
    fun saveFee(request: Fee) {
        val cardFee =
            feeRepository.findCardFeeByNumberTableAndNumberOfInstallmentsAndFlag(
                request.numberTable,
                request.numberOfInstallments,
                request.flag,
            )
        feeRepository
            .save(request)
            .takeIf { cardFee == null } ?: throw FeeDoesExistException("Card fee does exist in the database")
    }

    fun updateFee(request: Fee) {
        val cardFee =
            feeRepository.findCardFeeByNumberTableAndNumberOfInstallmentsAndFlag(
                request.numberTable,
                request.numberOfInstallments,
                request.flag,
            )
        feeRepository
            .save(request)
            .takeIf { cardFee != null } ?: throw FeeNotFoundException("Card fee not found in the database")
    }

    fun findCardFeeByNumberOfInstallmentsAndFlag(
        numberTable: TableEnum,
        numberOfInstallments: Int,
        flag: CreditCardFlagEnum,
    ) = feeRepository.findCardFeeByNumberTableAndNumberOfInstallmentsAndFlag(numberTable, numberOfInstallments, flag)
}
