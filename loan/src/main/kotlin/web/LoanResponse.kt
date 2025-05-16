package org.example.web

import org.example.domain.entity.Loan
import java.time.LocalDateTime

data class LoanResponse(
    val id: Long,
    val customer: String,
    val purchaseValue: Double,
    val numberOfInstallments: Int,
    val installmentValue: Double,
    val amountRetainedByMachine: Double,
    val amountReleasedByMachine: Double,
    val amountRetained: Double,
    val amountReleasedForClient: Double,
    val purchaseDate: LocalDateTime,
)

fun Loan.toLoanResponse() =
    LoanResponse(
        id = this.id,
        customer = this.customer,
        purchaseValue = this.purchaseValue,
        numberOfInstallments = this.numberOfInstallments,
        installmentValue = this.installmentValue,
        amountRetainedByMachine = this.amountRetainedByMachine,
        amountReleasedByMachine = this.amountReleasedByMachine,
        amountRetained = this.amountRetained,
        amountReleasedForClient = this.amountReleasedForClient,
        purchaseDate = this.purchaseDate,
    )
