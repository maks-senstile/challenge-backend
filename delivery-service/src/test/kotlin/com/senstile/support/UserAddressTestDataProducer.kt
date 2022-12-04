package com.senstile.support

import com.senstile.user.UserAddress

object UserAddressTestDataProducer {

    fun next(id: Int = 1, addressId: Int = 1): UserAddress {
        return UserAddress(
            id = id,
            firstName = "вася",
            address = UserAddress.Address(
                id = addressId,
                city = "Москва",
                addressLine = "",
                country = "Russia",
                postalCode = "111555"
            )
        )
    }
}