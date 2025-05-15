package com.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "customers")
data class Customer(
    @Id
    @Column(name = "customer", unique = true)
    val customer: String,
    val email: String,
)
