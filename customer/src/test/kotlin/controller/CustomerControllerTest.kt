package controller

import org.example.CustomerApplication
import org.example.web.controller.CustomerController
import org.example.web.request.CustomerRequest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import kotlin.test.assertEquals

@SpringBootTest(classes = [CustomerApplication::class])
class CustomerControllerTest {
    @Autowired
    private lateinit var customerController: CustomerController

    @Test
    fun `saveCustomer should return OK when customer does not exist`() {
        val uniqueCustomerName = "newCustomer_${System.currentTimeMillis()}"
        val request = CustomerRequest(uniqueCustomerName, "new@email.com")

        val response = customerController.saveCustomer(request)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("Customer saved successfully", response.body)
    }

    @Test
    fun `saveCustomer should return CONFLICT when customer exists in the database`() {
        val request = CustomerRequest("existingCustomer", "updated@email.com")

        val response = customerController.saveCustomer(request)

        assertEquals(HttpStatus.CONFLICT, response.statusCode)
        assertEquals("Customer does exist in the database", response.body)
    }

    @Test
    fun `updateCustomer should return OK when customer does exist`() {
        val request = CustomerRequest("existingCustomer", "email@email.com")

        val response = customerController.updateCustomer(request)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertTrue(response.body!!.contains("Customer updated successfully"))
    }

    @Test
    fun `updateCustomer should return NOT_FOUND when customer does not exist`() {
        val uniqueCustomerName = "nonExistentCustomer_${System.currentTimeMillis()}"
        val request = CustomerRequest(uniqueCustomerName, "nonexistent@email.com")

        val response = customerController.updateCustomer(request)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertEquals("Customer not found in the database", response.body)
    }

    @Test
    fun `findCustomerByCustomer should return OK when customer does exist`() {
        val customer = "existingCustomer"

        val response = customerController.findCustomerByCustomer(customer)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(customer, response.body?.customer)
        assertEquals("email@email.com", response.body?.email)
    }

    @Test
    fun `findCustomerByCustomer should return NOT_FOUND when customer does not exist`() {
        val uniqueCustomerName = "nonExistentCustomer_${System.currentTimeMillis()}"

        val response = customerController.findCustomerByCustomer(uniqueCustomerName)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertEquals<Any?>("Customer not found in the database", response.body)
    }
}
