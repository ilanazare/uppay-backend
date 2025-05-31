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
        val request = CustomerRequest("nonExistingCustomer", "email@test.com")
        every { customerRepository.findCustomerByCustomer(request.customer) } returns null
        every { customerRepository.save(request.toCustomer()) } returns request.toCustomer()

        customerService.saveCustomer(request)

        verify(exactly = 1) { customerRepository.findCustomerByCustomer(request.customer) }
        verify(exactly = 1) { customerRepository.save(request.toCustomer()) }
    }

    @Test
    fun `saveCustomer should throw CustomerDoesExistException when customer already exists`() {
        val request = CustomerRequest("existingCustomer", "email@email.com")
        val existingCustomer = request.toCustomer()
        every { customerRepository.findCustomerByCustomer(existingCustomer.customer) } returns existingCustomer

        val response =
            assertThrows(CustomerDoesExistException::class.java) {
                customerService.saveCustomer(request)
            }

        verify(exactly = 1) { customerRepository.findCustomerByCustomer(existingCustomer.customer) }
        verify(exactly = 0) { customerRepository.save(request.toCustomer()) }
        assertEquals("Customer does exist in the database", response.message)
    }

    @Test
    fun `updateFee should update customer when customer exists`() {
        val request = CustomerRequest("existingCustomer", "newEmail@email.com")
        val existingCustomer = Customer("existingCustomer", "oldEmail@email.com")
        every { customerRepository.findCustomerByCustomer(request.customer) } returns existingCustomer
        every { customerRepository.save(request.toCustomer()) } returns request.toCustomer()

        customerService.updateCustomer(request)

        verify(exactly = 1) { customerRepository.findCustomerByCustomer(request.customer) }
        verify(exactly = 1) { customerRepository.save(request.toCustomer()) }
    }

    @Test
    fun `updateFee should throw CustomerNotFoundException when customer does not exist`() {
        val request = CustomerRequest("nonExistingCustomer", "newEmail@email.com")
        every { customerRepository.findCustomerByCustomer(request.customer) } returns null

        val response =
            assertThrows(CustomerNotFoundException::class.java) {
                customerService.findCustomerByCustomer(request.customer)
            }

        verify(exactly = 1) { customerRepository.findCustomerByCustomer(request.customer) }
        verify(exactly = 0) { customerRepository.save(any()) }
        assertEquals("Customer not found in the database", response.message)
    }

    @Test
    fun `findCustomerByCustomer should return customer response when customer exists`() {
        val customer = "existingCustomer"
        val expectedResponse = Customer(customer, "existingEmail@email.com")

        every { customerRepository.findCustomerByCustomer(customer) } returns expectedResponse

        val response = customerService.findCustomerByCustomer(customer)

        verify(exactly = 1) { customerRepository.findCustomerByCustomer(customer) }
        assertEquals(expectedResponse.toCustomerResponse(), response)
        assertEquals(expectedResponse.toCustomerResponse().customer, response.customer)
        assertEquals(expectedResponse.toCustomerResponse().email, response.email)
    }

    @Test
    fun `findCustomerByCustomer should throw CustomerNotFoundException when customer does not exist`() {
        val customer = "nonExistingCustomer"
        every { customerRepository.findCustomerByCustomer(customer) } returns null

        val response =
            assertThrows(CustomerNotFoundException::class.java) {
                customerService.findCustomerByCustomer(customer)
            }
        verify(exactly = 1) { customerRepository.findCustomerByCustomer(customer) }
        assertEquals("Customer not found in the database", response.message)
    }
}
