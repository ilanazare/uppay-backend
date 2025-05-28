import com.domain.enums.CreditCardFlagEnum
import com.domain.enums.TableEnum
import com.exceptions.FeeDoesExistException
import com.exceptions.FeeNotFoundException
import com.repository.FeeRepository
import com.services.FeeService
import com.web.request.RequestFee
import com.web.request.RequestFeeUpdate
import com.web.request.toFee
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class FeeServiceTest {
    private lateinit var feeRepository: FeeRepository
    private lateinit var feeService: FeeService

    @BeforeEach
    fun setup() {
        feeRepository = mockk()
        feeService = FeeService(feeRepository)
    }

    @Test
    fun `saveFee should save when fee doesn't exist`() {
        // Given
        val requestFee =
            RequestFee(
                numberTable = TableEnum.ONE,
                numberOfInstallments = 1,
                flag = CreditCardFlagEnum.MASTER_VISA,
                machineFee = 1.0,
                clientFee = 2.0,
            )

        every {
            feeRepository.findCardFeeByNumberTableAndNumberOfInstallmentsAndFlag(
                any(),
                any(),
                any(),
            )
        } returns null

        every { feeRepository.save(any()) } returns mockk()

        // When / Then
        assertDoesNotThrow { feeService.saveFee(requestFee) }
        verify(exactly = 1) { feeRepository.save(any()) }
    }

    @Test
    fun `saveFee should throw FeeDoesExistException when fee exists`() {
        // Given
        val requestFee =
            RequestFee(
                numberTable = TableEnum.ONE,
                numberOfInstallments = 1,
                flag = CreditCardFlagEnum.MASTER_VISA,
                machineFee = 1.0,
                clientFee = 2.0,
            )

        every {
            feeRepository.findCardFeeByNumberTableAndNumberOfInstallmentsAndFlag(
                any(),
                any(),
                any(),
            )
        } returns mockk()

        // When / Then
        assertThrows(FeeDoesExistException::class.java) {
            feeService.saveFee(requestFee)
        }
        verify(exactly = 0) { feeRepository.save(any()) }
    }

    @Test
    fun `updateFee should update when fee exists`() {
        // Given
        val requestFeeUpdate =
            RequestFeeUpdate(
                id = 1L,
                numberTable = TableEnum.ONE,
                numberOfInstallments = 1,
                flag = CreditCardFlagEnum.MASTER_VISA,
                machineFee = 1.0,
                clientFee = 2.0,
            )

        val existingFee = requestFeeUpdate.toFee()

        every {
            feeRepository.findCardFeeByNumberTableAndNumberOfInstallmentsAndFlag(
                any(),
                any(),
                any(),
            )
        } returns existingFee

        every { feeRepository.save(any()) } returns existingFee

        // When / Then
        assertDoesNotThrow { feeService.updateFee(requestFeeUpdate) }
        verify(exactly = 1) { feeRepository.save(any()) }
    }

//    @Test
//    fun `updateFee should throw FeeNotFoundException when fee doesn't exist`() {
//        // Given
//        val requestFeeUpdate =
//            RequestFeeUpdate(
//                id = 1L,
//                numberTable = TableEnum.ONE,
//                numberOfInstallments = 1,
//                flag = CreditCardFlagEnum.MASTER_VISA,
//                machineFee = 1.0,
//                clientFee = 2.0,
//            )
//
//        every {
//            feeRepository.findCardFeeByNumberTableAndNumberOfInstallmentsAndFlag(
//                any(),
//                any(),
//                any(),
//            )
//        } returns null
//
//        // When / Then
//        assertThrows(FeeNotFoundException::class.java) {
//            feeService.updateFee(requestFeeUpdate)
//        }
//        verify(exactly = 0) { feeRepository.save(any()) }
//    }

//    @Test
//    fun `findCardFeeByNumberOfInstallmentsAndFlag should return fee when exists`() {
//        // Given
//        val numberTable = TableEnum.ONE
//        val numberOfInstallments = 1
//        val flag = CreditCardFlagEnum.MASTER_VISA
//
//        val expectedFee =
//            mockk<FeeResponse> {
//                every { id } returns 1L
//                every { numberTable } returns numberTable
//                every { numberOfInstallments } returns numberOfInstallments
//                every { flag } returns flag
//                every { machineFee } returns 1.0
//                every { clientFee } returns 2.0
//            }
//
//        every {
//            feeRepository.findCardFeeByNumberTableAndNumberOfInstallmentsAndFlag(
//                numberTable,
//                numberOfInstallments,
//                flag,
//            )
//        } returns mockk()
//
//        // When
//        val result =
//            feeService.findCardFeeByNumberOfInstallmentsAndFlag(
//                numberTable,
//                numberOfInstallments,
//                flag,
//            )
//
//        // Then
//        assertEquals(expectedFee.id, result.id)
//        assertEquals(expectedFee.numberTable, result.numberTable)
//        assertEquals(expectedFee.numberOfInstallments, result.numberOfInstallments)
//        assertEquals(expectedFee.flag, result.flag)
//        assertEquals(expectedFee.machineFee, result.machineFee)
//        assertEquals(expectedFee.clientFee, result.clientFee)
//    }

    @Test
    fun `findCardFeeByNumberOfInstallmentsAndFlag should throw FeeNotFoundException when fee doesn't exist`() {
        // Given
        val numberTable = TableEnum.ONE
        val numberOfInstallments = 1
        val flag = CreditCardFlagEnum.MASTER_VISA

        every {
            feeRepository.findCardFeeByNumberTableAndNumberOfInstallmentsAndFlag(
                numberTable,
                numberOfInstallments,
                flag,
            )
        } returns null

        // When / Then
        assertThrows(FeeNotFoundException::class.java) {
            feeService.findCardFeeByNumberOfInstallmentsAndFlag(
                numberTable,
                numberOfInstallments,
                flag,
            )
        }
    }
}
