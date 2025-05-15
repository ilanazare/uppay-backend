package com.services

import com.domain.enums.CreditCardFlagEnum
import com.domain.enums.TableEnum
import com.exceptions.FeeDoesExistException
import com.exceptions.FeeNotFoundException
import com.repository.FeeRepository
import com.web.request.RequestFee
import com.web.request.RequestFeeUpdate
import com.web.request.toFee
import com.web.request.toFeeFromRequestFee
import com.web.response.FeeResponse
import com.web.response.toFeeResponseFromFee
import org.springframework.stereotype.Service

@Service
class FeeService(
    val feeRepository: FeeRepository,
) {
    fun saveFee(request: RequestFee) {
        val cardFee =
            feeRepository.findCardFeeByNumberTableAndNumberOfInstallmentsAndFlag(
                request.numberTable,
                request.numberOfInstallments,
                request.flag,
            )
        if (cardFee == null) {
            feeRepository
                .save(request.toFeeFromRequestFee())
        } else {
            throw FeeDoesExistException("Fee does exist in the database")
        }
    }

    fun updateFee(request: RequestFeeUpdate) {
        try {
            val cardFee =
                feeRepository.findCardFeeByNumberTableAndNumberOfInstallmentsAndFlag(
                    request.numberTable,
                    request.numberOfInstallments,
                    request.flag,
                )
            feeRepository
                .save(request.toFee())
                .takeIf { (cardFee != null) && (request.id == cardFee.id) }
        } catch (e: Exception) {
            throw FeeNotFoundException("Fee not found in the database")
        }
    }

    fun findCardFeeByNumberOfInstallmentsAndFlag(
        numberTable: TableEnum,
        numberOfInstallments: Int,
        flag: CreditCardFlagEnum,
    ): FeeResponse =
        feeRepository
            .findCardFeeByNumberTableAndNumberOfInstallmentsAndFlag(numberTable, numberOfInstallments, flag)
            ?.toFeeResponseFromFee() ?: throw FeeNotFoundException("Fee not found in the database")
}
