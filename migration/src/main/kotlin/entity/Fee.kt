package com.entity

import com.enums.CreditCardFlagEnum
import com.enums.TableEnum
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "fee")
data class Fee(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,
    @Enumerated(EnumType.STRING)
    val numberTable: TableEnum,
    val numberOfInstallments: Int,
    @Enumerated(EnumType.STRING)
    val flag: CreditCardFlagEnum,
    val machineFee: Double,
    val clientFee: Double,
)
