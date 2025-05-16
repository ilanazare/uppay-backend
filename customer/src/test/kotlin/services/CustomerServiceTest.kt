package services

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.entity.Customer
import org.example.exceptions.CustomerDoesExistException
import org.example.exceptions.CustomerNotFoundException
import org.example.repository.CustomerRepository
import org.example.services.CustomerService
import org.example.web.request.CustomerRequest
import org.example.web.request.toCustomer
import org.example.web.response.toCustomerResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class CustomerServiceTest {
    private val customerRepository = mockk<CustomerRepository>()
    private val customerService = CustomerService(customerRepository)

    @Test
    fun `saveCustomer should save new customer when customer does not exist`() {
        // Given
        val request = CustomerRequest("customer1", "email@test.com")
        every { customerRepository.findCustomerByCustomer("customer1") } returns null
        every { customerRepository.save(any()) } returns request.toCustomer()

        // When
        customerService.saveCustomer(request)

        // Then
        verify(exactly = 1) { customerRepository.save(any()) }
    }

    @Test
    fun `saveCustomer should throw CustomerDoesExistException when customer already exists`() {
        // Given
        val request = CustomerRequest("customer1", "email@test.com")
        val existingCustomer = request.toCustomer()
        every { customerRepository.findCustomerByCustomer("customer1") } returns existingCustomer

        // When & Then
        assertThrows(CustomerDoesExistException::class.java) {
            customerService.saveCustomer(request)
        }
        verify(exactly = 0) { customerRepository.save(any()) }
    }

    @Test
    fun `updateFee should update customer when customer exists`() {
        // Given
        val request = CustomerRequest("customer1", "newemail@test.com")
        val existingCustomer = Customer("customer1", "oldemail@test.com")
        every { customerRepository.findCustomerByCustomer("customer1") } returns existingCustomer
        every { customerRepository.save(any()) } returns request.toCustomer()

        // When
        customerService.updateCustomer(request)

        // Then
        verify(exactly = 1) { customerRepository.save(any()) }
    }
//
//    @Test
//    fun `updateFee should throw CustomerNotFoundException when customer does not exist`() {
//        // Given
//        val request = CustomerRequest("customer1", "email@test.com")
//        every { customerRepository.findCustomerByCustomer("customer1") } returns null
//
//        // When & Then
//        assertThrows(CustomerNotFoundException::class.java) {
//            customerService.updateFee(request)
//        }
//        verify(exactly = 0) { customerRepository.save(any()) }
//    }

    @Test
    fun `findCardFeeByNumberOfInstallmentsAndFlag should return customer response when customer exists`() {
        // Given
        val customer = Customer("customer1", "email@test.com")
        val expectedResponse = customer.toCustomerResponse()
        every { customerRepository.findCustomerByCustomer("customer1") } returns customer

        // When
        val result = customerService.findCustomerByCustomer("customer1")

        // Then
        assertEquals(expectedResponse, result)
    }

    @Test
    fun `findCardFeeByNumberOfInstallmentsAndFlag should throw CustomerNotFoundException when customer does not exist`() {
        // Given
        every { customerRepository.findCustomerByCustomer("customer1") } returns null

        // When & Then
        assertThrows(CustomerNotFoundException::class.java) {
            customerService.findCustomerByCustomer("customer1")
        }
    }
}
