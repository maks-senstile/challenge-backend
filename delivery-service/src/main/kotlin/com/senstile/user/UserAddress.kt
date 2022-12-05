package com.senstile.user

data class UserAddress(
    val id: Int,
    val firstName: String?,
    val address: Address
) {

    data class Address(
        val id: Int,
        val addressLine: String?,
        val city: String?,
        val country: String?,
        val postalCode: String?
    )
}