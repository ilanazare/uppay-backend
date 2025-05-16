package org.example.services

import org.example.client.CustomerClient
import org.example.client.FeeClient
import org.example.domain.entity.Loan
import org.example.domain.enums.CreditCardFlagEnum
import org.example.domain.enums.TableEnum
import org.example.exceptions.CustomerNotFoundException
import org.example.repository.LoanRepository
import org.example.web.LoanResponse
import org.example.web.request.LoanRequest
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class LoanService(
    val customerClient: CustomerClient,
    val feeClient: FeeClient,
    val loanRepository: LoanRepository,
) {
    fun saveLoan(
        tableNumber: TableEnum,
        request: LoanRequest,
    ) {
        val response = customerClient.findCustomerByCustomer(request.customer)
        if (!response.statusCode.is2xxSuccessful || response.body == null) {
            throw CustomerNotFoundException("Customer, ${request.customer}, not found in database")
        }

        val customer = response.body!!.customer
        val amountReleasedByMachine =
            calculatesAmountReleasedByMachine(tableNumber, request.purchaseValue, request.numberOfInstallments, request.flag)
        val amountRetainedByMachine =
            calculatesAmountRetainedByMachine(tableNumber, request.purchaseValue, request.numberOfInstallments, request.flag)
        val installmentValue = calculateInstallmentValue(request.purchaseValue, request.numberOfInstallments)
        val amountRetained = calculatesAmountRetained(tableNumber, request.purchaseValue, request.numberOfInstallments, request.flag)
        val amountReleasedForClient = amountReleasedByMachine.minus(amountRetained)
        loanRepository
            .save(
                Loan(
                    id = 0L,
                    customer = request.customer,
                    purchaseValue = request.purchaseValue,
                    numberOfInstallments = request.numberOfInstallments,
                    installmentValue = installmentValue,
                    amountRetainedByMachine = amountRetainedByMachine,
                    amountReleasedByMachine = amountReleasedByMachine,
                    amountRetained = amountRetained,
                    amountReleasedForClient = amountReleasedForClient,
                    purchaseDate = LocalDateTime.now(),
                ),
            ).takeIf { request.customer == customer }
    }

    fun findListAmountByCustomer(customer: String): List<LoanResponse> =
        try {
            loanRepository.findListLoanByCustomer(customer)
        } catch (e: Exception) {
            throw CustomerNotFoundException("Customer, $customer, not found in database")
        }

    private fun calculateInstallmentValue(
        purchaseValue: Double,
        numberOfInstallments: Int,
    ) = purchaseValue.div(numberOfInstallments)

    private fun calculatesAmountReleasedByMachine(
        tableNumber: TableEnum,
        purchaseValue: Double,
        numberOfInstallments: Int,
        flag: CreditCardFlagEnum,
    ): Double {
        val machineFee = feeClient.findCardFeeByNumberOfInstallmentsAndFlag(tableNumber, numberOfInstallments, flag).body?.machineFee
        val amountReleasedByMachine = purchaseValue.times(1 - (machineFee!!.div(100)))
        return amountReleasedByMachine
    }

    private fun calculatesAmountRetainedByMachine(
        tableNumber: TableEnum,
        purchaseValue: Double,
        numberOfInstallments: Int,
        flag: CreditCardFlagEnum,
    ): Double {
        val machineFee = feeClient.findCardFeeByNumberOfInstallmentsAndFlag(tableNumber, numberOfInstallments, flag).body?.machineFee
        val amountReleasedByMachine = purchaseValue.times(1 - (machineFee!!.div(100)))
        val amountRetainedByMachine = purchaseValue.minus(amountReleasedByMachine)
        return amountRetainedByMachine
    }

    private fun calculatesAmountRetained(
        tableNumber: TableEnum,
        purchaseValue: Double,
        numberOfInstallments: Int,
        flag: CreditCardFlagEnum,
    ): Double {
        val machineFee = feeClient.findCardFeeByNumberOfInstallmentsAndFlag(tableNumber, numberOfInstallments, flag).body?.machineFee
        val clientFee = feeClient.findCardFeeByNumberOfInstallmentsAndFlag(tableNumber, numberOfInstallments, flag).body?.clientFee
        val amountRetained = purchaseValue.times((machineFee?.let { clientFee?.minus(it) })!!.div(100))
        return amountRetained
    }
}
