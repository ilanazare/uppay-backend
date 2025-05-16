package org.example.repository

import org.example.domain.entity.Loan
import org.example.web.LoanResponse
import org.springframework.data.repository.CrudRepository

interface LoanRepository : CrudRepository<Loan, String> {
    fun findListLoanByCustomer(customer: String): List<LoanResponse>
}
