package user

import com.domain.entity.User
import com.repository.UserRepository
import com.service.UserService
import com.web.response.UserResponse
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.password.PasswordEncoder

class UserServiceTest {
    private lateinit var userService: UserService
    private lateinit var userRepository: UserRepository
    private lateinit var passwordEncoder: PasswordEncoder

    @BeforeEach
    fun setUp() {
        userRepository = mockk()
        passwordEncoder = mockk()
        userService = UserService(userRepository, passwordEncoder)
    }

    @Test
    fun `save should encode password and save user when username exist`() {
        val rawPassword = "plainPassword"
        val encodedPassword = "encodedPassword"
        val requestUser = User(username = "testUser", password = rawPassword, authorities = "ADMIN")
        val savedUser = requestUser.copy(password = encodedPassword)
        val expectedResponse = UserResponse(username = "testUser", password = encodedPassword, authorities = "ADMIN")

        every { passwordEncoder.encode(rawPassword) } returns encodedPassword
        every { userRepository.findUserByUsername("testUser") } returns null
        every { userRepository.save(savedUser) } returns savedUser

        val result = userService.save(requestUser)

        assertEquals(expectedResponse, result)
        verify { passwordEncoder.encode(rawPassword) }
        verify { userRepository.findUserByUsername("testUser") }
        verify { userRepository.save(savedUser) }
    }

//    @Test
//    fun `save should throw UserDoesExistException when username doesn't exists`() {
//        val existingUser = User(username = "existingUser", password = "password", authorities = "ADMIN")
//        every { userRepository.findUserByUsername("existingUser") } returns existingUser
//
//        assertThrows(UserDoesExistException::class.java) {
//            userService.save(existingUser)
//        }
//
//        verify { userRepository.findUserByUsername("existingUser") }
//        verify(exactly = 0) { userRepository.save(any()) }
//    }

//    @Test
//    fun `save should not call password encoder when user already exists`() {
//        val existingUser = User(username = "existingUser", password = "password", authorities = "ADMIN")
//        every { userRepository.findUserByUsername("existingUser") } returns existingUser
//
//        assertThrows(UserDoesExistException::class.java) {
//            userService.save(existingUser)
//        }
//
//        verify(exactly = 0) { passwordEncoder.encode(any()) }
//    }

    @Test
    fun `save should return UserResponse with correct username`() {
        val rawPassword = "plainPassword"
        val encodedPassword = "encodedPassword"
        val requestUser = User(username = "testUser", password = rawPassword, authorities = "ADMIN")
        val savedUser = requestUser.copy(password = encodedPassword)

        every { passwordEncoder.encode(rawPassword) } returns encodedPassword
        every { userRepository.findUserByUsername("testUser") } returns null
        every { userRepository.save(savedUser) } returns savedUser

        val result = userService.save(requestUser)

        assertEquals("testUser", result?.username)
    }

    @Test
    fun `save should modify user password with encoded version before saving`() {
        val rawPassword = "plainPassword"
        val encodedPassword = "encodedPassword"
        val requestUser = User(username = "testUser", password = rawPassword, authorities = "ADMIN")

        every { passwordEncoder.encode(rawPassword) } returns encodedPassword
        every { userRepository.findUserByUsername("testUser") } returns null
        every { userRepository.save(any()) } answers {
            val user = firstArg<User>()
            assertEquals(encodedPassword, user.password)
            user
        }

        userService.save(requestUser)

        verify {
            userRepository.save(
                withArg {
                    assertEquals(encodedPassword, it.password)
                },
            )
        }
    }
}
