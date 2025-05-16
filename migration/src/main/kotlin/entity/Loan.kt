package com.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "loan")
data class Loan(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,
    @Column(name = "customer", unique = true)
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
