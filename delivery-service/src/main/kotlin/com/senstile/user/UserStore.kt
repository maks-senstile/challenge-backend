package com.senstile.user

import org.springframework.stereotype.Component

/**
 * Абстрактное хранилище пользователей
 */
@Component
class UserStore(
    private val userClient: UserClient
) {

    suspend fun getUserAddress(userId: Int, addressId: Int): UserAddress {
        val user = userClient.requireUser(userId)

        return UserAddress(
            id = user.id,
            firstName = user.firstName,
            address = user.requireAddress(addressId).let { address ->
                UserAddress.Address(
                    id = address.id,
                    city = address.city,
                    country = address.country,
                    postalCode = address.postalCode,
                    addressLine = address.addressLine
                )
            }
        )
    }
}