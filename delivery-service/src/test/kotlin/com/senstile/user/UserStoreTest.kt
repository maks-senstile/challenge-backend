package com.senstile.user

import com.senstile.domain.exceptions.ObjectNotFoundException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserStoreTest {

    @Test
    fun getUserAddress(): Unit = runBlocking {
        val userClient = mockk<UserClient>().also {
            coEvery { it.requireUser(1) } returns stubUser1
        }
        val userStore = UserStore(userClient)

        val userAddress = userStore.getUserAddress(1, 2)

        assertEquals(stubUser1.id, userAddress.id)
        assertEquals(stubUser1.addresses?.first()?.city, userAddress.address.city)
    }

    @Test
    fun userAddressNotFound(): Unit = runBlocking {
        val userClient = mockk<UserClient>().also {
            coEvery { it.requireUser(1) } throws(ObjectNotFoundException("User not found."))
        }
        val userStore = UserStore(userClient)

        assertThrows<ObjectNotFoundException> {
            userStore.getUserAddress(1, 2)
        }
    }

    private val stubUser1 = UserModel(
        id = 1,
        firstName = "Вася",
        addresses = listOf(
            AddressModel(
                id = 2,
                city = "Москва",
                addressLine = "",
                country = "Russia",
                postalCode = "111555"
            )
        )
    )
}